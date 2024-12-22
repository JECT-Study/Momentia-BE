package org.ject.momentia.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
	public static final String NICKNAME_FORMAT = "^[가-힣a-zA-Z0-9_-]{2,20}$";
}
