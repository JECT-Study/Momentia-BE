package org.ject.momentia.api.user.service;

import org.apache.commons.lang3.ObjectUtils;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.service.module.FollowModuleService;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.user.converter.UserConverter;
import org.ject.momentia.api.user.model.UserInfo;
import org.ject.momentia.api.user.model.UserUpdateRequest;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.image.Image;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
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
		// userId가 null인 경우, 본인 프로필
		if (userId == null) {
			if (user == null)
				throw ErrorCd.NO_PERMISSION.serviceException();
			return UserConverter.toUserInfo(user, true, false);
		}

		var targetUser = accountService.findByIdElseThrowException(userId);
		var isFollow = followModuleService.isFollowing(user, targetUser);
		return UserConverter.toUserInfo(targetUser, user != null && user.isMine(userId), isFollow);
	}

	@Transactional
	public UserInfo updateInfo(User user, UserUpdateRequest userUpdateRequest) {
		validateUserUpdateRequestParam(userUpdateRequest);
		Image image = null;
		var updateImageId = userUpdateRequest.profileImage();

		if (updateImageId != null) {
			imageService.validateTempImage(updateImageId, true);
			image = imageService.useImageToService(updateImageId, ImageTargetType.PROFILE, user.getId());
		}

		user.update(userUpdateRequest.field(), userUpdateRequest.nickname(),
			userUpdateRequest.introduction(), image);
		return UserConverter.toUserInfo(userRepository.save(user), true, true);
	}

	private void validateUserUpdateRequestParam(UserUpdateRequest userUpdateRequest) {
		if (ObjectUtils.allNull(userUpdateRequest.field(), userUpdateRequest.introduction(),
			userUpdateRequest.nickname(), userUpdateRequest.profileImage())) {
			throw ErrorCd.INVALID_PARAMETER.serviceException();
		}
	}
}
