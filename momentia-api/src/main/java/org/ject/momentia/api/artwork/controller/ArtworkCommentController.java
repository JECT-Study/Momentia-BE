package org.ject.momentia.api.artwork.controller;

import org.ject.momentia.api.artwork.model.dto.ArtworkCommentIdResponse;
import org.ject.momentia.api.artwork.model.dto.ArtworkCommentListResponse;
import org.ject.momentia.api.artwork.model.dto.ArtworkCommentRequest;
import org.ject.momentia.api.artwork.service.ArtworkCommentService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkCommentController {

	private final ArtworkCommentService artworkCommentService;

	/**
	 * [POST] 댓글 작성
	 */
	@PostMapping("/post/{postId}/comment")
	@ResponseStatus(HttpStatus.CREATED)
	public ArtworkCommentIdResponse createComment(@RequestBody @Valid ArtworkCommentRequest artworkCommentCreateRequest,
		@PathVariable Long postId, @MomentiaUser User user) {
		return artworkCommentService.createComment(user, postId, artworkCommentCreateRequest);
	}

	/**
	 * [DELETE] 댓글 삭제
	 */
	@DeleteMapping("/comment/{commentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteComment(@PathVariable Long commentId, @MomentiaUser User user) {
		artworkCommentService.deleteComment(user, commentId);
	}

	/**
	 * [GET] 댓글 리스트 가져오기
	 */
	@GetMapping("/post/{postId}/comments")
	@ResponseStatus(HttpStatus.OK)
	public ArtworkCommentListResponse getCommentList(@PathVariable Long postId, @RequestParam Long skip,
		@RequestParam Long size, @MomentiaUser User user) {
		return artworkCommentService.getCommentList(user, postId, skip, size);
	}

	/**
	 * [PATCH] 댓글 수정
	 */
	@PatchMapping("/comment/{commentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateComment(@PathVariable Long commentId,
		@RequestBody @Valid ArtworkCommentRequest artworkCommentCreateRequest, @MomentiaUser User user) {
		artworkCommentService.updateComment(user, commentId, artworkCommentCreateRequest);
	}

}
