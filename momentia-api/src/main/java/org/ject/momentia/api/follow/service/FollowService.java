package org.ject.momentia.api.follow.service;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.converter.FollowConverter;
import org.ject.momentia.api.follow.model.FollowResponse;
import org.ject.momentia.api.follow.model.FollowUserRequest;
import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.api.follow.service.module.FollowModuleService;
import org.ject.momentia.api.notification.service.NotificationPublishService;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.api.user.service.AccountService;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.notification.type.NotificationType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {
	private final FollowRepository followRepository;
	private final UserRepository userRepository;
	private final AccountService accountService;

	private final FollowModuleService followModule;
	private final NotificationPublishService notificationPublishService;

	@Transactional
	public void follow(User user, FollowUserRequest followingId) {
		User followingUser = accountService.findByIdElseThrowException(followingId.userId());
		FollowId id = FollowConverter.toFollowId(user, followingUser);
		if (followModule.isFollowing(user, followingUser, false))
			throw ErrorCd.ALREADY_FOLLOW.serviceException();
		followRepository.save(FollowConverter.toFollow(id));

		// followCount 직접 세는 로직으로 변경 의논
		followingUser.increaseFollowerCount();
		user.increaseFollowingCount(); // Detached 상태이므로 save 필요
		userRepository.save(user);

		notificationPublishService.publish(NotificationType.FOLLOWED_BY_USER, user, followingUser, null);
	}

	@Transactional
	public void unfollow(User user, FollowUserRequest followingId) {
		User followingUser = accountService.findByIdElseThrowException(followingId.userId());
		FollowId id = FollowConverter.toFollowId(user, followingUser);
		if (!followModule.isFollowing(user, followingUser, false))
			throw ErrorCd.FOLLOW_NOT_FOUND.serviceException();
		followRepository.deleteById(id);

		// followCount 직접 세는 로직으로 변경 의논
		followingUser.decreaseFollowerCount();
		user.decreaseFollowingCount();
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public FollowResponse getSocialRelationInfo(Long userId, boolean isFollowing, User selfUser) {
		var user = accountService.findByIdElseThrowException(userId);
		return new FollowResponse(isFollowing ? followRepository.findFollowingUsersInfo(user, selfUser) :
			followRepository.findFollowerUsersInfo(user, selfUser));
	}
}
