package org.ject.momentia.api.user.service;

import org.apache.commons.lang3.ObjectUtils;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.service.module.FollowModuleService;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.user.converter.UserConverter;
import org.ject.momentia.api.user.model.UserInfo;
import org.ject.momentia.api.user.model.UserUpdateRequest;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final AccountService accountService;
	private final FollowModuleService followModuleService;
	private final UserRepository userRepository;
	private final ImageService imageService;

	/**
	 * 사용자 정보를 가져온다.
	 * @param user
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(User user, Long userId) {
		if (userId == null || user.isMine(userId)) {
			return UserConverter.toUserInfo(user, true, true);
		}

		var targetUser = accountService.findByIdElseThrowException(userId);
		var isFollow = followModuleService.isFollowing(user, targetUser);
		return UserConverter.toUserInfo(targetUser, false, isFollow);
	}

	@Transactional
	public UserInfo updateInfo(User user, UserUpdateRequest userUpdateRequest) {
		validateUserUpdateRequestParam(userUpdateRequest);
		var updateImageId = userUpdateRequest.profileImage();
		var updateImage = updateImageId == null ? null : imageService.validateActiveImage(updateImageId);
		user.update(userUpdateRequest.field(), userUpdateRequest.nickname(),
			userUpdateRequest.introduction(), updateImage);
		return UserConverter.toUserInfo(userRepository.save(user), true, true);
	}

	private void validateUserUpdateRequestParam(UserUpdateRequest userUpdateRequest) {
		if (ObjectUtils.allNull(userUpdateRequest.field(), userUpdateRequest.introduction(),
			userUpdateRequest.nickname(), userUpdateRequest.profileImage())) {
			throw ErrorCd.INVALID_PARAMETER.serviceException();
		}
	}
}
