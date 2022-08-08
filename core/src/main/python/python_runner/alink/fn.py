import base64
import json
import os
import shutil
import sys
import zipfile
from typing import Dict, List

from alink.py4j_gateway import get_class_from_name
from alink.type_conversion import to_py_value, to_java_value, to_java_values


def import_file(filename):
    import os
    from importlib.util import spec_from_file_location, module_from_spec
    module_name = os.path.splitext(filename)[0]
    print(f'module_name = {module_name}, filename = {filename}', flush=True)
    spec = spec_from_file_location(module_name, filename)
    print(f'spec = {spec}', flush=True)
    module = module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)


def import_paths(paths: List[str]):
    print(f'cwd = {os.getcwd()}', flush=True)
    to_be_added = []
    for p in paths:
        p = os.path.normcase(os.path.normpath(p))
        print(f'p = {p}', flush=True)
        if os.path.isfile(p):
            if p.endswith(".py"):
                import_file(p)
                print(f'Import file: {p}', flush=True)
            elif zipfile.is_zipfile(p):
                folder_name = p.rstrip(".zip")
                shutil.unpack_archive(p, extract_dir=folder_name)
                to_be_added.append(folder_name)
                print(f'Add dir to to_be_added: {folder_name}', flush=True)
            else:
                print(f'Not supported: {p}', flush=True)
        else:
            to_be_added.append(p)
            print(f'Add dir to to_be_added: {p}')
    print(f'before sys.path = {sys.path}', flush=True)
    for p in to_be_added:
        if os.path.exists(p):
            if p not in sys.path and p + os.sep not in sys.path:
                sys.path.append(p)
    print(f'sys.path = {sys.path}', flush=True)
    return to_be_added


class UdfConfig(object):
    def __init__(self, config_json):
        """
        the format of config_json is:
        {
           "paths": ["Python files or directories"],
           "className": "",
           "classObjectType": "DILL_BASE64 or CLOUDPICKLE_BASE64",
           "classObject": "Serialized Python code in BASE64"
        }
        """
        print('the config: {}'.format(config_json))
        self._config: Dict = json.loads(config_json)
        self._fn = None
        import_paths(self._config.get('paths', []))

    def get_fn(self):
        if self._fn is not None:
            return self._fn

        if 'className' in self._config:
            class_name = self._config['className']
            cls = get_class_from_name(class_name)
            self._fn = cls()
            return self._fn
        elif 'classObject' in self._config:
            code = self._config['classObject']
            class_object_type = self._config["classObjectType"]
            if class_object_type == 'DILL_BASE64':
                # noinspection PyUnresolvedReferences
                import dill
                # noinspection PyProtectedMember
                dill._dill._reverse_typemap['ClassType'] = type
                obj = dill.loads(base64.b64decode(code))
            elif class_object_type == 'CLOUDPICKLE_BASE64':
                import cloudpickle
                obj = cloudpickle.loads(base64.b64decode(code))
            else:
                raise ValueError("Invalid class object type: " + class_object_type)
            if callable(obj):  # if obj is a func, wrap it to class with eval
                obj = wrap_callable_to_class(obj)()
            self._fn = obj
            return self._fn
        else:
            raise RuntimeError('Missing class definition')


def wrap_callable_to_class(func):
    class CallableWrapper:
        # noinspection PyMethodMayBeStatic
        def eval(self, *args):
            return func(*args)

    return CallableWrapper


class PyScalarFn:
    def __init__(self):
        self._fn = None
        self._result_type = None

    def init(self, config_json: str, j_result_type: str):
        config = UdfConfig(config_json)
        self._fn = config.get_fn()
        self._result_type = j_result_type

    def eval(self, args):
        if args is None:
            return None
        import time
        start = time.time()
        args = to_py_value(args)
        if isinstance(args, (list,)):
            ret = self._fn.eval(*args)
        else:
            ret = self._fn.eval(args)
        ret = to_java_value(ret, self._result_type)
        end = time.time()
        print(f"Total Python eval time {end - start}", flush=True)
        return ret

    class Java:
        implements = ['com.alibaba.alink.common.pyrunner.fn.PyScalarFnHandle']


class PyTableFn:
    def __init__(self):
        self._fn = None
        self._collector = None
        self._result_types = None

    def init(self, collector, config_json: str, j_result_types: List[str]):
        config = UdfConfig(config_json)
        self._fn = config.get_fn()
        self._collector = collector
        self._result_types = [t for t in j_result_types]

    def eval(self, args):
        if args is None:
            return None
        args = to_py_value(args)
        if isinstance(args, (list,)):
            ret = self._fn.eval(*args)
        else:
            ret = self._fn.eval(args)
        # ret is a generator
        for row in ret:
            if not isinstance(row, (list, tuple)):
                row = (row,)
            row = to_java_values(list(row), list(self._result_types))
            self._collector.collect(row)

    class Java:
        implements = ['com.alibaba.alink.common.pyrunner.fn.PyTableFnHandle']
