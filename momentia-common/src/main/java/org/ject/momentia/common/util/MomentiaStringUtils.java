package org.ject.momentia.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 문자열 관련 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MomentiaStringUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(MomentiaStringUtils.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 문자열 포맷팅을 로그와 동일한 방식으로 수행한다.
	 * @param formatString
	 * @param args
	 * @return
	 */
	public static String format(String formatString, Object... args) {
		return MessageFormatter.arrayFormat(formatString, args).getMessage();
	}

	/**
	 * 객체를 readable 한 문자열로 변환한다.
	 * @param object
	 * @return
	 */
	public static String reflectionToString(Object object) {
		if (object != null) {
			try {
				return OBJECT_MAPPER.writeValueAsString(object);
			} catch (JsonProcessingException var2) {
				JsonProcessingException e = var2;
				LOGGER.warn("ReflectionToString json processing error", e);
			}
		}

		return StringUtils.EMPTY;
	}
}
