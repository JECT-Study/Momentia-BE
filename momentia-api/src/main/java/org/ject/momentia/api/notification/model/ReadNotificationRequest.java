package org.ject.momentia.api.notification.model;

import jakarta.validation.constraints.Positive;

public record ReadNotificationRequest(
	@Positive long notificationId
) {
}
