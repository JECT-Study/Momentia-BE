package org.ject.momentia.common.domain.user;

import org.ject.momentia.common.domain.user.type.OAuthProvider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
	name = "social_account",
	schema = "momentia"
)
public class SocialAccount {
	@Id
	@Column(name = "user_id", nullable = false)
	private Long id;

	@MapsId(value = "id")
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private User user;

	@Size(max = 50)
	@NotNull
	@Column(name = "social_id", nullable = false, length = 50)
	private String socialId;

	@NotNull
	@Column(name = "social_type", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private OAuthProvider socialType;

}