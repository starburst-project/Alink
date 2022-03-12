package com.alibaba.alink.server.service.identifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.alink.server.excpetion.IdGenerateException;
import com.alibaba.alink.server.service.api.identifier.IdentifierGeneratorService;
import com.alibaba.alink.server.service.api.identifier.MachineIdentifierService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * Snowflake.
 * <p>
 * https://segmentfault.com/a/1190000020899379
 */
@Service
public class IdentifierGeneratorServiceImpl implements IdentifierGeneratorService {

	private final static long START_TIMESTAMP = 1645547512L;

	private final static String START_TIMESTAMP_STRING = DateUtil.formatDateTime(new Date(START_TIMESTAMP));

	private final static int MACHINE_OFFSET = 22;

	private final static int INC_OFFSET = 9;

	private final static long MAXIMUM_MACHINE = ~(-1L << MACHINE_OFFSET);

	private final static long MAXIMUM_INC = ~(-1L << INC_OFFSET);

	private final static long TIMESTAMP_MASK = 0xFFFFFFFF;

	private final static long MACHINE_MASK = 0x3FFFFF;

	private final static long INC_MASK = 0x1FF;

	private final static int TIMESTAMP_LEFT = MACHINE_OFFSET + INC_OFFSET;

	private final Object lock = new Object();

	private long latest, machineId = -1, inc;

	@Autowired
	MachineIdentifierService machineIdentifierService;

	public IdentifierGeneratorServiceImpl() {
		synchronized (lock) {
			latest = System.currentTimeMillis() / 1000 - START_TIMESTAMP;

			if (latest <= 0) {
				throw new IdGenerateException(String.format(
					"Please aligning the system's timestamp. " +
						"it must be greater than %s",
					START_TIMESTAMP_STRING
				));
			}

		}
	}

	@Override
	public long nextId() {
		synchronized (lock) {
			if (machineId < 0) {
				machineId = nextMachineId();
			}

			long current = System.currentTimeMillis() / 1000 - START_TIMESTAMP;

			if (current < latest) {
				throw new IdGenerateException("System time should not be adjust when running the service");
			}

			if (current == latest) {
				if (inc == MAXIMUM_INC) {
					machineId = nextMachineId();
					inc = 0;
				} else {
					inc++;
				}
			} else {
				inc = 0;
			}

			latest = current;

			return current << TIMESTAMP_LEFT
				| machineId << INC_OFFSET
				| inc;
		}
	}

	private long nextMachineId() {
		return machineIdentifierService.nextId() % MAXIMUM_MACHINE;
	}

	@Override
	public String formatId(long id) {

		long timePart = (id >> TIMESTAMP_LEFT) & TIMESTAMP_MASK;
		long machinePart = (id >> INC_OFFSET) & MACHINE_MASK;
		long incPart = id & INC_MASK;

		return DatePattern.NORM_DATETIME_FORMAT.format(new Date(timePart)) + "-"
			+ machinePart + "-" + incPart;
	}
}
