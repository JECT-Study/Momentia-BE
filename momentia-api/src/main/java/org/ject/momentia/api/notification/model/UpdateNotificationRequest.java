package org.ject.momentia.api.notification.model;

import org.ject.momentia.api.notification.type.NotificationType;

public record UpdateNotificationRequest(
	NotificationType type,
	boolean status
) {
}
