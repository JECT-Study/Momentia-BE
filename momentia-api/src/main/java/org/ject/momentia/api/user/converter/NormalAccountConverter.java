package org.ject.momentia.api.user.converter;

import org.ject.momentia.common.domain.user.NormalAccount;
import org.ject.momentia.common.domain.user.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NormalAccountConverter {
	public static NormalAccount userOf(User user, String encodedPassword) {
		return new NormalAccount(null, user, encodedPassword);
	}
}
