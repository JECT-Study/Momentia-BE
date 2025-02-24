package org.ject.momentia.api.notification.converter;

import static org.ject.momentia.api.notification.model.UserNotificationSettingInfo.*;

import java.util.Arrays;

import org.ject.momentia.api.notification.entity.NotificationSetting;
import org.ject.momentia.api.notification.model.UserNotificationSettingInfo;
import org.ject.momentia.api.notification.type.NotificationType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationSettingConverter {
	public static UserNotificationSettingInfo convertToUserNotificationSettingInfo(
		NotificationSetting notificationSetting
	) {
		var settings = Arrays.stream(NotificationType.values())
			.map(type -> new NotificationSettingInfoUnit(type, type.getOptionGetter().apply(notificationSetting)))
			.toList();

		return new UserNotificationSettingInfo(settings);
	}
}
