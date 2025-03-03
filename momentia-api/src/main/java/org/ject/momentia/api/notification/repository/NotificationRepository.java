package org.ject.momentia.api.notification.repository;

import java.util.List;

import org.ject.momentia.common.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserId(Long userId);

	@Query("""
		SELECT n
		FROM Notification n
		WHERE n.user.id = :userId
		ORDER BY n.createdAt DESC
		LIMIT :limit
		""")
	List<Notification> findByUserId(Long userId, int limit);

	@Query("""
		SELECT n
		FROM Notification n
		WHERE n.user.id = :userId
		AND n.id < :lastNotificationId
		ORDER BY n.createdAt DESC
		LIMIT :limit
		""")
	List<Notification> findByUserIdAndLastNotificationId(Long userId, Long lastNotificationId, int limit);
}
