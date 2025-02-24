package org.ject.momentia.api.notification.controller;

import java.util.List;

import org.ject.momentia.api.notification.model.NotificationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class NotificationController {
	@GetMapping("/notifications")
	@ResponseStatus(HttpStatus.OK)
	public List<NotificationInfo> getNotifications() {

	}

	@PatchMapping("/notification/read")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void readNotification() {

	}

	@GetMapping("/notification/setting")
	@ResponseStatus(HttpStatus.OK)
	public void getNotificationSetting() {

	}

	@PatchMapping("/notification/setting")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateNotificationSetting() {

	}
}
