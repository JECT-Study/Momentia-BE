package org.ject.momentia.api.notification.service;

import java.time.Instant;

import org.ject.momentia.api.notification.repository.NotificationRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.notification.Notification;
import org.ject.momentia.common.domain.notification.type.NotificationType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPublishService {
	private final NotificationRepository notificationRepository;
	private final NotificationService notificationService;

	@Async
	public void publish(NotificationType notificationType, User publishUser, User receiveUser,
		ArtworkPost artworkPost) {
		var notificationSetting = notificationService.findOrCreateNotificationSetting(receiveUser);
		if (Boolean.FALSE.equals(notificationType.getOptionGetter().apply(notificationSetting))) {
			return;
		}

		var nowInstant = Instant.now();
		var notification = Notification.builder()
			.type(notificationType)
			.user(receiveUser)
			.targetPost(artworkPost)
			.isRead(false)
			.targetUser(publishUser)
			.createdAt(nowInstant)
			.updatedAt(nowInstant)
			.build();

		notificationRepository.save(notification);
	}
}
