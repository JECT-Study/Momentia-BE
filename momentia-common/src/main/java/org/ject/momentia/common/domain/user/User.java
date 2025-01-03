package org.ject.momentia.common.domain.user;

import org.ject.momentia.common.domain.user.type.AccountStatus;
import org.ject.momentia.common.domain.user.type.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ject.momentia.common.domain.user.type.FieldType;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false, length = 10)
	private AccountType accountType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false, length = 10)
	private AccountStatus accountStatus;

	@Size(max = 320)
	@Column(name = "email", length = 320)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "field", nullable = true, length = 20)
	private FieldType field;

	public void increaseFollowerCount() {
		this.followerCount++;
	}
	public void increaseFollowingCount() {
		this.followingCount++;
	}
	public void decreaseFollowerCount() {
		this.followerCount--;
	}
	public void decreaseFollowingCount() {
		this.followingCount--;
	}

	public boolean matchAccountType(AccountType accountType) {
		return this.accountType == accountType;
	}
}