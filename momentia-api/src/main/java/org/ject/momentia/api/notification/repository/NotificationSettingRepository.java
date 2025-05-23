package org.ject.momentia.api.notification.repository;

import org.ject.momentia.common.domain.notification.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}
