package org.ject.momentia.common.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "user",
	schema = "momentia"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {
	@Id
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Size(max = 50)
	@NotNull
	@Column(name = "social_id", nullable = false, length = 50)
	private String socialId;

	@Size(max = 10)
	@NotNull
	@Column(name = "social_type", nullable = false, length = 10)
	private String socialType;

}