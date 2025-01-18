package org.ject.momentia.api.follow.controller;

import java.util.List;

import org.ject.momentia.api.follow.model.FollowInfo;
import org.ject.momentia.api.follow.model.FollowUserRequest;
import org.ject.momentia.api.follow.service.FollowService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class FollowController {

	private final FollowService followService;

	@PostMapping("/follow")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void follow(@RequestBody FollowUserRequest request, @MomentiaUser User user) {
		followService.follow(user, request);
	}

	@DeleteMapping("/follow")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unfollow(@RequestBody FollowUserRequest request, @MomentiaUser User user) {
		followService.unfollow(user, request);
	}

	@GetMapping("/{userId}/followingList")
	@ResponseStatus(HttpStatus.OK)
	public List<FollowInfo> getUserFollowingList(@PathVariable Long userId) {
		return followService.getSocialRelationInfo(userId, true);
	}

	@GetMapping("/{userId}/followerList")
	@ResponseStatus(HttpStatus.OK)
	public List<FollowInfo> getUserFollwerList(@PathVariable Long userId) {
		return followService.getSocialRelationInfo(userId, false);
	}
}
