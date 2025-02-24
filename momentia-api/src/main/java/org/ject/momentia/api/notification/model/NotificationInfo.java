package org.ject.momentia.api.notification.model;

import java.time.LocalDateTime;

import org.ject.momentia.api.notification.type.NotificationType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record NotificationInfo(
	@Positive long notificationId,
	@NotNull NotificationType type,
	@Positive long targetPostId,
	@NotEmpty String targetPostTitle,
	String targetImage,
	@Positive long targetUserId,
	String targetUserNickname,
	boolean isRead,
	@NotNull LocalDateTime createdAt
) {

}
