package org.ject.momentia.api.notification.model;

import java.util.List;

import org.ject.momentia.common.domain.notification.type.NotificationType;

import jakarta.validation.constraints.NotEmpty;

public record UserNotificationSettingInfo(
	@NotEmpty List<NotificationSettingInfoUnit> settings
) {
	public record NotificationSettingInfoUnit(
		NotificationType type,
		boolean status
	) {

	}

}
