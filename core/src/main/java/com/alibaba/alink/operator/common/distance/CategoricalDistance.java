package com.alibaba.alink.operator.common.distance;

import com.alibaba.alink.common.exceptions.AkIllegalDataException;

import java.io.Serializable;

/**
 * Base class for calculating categorical distance.
 *
 * It supports calculating distances between two strings(the two strings are compared character by character),
 * or two string arrays(the two arrays are compared string by string).
 */
public interface CategoricalDistance extends Serializable {
	/**
	 * Calculate the distance between two strings, the distance is compared by character.
	 *
	 * @param str1 string1
	 * @param str2 string2
	 * @return the distance
	 */
	int calc(String str1, String str2);

	/**
	 * Calculate the distance between two strings, the distance is compared by string.
	 *
	 * @param str1 string1
	 * @param str2 string2
	 * @return the distance
	 */
	int calc(String[] str1, String[] str2);

	default <M> int calc(M left, M right) {
		if (left instanceof String && right instanceof String) {
			return calc((String) left, (String) right);
		} else if (left instanceof String[] && right instanceof String[]) {
			return calc((String[]) left, (String[]) right);
		} else {
			throw new AkIllegalDataException(
				"only support calculation between string and string or string array and string array!");
		}
	}
}
