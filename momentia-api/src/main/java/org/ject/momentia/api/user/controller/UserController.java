package org.ject.momentia.api.user.controller;

import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.user.model.UserInfo;
import org.ject.momentia.api.user.model.UserUpdateRequest;
import org.ject.momentia.api.user.service.UserService;
import org.ject.momentia.common.domain.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
	private final UserService userService;

	@GetMapping
	public UserInfo getMyInfo(@MomentiaUser User user, @RequestParam(required = false) @Positive Long userId) {
		return userService.getUserInfo(user, userId);
	}

	@PatchMapping
	public UserInfo updateInfo(@MomentiaUser User user, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return userService.updateInfo(user, userUpdateRequest);
	}
}
