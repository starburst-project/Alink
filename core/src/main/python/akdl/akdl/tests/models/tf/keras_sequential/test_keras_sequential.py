import json

import tensorflow as tf
from akdl.models.tf.keras_sequential import keras_sequential_main
from akdl.runner.config import TrainTaskConfig

if tf.__version__ >= '2.0':
    tf = tf.compat.v1

# tf.compat.v1.enable_eager_execution()


def test_keras_sequential_multi_classification(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    with open(tmp_path / 'bc_data_1', 'w') as f:
        f.write("10")

    model_config = {
        'layers': [
            "Reshape((28, 28, 1))",
            "Conv2D(5, 7)",
            "Flatten()",
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'double',
        'batch_size': '32',
        'num_epochs': '2',
        'model_config': json.dumps(model_config),
        'ALINK:bc_1': str(tmp_path / 'bc_data_1')
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='multi_classification_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=100,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_multi_classification_one_worker(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    with open(tmp_path / 'bc_data_1', 'w') as f:
        f.write("10")

    model_config = {
        'layers': [
            "Reshape((28, 28, 1))",
            "Conv2D(5, 7)",
            "Flatten()",
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'double',
        'batch_size': '32',
        'num_epochs': '1',
        'model_config': json.dumps(model_config),
        'ALINK:ONLY_ONE_WORKER': 'true',
        'validation_split': '0.2',
        'save_checkpoints_epochs': '1.',
        'save_best_only': 'true',
        'best_exporter_metric': 'msle',
        'ALINK:bc_1': str(tmp_path / 'bc_data_1')
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='multi_classification_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=8000,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_binary_classification(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    with open(tmp_path / 'bc_data_1', 'w') as f:
        f.write("2")

    model_config = {
        'layers': [
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '32',
        'num_epochs': '2',
        'model_config': json.dumps(model_config),
        'ALINK:bc_1': str(tmp_path / 'bc_data_1')
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='binary_classification_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=100,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_binary_classification_one_worker(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    with open(tmp_path / 'bc_data_1', 'w') as f:
        f.write("2")

    model_config = {
        'layers': [
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '32',
        'num_epochs': '2',
        'ALINK:ONLY_ONE_WORKER': 'true',
        'validation_split': '0.2',
        'save_checkpoints_epochs': '0.2',
        'save_best_only': 'true',
        'best_exporter_metric': 'binary_accuracy',
        'metric_bigger': 'true',
        'model_config': json.dumps(model_config),
        'ALINK:bc_1': str(tmp_path / 'bc_data_1')
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='binary_classification_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=8000,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_regression(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    model_config = {
        'layers': [
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '32',
        'num_epochs': '2',
        'model_config': json.dumps(model_config),
        'optimizer': 'Adam(learning_rate=0.1)'
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='regression_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=100,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_regression_one_worker(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [784],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'double'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    model_config = {
        'layers': [
            "Dense(1024, activation='relu')",
            "Dense(512, activation='relu')",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '32',
        'num_epochs': '10',
        'ALINK:ONLY_ONE_WORKER': 'true',
        'validation_split': '0.2',
        'save_checkpoints_epochs': '0.2',
        'save_best_only': 'true',
        'best_exporter_metric': 'mean_squared_logarithmic_error',
        'model_config': json.dumps(model_config),
        'optimizer': 'Adam(learning_rate=0.1)'
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='regression_dataset.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=8000,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_keras_sequential_classification_string(tmp_path):
    print(tmp_path)
    tensor_shapes = {
        'tensor': [],
    }
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {
        'tensor': 'string'
    }
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    with open(tmp_path / 'bc_data_1', 'w') as f:
        f.write("2")

    model_config = {
        'layers': [
            "hub.KerasLayer('https://tfhub.dev/google/nnlm-de-dim50/2', input_shape=[1], dtype=tf.string)",
            "Flatten()",
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '32',
        'num_epochs': '2',
        'model_config': json.dumps(model_config),
        'optimizer': 'Adam(learning_rate=0.1)'
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='binary_classification_string_data.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=100,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)


def test_auto_encoder(tmp_path):
    print(tmp_path)
    tensor_shapes = {'label': [28, 28], 'tensor': [28, 28]}
    json.dump(tensor_shapes, open(tmp_path / 'tensor_shapes.txt', "w"))
    tensor_types = {'label': 'float', 'tensor': 'float'}
    json.dump(tensor_types, open(tmp_path / 'tensor_types.txt', "w"))

    model_config = {
        'layers': [
            "tf.keras.Sequential([Flatten(),Dense(64, activation='relu'),])",
            "tf.keras.Sequential([Dense(784, activation='sigmoid'),Reshape((28, 28))], name='output')"
        ]
    }

    user_params = {
        'tensor_cols': json.dumps(['tensor']),
        'label_col': 'label',
        'label_type': 'float',
        'batch_size': '128',
        'add_output_layer': 'false',
        'num_epochs': '1',
        'model_config': json.dumps(model_config),
        'metrics': json.dumps(['mse', 'mae', 'tf.keras.metrics.RootMeanSquaredError()']),
        'optimizer': 'Adam(learning_rate=0.1)',
        'loss': 'tf.keras.losses.BinaryCrossentropy()',
    }
    args: TrainTaskConfig = TrainTaskConfig(
        dataset_file='autoencoder.tfrecords',
        tf_context=None,
        num_workers=1, cluster=None, task_type='chief', task_index=0,
        work_dir=str(tmp_path),
        dataset=None, dataset_length=100,
        saved_model_dir=str(tmp_path / 'saved_model_dir'),
        user_params=user_params)
    keras_sequential_main.main(args)
