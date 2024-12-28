package org.ject.momentia.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
	public static final String NICKNAME_FORMAT = "^[가-힣a-zA-Z0-9_-]{2,20}$";
	public static final String PASSWORD_FORMAT = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{}\\[\\]:;\"',.<>?\\/]).{8,20}$";
}
