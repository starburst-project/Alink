package com.alibaba.alink.common.io.plugin;

import org.apache.flink.api.java.tuple.Tuple2;

import com.alibaba.alink.common.AlinkGlobalConfiguration;
import com.alibaba.alink.common.exceptions.PluginNotExistException;
import com.alibaba.alink.common.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClassLoaderContainer {

	private final static Logger LOG = LoggerFactory.getLogger(ClassLoaderContainer.class);

	private static final ClassLoaderContainer INSTANCE = new ClassLoaderContainer();

	public synchronized static ClassLoaderContainer getInstance() {
		return INSTANCE;
	}

	private final Map <RegisterKey, ClassLoader> registeredClassLoaders = new HashMap <>();

	private ClassLoaderContainer() {
	}

	public synchronized <T> ClassLoader create(
		RegisterKey key,
		DistributeCache distributeCache,
		Class <T> service,
		Predicate <T> serviceFilter,
		Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) {

		ClassLoader hit = registeredClassLoaders.get(key);

		if (hit != null) {
			return hit;
		}

		hit = registeredClassLoaders.get(new RegisterKey(key.getName(), null));

		if (hit != null) {
			return hit;
		}

		hit = loadFromThreadContext(key, service, serviceFilter);

		if (hit != null) {
			return hit;
		}

		try {
			hit = loadFromPlugin(key, distributeCache, service, serviceFilter, versionGetter);
		} catch (Exception e) {
			// pass
			LOG.warn("Could not find {} from plugin.", JsonConverter.toJson(key), e);
		}

		if (hit != null) {
			return hit;
		}

		LOG.warn("Could not find the service from factory. Return the thread context classloader by default.");

		return Thread.currentThread().getContextClassLoader();
	}

	private <T> ClassLoader filterFromServices(
		RegisterKey key, List <Tuple2 <T, PluginDescriptor>> loadedServices,
		Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) throws IOException {

		if (!loadedServices.isEmpty()) {

			ClassLoader hit = null;

			if (key.getVersion() == null) {
				hit = loadedServices.get(0).f0.getClass().getClassLoader();
			} else {
				for (Tuple2 <T, PluginDescriptor> loaded : loadedServices) {
					String version = versionGetter.apply(loaded);

					if (version.compareToIgnoreCase(key.getVersion()) == 0) {
						hit = loaded.f0.getClass().getClassLoader();
						break;
					}
				}
			}

			if (hit == null) {
				LOG.warn("Could not find the class loader of service: {} exactly", JsonConverter.toJson(key));

				if (loadedServices.size() > 1) {
					LOG.warn("Find multiple services for {}, select the first randomly.", JsonConverter.toJson(key));
				}

				key = new RegisterKey(key.getName(), null);

				hit = loadedServices.get(0).f0.getClass().getClassLoader();
			}

			registeredClassLoaders.put(key, hit);

			return hit;
		}

		throw new PluginNotExistException(
			String.format("Could not find the appropriate service. %s", JsonConverter.toJson(key))
		);
	}

	private <T> ClassLoader loadFromThreadContext(
		RegisterKey key, Class <T> service, Predicate <T> serviceFilter) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		Iterable<T> services = ServiceLoader.load(service, classLoader);

		for (T s : services) {
			if (serviceFilter.test(s)) {
				return classLoader;
			}
		}

		return null;
	}

	private <T> ClassLoader loadFromPlugin(
		RegisterKey key,
		DistributeCache distributeCache,
		Class <T> service,
		Predicate <T> serviceFilter,
		Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) throws IOException {

		final List <Tuple2 <T, PluginDescriptor>> loadedServices = new ArrayList <>();

		distributeCache.distributeAsLocalFile();

		PluginUtils.createJarsPluginManagerFromRootFolder(
				PluginUtils.readPluginConf(distributeCache.context())
			)
			.load(service, AlinkGlobalConfiguration.getFlinkVersion(), key.getName(), key.getVersion())
			.forEachRemaining(t -> {
				if (serviceFilter.test(t.f0)) {
					loadedServices.add(t);
				}
			});

		return filterFromServices(key, loadedServices, versionGetter);
	}

}
