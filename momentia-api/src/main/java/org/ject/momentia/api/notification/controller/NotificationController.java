package org.ject.momentia.api.notification.controller;

import java.util.List;

import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.notification.model.NotificationInfo;
import org.ject.momentia.api.notification.model.ReadNotificationRequest;
import org.ject.momentia.api.notification.model.UserNotificationSettingInfo;
import org.ject.momentia.api.notification.service.NotificationService;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping("/notifications")
	@ResponseStatus(HttpStatus.OK)
	public List<NotificationInfo> getNotifications(
		@MomentiaUser User user,
		@RequestParam(required = false) Long lastNotificationId) {
		return notificationService.getNotification(user, lastNotificationId);
	}

	@PatchMapping("/notification/read")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void readNotification(
		@MomentiaUser User user,
		@RequestBody ReadNotificationRequest readNotificationRequest
	) {
		notificationService.readNotification(user, readNotificationRequest.notificationId());
	}

	@GetMapping("/notification/setting")
	@ResponseStatus(HttpStatus.OK)
	public UserNotificationSettingInfo getNotificationSetting(
		@MomentiaUser User user
	) {
		return notificationService.getNotificationSetting(user);
	}

	@PatchMapping("/notification/setting")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateNotificationSetting(
		@MomentiaUser User user,
		@RequestBody UserNotificationSettingInfo userNotificationSettingInfo
	) {
		notificationService.updateNotificationSetting(user, userNotificationSettingInfo.settings());
	}
}
