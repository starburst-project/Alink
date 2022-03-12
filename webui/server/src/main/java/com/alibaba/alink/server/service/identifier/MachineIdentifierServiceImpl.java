package com.alibaba.alink.server.service.identifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.alink.server.domain.MachineInfo;
import com.alibaba.alink.server.mapper.MachineInfoMapper;
import com.alibaba.alink.server.service.api.identifier.MachineIdentifierService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

@Service
public class MachineIdentifierServiceImpl implements MachineIdentifierService {

	private static final String ip;

	static {
		String localIp;
		try {
			localIp = getLocalIp();
		} catch (SocketException e) {
			localIp = "";
		}

		ip = localIp;
	}

	@Autowired
	MachineInfoMapper machineInfoMapper;

	// @Autowired
	// Environment environment;

	@Override
	public long nextId() {
		MachineInfo machineInfo = new MachineInfo();

		// machineInfo.setIp(ip + ":" + environment.getProperty("local.server.port"));
		machineInfo.setIp(ip);
		machineInfo.setGmtCreate(new Date());

		machineInfoMapper.insert(machineInfo);

		return machineInfo.getId();
	}

	@Override
	public String format(int identifier) {
		return String.valueOf(identifier);
	}

	private static String getLocalIp() throws SocketException {
		StringBuilder stringBuilder = new StringBuilder();

		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		int j = 0;
		while (interfaces.hasMoreElements()) {
			NetworkInterface iFace = interfaces.nextElement();

			if (iFace.isLoopback() || !iFace.isUp()) {
				continue;
			}

			Enumeration<InetAddress> addresses = iFace.getInetAddresses();
			if (j != 0) {
				stringBuilder.append("#");
			}

			int i = 0;
			while (addresses.hasMoreElements()) {
				if (i++ != 0) {
					stringBuilder.append(";");
				}
				stringBuilder.append(addresses.nextElement().getHostAddress());
			}
			j++;
		}

		return stringBuilder.toString();
	}
}
