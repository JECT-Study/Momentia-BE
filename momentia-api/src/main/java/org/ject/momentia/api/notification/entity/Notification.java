package org.ject.momentia.api.notification.entity;

import java.time.Instant;

import org.ject.momentia.api.notification.type.NotificationType;
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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "momentia")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Size(max = 30)
	@NotNull
	@Column(name = "type", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Column(name = "target_post_id")
	private Long targetPostId;

	@Size(max = 15)
	@NotNull
	@Column(name = "read_status", nullable = false, length = 15)
	private String readStatus;

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