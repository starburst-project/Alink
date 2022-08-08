package com.alibaba.alink.common.io.redis;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.exceptions.AkIllegalOperatorParameterException;
import com.alibaba.alink.params.io.RedisParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisFactoryImpl implements RedisFactory {
	private static final Logger LOG = LoggerFactory.getLogger(RedisFactoryImpl.class);
	private final int MAX_TOTAL = 500;
	private final int MAX_IDLE = 500;
	private final int MAX_ATTEMPTS = 10;
	private final int MAX_WAIT_MILLIS = 10000;
	private final int TIME_OUT = 10000;
	private final int DEFAULT_STANDALONE_PORT = 6379;
	private final int DEFAULT_CLUSTER_PORT = 7000;
	private Boolean withPipelining;

	@Override
	public Redis create(Params params) {
		Boolean useRedisCluster = params.get(RedisParams.CLUSTER_MODE);
		withPipelining = params.get(RedisParams.PIPELINE_SIZE) > 1;
		if (useRedisCluster) {
			return jedisClusterCreate(params);
		} else {
			return jedisCreate(params);
		}
	}

	public Redis jedisCreate(Params params) {
		String redisStandaloneIp;
		Integer redisStandalonePort;
		String redisIpPort = params.get(RedisParams.REDIS_IPS)[0];
		if (redisIpPort.contains(":")) {
			try {
				redisStandaloneIp = redisIpPort.split(":")[0];
				redisStandalonePort = Integer.parseInt(redisIpPort.split(":")[1]);
			} catch (NumberFormatException e) {
				throw new AkIllegalOperatorParameterException("illegal REDIS_IPS value, use 'ip:port' or ip alone");
			}
		} else {
			redisStandaloneIp = redisIpPort;
			redisStandalonePort = DEFAULT_STANDALONE_PORT;
		}
		final Jedis redis = new Jedis(redisStandaloneIp, redisStandalonePort);
		// if has database index and timeout secound
		Client client = redis.getClient();
		if (params.contains(RedisParams.DATABASE_INDEX)) {
			client.setDb(params.get(RedisParams.DATABASE_INDEX));
		}
		if (params.contains(RedisParams.TIMEOUT)) {
			client.setConnectionTimeout(params.get(RedisParams.TIMEOUT));
		}
		if (params.contains(RedisParams.REDIS_PASSWORD)) {
			redis.auth(params.get(RedisParams.REDIS_PASSWORD));
		}

		return new Redis() {
			int pipelineSize = params.get(RedisParams.PIPELINE_SIZE);
			List <byte[]> keyByteBuffer = new ArrayList <>(pipelineSize);
			List <byte[]> valueByteBuffer = new ArrayList <>(pipelineSize);
			List <String> keyStringBuffer = new ArrayList <>(pipelineSize);
			List <String> valueStringBuffer = new ArrayList <>(pipelineSize);

			@Override
			public void close() {
				if (withPipelining) {
					redis.close();
					return;
				}
				if (keyByteBuffer.size() > 0) {
					Pipeline pipeline = redis.pipelined();
					for (int i = 0; i < keyByteBuffer.size(); i++) {
						pipeline.set(keyByteBuffer.get(i), valueByteBuffer.get(i));
					}
					pipeline.sync();
					keyByteBuffer.clear();
					valueByteBuffer.clear();
				}
				if (keyStringBuffer.size() > 0) {
					Pipeline pipeline = redis.pipelined();
					for (int i = 0; i < keyByteBuffer.size(); i++) {
						pipeline.set(keyByteBuffer.get(i), valueByteBuffer.get(i));
					}
					pipeline.sync();
					keyByteBuffer.clear();
					valueByteBuffer.clear();
				}
				redis.close();
			}

			@Override
			public String ping() {
				return redis.ping();
			}

			@Override
			public String set(byte[] key, byte[] value) {
				if (withPipelining) {
					return redis.set(key, value);
				}
				if (keyByteBuffer.size() < pipelineSize) {
					keyByteBuffer.add(key);
					valueByteBuffer.add(value);
					return "add to buffer";
				} else {
					Pipeline pipeline = redis.pipelined();
					for (int i = 0; i < keyByteBuffer.size(); i++) {
						pipeline.set(keyByteBuffer.get(i), valueByteBuffer.get(i));
					}
					pipeline.sync();
					keyByteBuffer.clear();
					valueByteBuffer.clear();
					return "flush buffer";
				}
			}

			@Override
			public String set(final String key, final String value) {
				if (withPipelining) {
					return redis.set(key, value);
				}
				if (keyStringBuffer.size() < pipelineSize) {
					keyStringBuffer.add(key);
					valueStringBuffer.add(value);
					return "add to buffer";
				} else {
					Pipeline pipeline = redis.pipelined();
					for (int i = 0; i < keyStringBuffer.size(); i++) {
						pipeline.set(keyStringBuffer.get(i), valueStringBuffer.get(i));
					}
					pipeline.sync();
					keyStringBuffer.clear();
					valueStringBuffer.clear();
					return "flush buffer";
				}
			}

			@Override
			public byte[] get(byte[] key) {

				return redis.get(key);
			}

			@Override
			public String get(String key) {
				return redis.get(key);
			}

			@Override
			public List <byte[]> getKeys() {
				Set <String> keySet = redis.keys("*");
				List <byte[]> result = new ArrayList <>(keySet.size());
				for (String s : keySet) {
					result.add(s.getBytes());
				}
				return result;
			}
		};
	}

	public Redis jedisClusterCreate(Params params) {
		String[] redisClusterIpPorts = params.get(RedisParams.REDIS_IPS);
		Set <HostAndPort> nodes = new HashSet <>();
		for (String redisIpPort : redisClusterIpPorts) {
			if (redisIpPort.contains(":")) {
				String[] ipPort = redisIpPort.split(":");
				nodes.add(new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1])));
			} else {
				nodes.add(new HostAndPort(redisIpPort, DEFAULT_CLUSTER_PORT));
			}
		}

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_TOTAL);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT_MILLIS);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		final JedisCluster jedisCluster = new JedisCluster(nodes, TIME_OUT, TIME_OUT, MAX_ATTEMPTS,
			params.get(RedisParams.REDIS_PASSWORD), config);

		return new Redis() {
			@Override
			public void close() {
				try {
					jedisCluster.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String ping() {
				LOG.error("No way to dispatch ping command to Redis Cluster.");
				return "No way to dispatch ping command to Redis Cluster.";
			}

			@Override
			public String set(byte[] key, byte[] value) {
				return jedisCluster.set(key, value);
			}

			@Override
			public String set(final String key, final String value) {
				return jedisCluster.set(key, value);
			}

			@Override
			public byte[] get(byte[] key) {
				return jedisCluster.get(key);
			}

			@Override
			public String get(String key) {
				return jedisCluster.get(key);
			}

			@Override
			public List <byte[]> getKeys() {
				Set <String> keySet = jedisCluster.hkeys("*");
				List <byte[]> result = new ArrayList <>(keySet.size());
				for (String s : keySet) {
					result.add(s.getBytes());
				}
				return result;
			}
		};
	}
}
