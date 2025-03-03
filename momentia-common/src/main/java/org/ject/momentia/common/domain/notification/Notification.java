package org.ject.momentia.common.domain.notification;

import java.time.Instant;

import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.notification.type.NotificationType;
import org.ject.momentia.common.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "notification", schema = "momentia")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@JoinColumn(name = "target_post_id")
	@ManyToOne
	private ArtworkPost targetPost;

	@NotNull
	@Column(name = "read_status", nullable = false, length = 15)
	private boolean isRead;

	@JoinColumn(name = "target_user_id")
	@ManyToOne
	private User targetUser;

	@NotNull
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@NotNull
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

}