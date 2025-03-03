package org.ject.momentia.api.notification.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.ject.momentia.api.notification.model.NotificationInfo;
import org.ject.momentia.common.domain.notification.Notification;
import org.ject.momentia.common.domain.notification.type.NotificationBaseType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConverter {
	public static NotificationInfo convertToNotificationInfo(
		Notification notification, String image
	) {
		var targetPost = notification.getTargetPost();
		var targetUser = notification.getTargetUser();
		var type = notification.getType();

		var builder = NotificationInfo.builder()
			.notificationId(notification.getId())
			.type(notification.getType())
			.isRead(notification.isRead())
			.targetImage(image)
			.createdAt(LocalDateTime.ofInstant(notification.getCreatedAt(), ZoneId.of("Asia/Seoul")));

		if (type.getNotificationBaseType() == NotificationBaseType.PICKED_ARTWORK
			|| type.getNotificationBaseType() == NotificationBaseType.ACTION_ON_ARTWORK) {
			builder.targetPostId(targetPost.getId())
				.targetPostTitle(targetPost.getTitle());
		}

		if (type.getNotificationBaseType() == NotificationBaseType.FOLLOW
			|| type.getNotificationBaseType() == NotificationBaseType.ACTION_ON_ARTWORK) {
			builder.targetUserId(targetUser.getId())
				.targetUserNickname(targetUser.getNickname());
		}

		return builder.build();
	}
}
