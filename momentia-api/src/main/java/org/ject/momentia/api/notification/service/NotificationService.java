package org.ject.momentia.api.notification.service;

import java.util.List;
import java.util.Objects;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.notification.converter.NotificationConverter;
import org.ject.momentia.api.notification.converter.NotificationSettingConverter;
import org.ject.momentia.api.notification.model.NotificationInfo;
import org.ject.momentia.api.notification.model.UserNotificationSettingInfo;
import org.ject.momentia.api.notification.repository.NotificationRepository;
import org.ject.momentia.api.notification.repository.NotificationSettingRepository;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.common.domain.notification.Notification;
import org.ject.momentia.common.domain.notification.NotificationSetting;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private static final int NOTIFICATION_LIMIT = 20;

	private final ImageService imageService;
	private final NotificationRepository notificationRepository;
	private final NotificationSettingRepository notificationSettingRepository;

	@Transactional(readOnly = true)
	public List<NotificationInfo> getNotification(User user, Long lastNotificationId) {
		var notis = lastNotificationId == null ?
			notificationRepository.findByUserId(user.getId(), NOTIFICATION_LIMIT) :
			notificationRepository.findByUserIdAndLastNotificationId(user.getId(), lastNotificationId,
				NOTIFICATION_LIMIT);

		return notis.stream()
			.map(this::convertToNotificationInfo)
			.toList();
	}

	@Transactional
	public void readNotification(User user, Long notificationId) {
		var notification = getValidatedNotification(user, notificationId);
		notification.setRead(true);
		notificationRepository.save(notification);
	}

	@Transactional(readOnly = true)
	public UserNotificationSettingInfo getNotificationSetting(User user) {
		var notificationSetting = findOrCreateNotificationSetting(user);
		return NotificationSettingConverter.convertToUserNotificationSettingInfo(notificationSetting);
	}

	@Transactional
	public void updateNotificationSetting(User user,
		List<UserNotificationSettingInfo.NotificationSettingInfoUnit> settings) {
		var notificationConfig = findOrCreateNotificationSetting(user);

		for (var setting : settings) {
			notificationConfig.updateSetting(setting.type(), setting.status());
		}

		notificationSettingRepository.save(notificationConfig);
	}

	public Notification getValidatedNotification(User user, Long notificationId) {
		var notification = notificationRepository.findById(notificationId)
			.orElseThrow(ErrorCd.IMAGE_NOT_UPLOADED::serviceException);

		if (!Objects.equals(notification.getUser().getId(), user.getId())) {
			throw ErrorCd.NOT_OWNER.serviceException();
		}

		return notification;
	}

	/**
	 * 설정 값 조회 시, 기존 기능 개발 전의 유저는 설정이 존재하지 않음
	 * 이에 따라, 설정이 없을 경우 새로운 설정을 생성하여 반환
	 * @param user
	 * @return
	 */
	@Transactional
	public NotificationSetting findOrCreateNotificationSetting(User user) {
		return notificationSettingRepository.findById(user.getId())
			.orElseGet(() -> notificationSettingRepository.save(NotificationSetting.createSetting(user.getId())));
	}

	public NotificationInfo convertToNotificationInfo(Notification notification) {
		var image = switch (notification.getType().getNotificationBaseType()) {
			case ACTION_ON_ARTWORK, PICKED_ARTWORK -> imageService.getImageUrl(
				ImageTargetType.ARTWORK, notification.getTargetPost().getId());
			case FOLLOW -> imageService.getImageUrl(
				ImageTargetType.PROFILE, notification.getTargetUser().getId());
			case PICKED_USER -> imageService.getImageUrl(
				ImageTargetType.PROFILE, notification.getUser().getId());
			case UNUSED -> null;
		};
		return NotificationConverter.convertToNotificationInfo(notification, image);
	}
}
