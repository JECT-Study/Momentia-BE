package org.ject.momentia.api.artwork.controller;

import org.ject.momentia.api.artwork.service.ArtworkLikeService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkLikeController {

	private final ArtworkLikeService artworkLikeService;

	/**
	 * [POST] 작품 좋아요
	 */
	@PostMapping("/post/{postId}/like")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void like(@PathVariable Long postId, @MomentiaUser User user) {
		artworkLikeService.like(user, postId);
	}

	/**
	 * [DELETE] 작품 좋아요 취소
	 */
	@DeleteMapping("/post/{postId}/like")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unlike(@PathVariable Long postId, @MomentiaUser User user) {
		artworkLikeService.unlike(user, postId);
	}

}
