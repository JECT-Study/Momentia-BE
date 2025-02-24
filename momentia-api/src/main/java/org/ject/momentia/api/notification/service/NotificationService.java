package org.ject.momentia.api.notification.service;

import java.util.List;

import org.ject.momentia.api.notification.converter.NotificationSettingConverter;
import org.ject.momentia.api.notification.entity.NotificationSetting;
import org.ject.momentia.api.notification.model.NotificationInfo;
import org.ject.momentia.api.notification.model.UserNotificationSettingInfo;
import org.ject.momentia.api.notification.repository.NotificationRepository;
import org.ject.momentia.api.notification.repository.NotificationSettingRepository;
import org.ject.momentia.api.notification.type.NotificationType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final NotificationSettingRepository notificationSettingRepository;

	public List<NotificationInfo> getNotification(User user) {
		var notis = notificationRepository.findByUserId(user.getId());
		notis.stream()

	}

	public void readNotification() {

	}

	public UserNotificationSettingInfo getNotificationSetting(User user) {
		var notificationSetting = findOrCreateNotificationSetting(user);
		return NotificationSettingConverter.convertToUserNotificationSettingInfo(notificationSetting);
	}

	@Transactional
	public void updateNotificationSetting(User user, NotificationType type, boolean status) {
		var notificationConfig = findOrCreateNotificationSetting(user);

		notificationConfig.updateSetting(type, status);
		notificationSettingRepository.save(notificationConfig);
	}

	/**
	 * 설정 값 조회 시, 기존 기능 개발 전의 유저는 설정이 존재하지 않음
	 * 이에 따라, 설정이 없을 경우 새로운 설정을 생성하여 반환
	 * @param user
	 * @return
	 */
	public NotificationSetting findOrCreateNotificationSetting(User user) {
		return notificationSettingRepository.findById(user.getId())
			.orElseGet(() -> NotificationSetting.createSetting(user.getId()));
	}
}
