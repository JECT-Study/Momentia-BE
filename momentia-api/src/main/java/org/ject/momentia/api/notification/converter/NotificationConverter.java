package org.ject.momentia.api.notification.converter;

import org.ject.momentia.api.notification.entity.Notification;
import org.ject.momentia.api.notification.model.NotificationInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConverter {
	public static NotificationInfo convertToNotificationInfo(
		Notification notification
	) {
		return NotificationInfo.builder()
			.notificationId(notification.getId())
			.type(notification.getType())
			.targetPostId(notification.getTargetPostId())
			.targetPostTitle(null)
			.targetUserId(notification.getTargetUser().getId())
			.targetUserNickname(notification.getTargetUser().getNickname())
			.isRead(notification.isRead())
			.createdAt(notification.getCreatedAt())

	}
}
