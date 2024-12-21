package org.ject.momentia.common.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(
	name = "user",
	schema = "momentia",
	indexes = {
		@Index(name = "user_index_01", columnList = "account_status, id")
	}
)
public class User {
	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Size(max = 50)
	@NotNull
	@Column(name = "nickname", nullable = false, length = 50)
	private String nickname;

	@Column(name = "profile_image")
	private Long profileImage;

	@Size(max = 50)
	@Column(name = "introduction", length = 50)
	private String introduction;

	@NotNull
	@Column(name = "follower_count", nullable = false)
	private Integer followerCount;

	@NotNull
	@Column(name = "following_count", nullable = false)
	private Integer followingCount;

	@Size(max = 10)
	@NotNull
	@Column(name = "account_type", nullable = false, length = 10)
	private String accountType;

	@Size(max = 10)
	@NotNull
	@Column(name = "account_status", nullable = false, length = 10)
	private String accountStatus;

	@Size(max = 320)
	@Column(name = "email", length = 320)
	private String email;

}