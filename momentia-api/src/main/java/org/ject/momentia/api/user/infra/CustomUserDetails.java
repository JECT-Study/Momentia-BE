package org.ject.momentia.api.user.infra;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomUserDetails extends User {
	private final Long id;
	private final String name;
	private final String profileImage;

	public CustomUserDetails(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities,
		String name, String profileImage) {
		super(String.valueOf(id), password != null ? password : "", true, true, true, true,
			authorities != null ? authorities : Collections.emptyList());
		this.id = id;
		this.name = name;
		this.profileImage = profileImage;
	}

	public static CustomUserDetails from(org.ject.momentia.common.domain.user.User user) {
		return new CustomUserDetails(
			user.getId(),
			user.getEmail(),
			null,
			null,
			user.getNickname(),
			null
		);
	}
}
