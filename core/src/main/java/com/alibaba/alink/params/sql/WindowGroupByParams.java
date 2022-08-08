package com.alibaba.alink.params.sql;

import org.apache.flink.ml.api.misc.param.ParamInfo;
import org.apache.flink.ml.api.misc.param.ParamInfoFactory;
import org.apache.flink.ml.api.misc.param.WithParams;

import com.alibaba.alink.common.annotation.DescCn;
import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.params.ParamUtil;

import java.io.Serializable;

public interface WindowGroupByParams<T> extends WithParams <T> {

	@NameCn("select语句")
	@DescCn("select语句")
	ParamInfo <String> SELECT_CLAUSE = ParamInfoFactory
		.createParamInfo("selectClause", String.class)
		.setDescription("Select clause")
		.setRequired()
		.build();
	@NameCn("groupby语句")
	@DescCn("groupby语句")
	ParamInfo <String> GROUP_BY_CLAUSE = ParamInfoFactory
		.createParamInfo("groupByClause", String.class)
		.setDescription("Groupby clause")
		.setHasDefaultValue(null)
		.build();
	@NameCn("时间长度单位")
	@DescCn("时间长度单位")
	ParamInfo <IntervalUnit> INTERVAL_UNIT = ParamInfoFactory
		.createParamInfo("intervalUnit", IntervalUnit.class)
		.setDescription("Interval unit, one of second, minute, hour, day or month")
		.setHasDefaultValue(IntervalUnit.SECOND)
		.build();

	default IntervalUnit getIntervalUnit() {
		return get(INTERVAL_UNIT);
	}

	default T setIntervalUnit(IntervalUnit value) {
		return set(INTERVAL_UNIT, value);
	}

	default T setIntervalUnit(String value) {
		return set(INTERVAL_UNIT, ParamUtil.searchEnum(INTERVAL_UNIT, value));
	}

	enum IntervalUnit implements Serializable {
		SECOND,
		MINUTE,
		HOUR,
		DAY,
		MONTH
	}

	@NameCn("窗口类型")
	@DescCn("窗口类型")
	ParamInfo <WindowType> WINDOW_TYPE = ParamInfoFactory
		.createParamInfo("windowType", WindowType.class)
		.setDescription("Window type, one of \"tumble\", \"hop\", \"session\"")
		.setHasDefaultValue(WindowType.TUMBLE)
		.build();

	default WindowType getWindowType() {
		return get(WINDOW_TYPE);
	}

	default T setWindowType(WindowType value) {
		return set(WINDOW_TYPE, value);
	}

	default T setWindowType(String value) {
		return set(WINDOW_TYPE, ParamUtil.searchEnum(WINDOW_TYPE, value));
	}

	enum WindowType implements Serializable {
		TUMBLE,
		HOP,
		SESSION
	}

	@NameCn("session间隔长度")
	@DescCn("session间隔长度")
	ParamInfo <Integer> SESSION_GAP = ParamInfoFactory
		.createParamInfo("sessionGap", Integer.class)
		.setDescription("Session gap")
		.setRequired()
		.build();
	@NameCn("滑动窗口滑动长度")
	@DescCn("滑动窗口滑动长度")
	ParamInfo <Integer> SLIDING_LENGTH = ParamInfoFactory
		.createParamInfo("slidingLength", Integer.class)
		.setDescription("Sliding length")
		.setRequired()
		.build();
	@NameCn("窗口长度")
	@DescCn("窗口长度")
	ParamInfo <Integer> WINDOW_LENGTH = ParamInfoFactory
		.createParamInfo("windowLength", Integer.class)
		.setDescription("Window length")
		.setRequired()
		.build();

	default String getSelectClause() {
		return get(SELECT_CLAUSE);
	}

	default T setSelectClause(String value) {
		return set(SELECT_CLAUSE, value);
	}

	default String getGroupByClause() {
		return get(GROUP_BY_CLAUSE);
	}

	default T setGroupByClause(String value) {
		return set(GROUP_BY_CLAUSE, value);
	}

	default Integer getSessionGap() {
		return get(SESSION_GAP);
	}

	default T setSessionGap(Integer value) {
		return set(SESSION_GAP, value);
	}

	default Integer getSlidingLength() {
		return get(SLIDING_LENGTH);
	}

	default T setSlidingLength(Integer value) {
		return set(SLIDING_LENGTH, value);
	}

	default Integer getWindowLength() {
		return get(WINDOW_LENGTH);
	}

	default T setWindowLength(Integer value) {
		return set(WINDOW_LENGTH, value);
	}
}
