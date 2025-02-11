package org.ject.momentia.api.artwork.controller;

import org.ject.momentia.api.artwork.model.ArtworkFollowingUserPostsResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostCreateRequest;
import org.ject.momentia.api.artwork.model.ArtworkPostIdResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostModel;
import org.ject.momentia.api.artwork.model.ArtworkPostResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostUpdateRequest;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.artwork.service.ArtworkPostService;
import org.ject.momentia.api.config.CookieUtil;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.pagination.model.PaginationResponse;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineResponse;
import org.ject.momentia.common.domain.artwork.type.Category;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkController {

	private final ArtworkPostService artworkPostService;

	/**
	 * [POST] 작품 업로드
	 */
	@PostMapping("/post")
	@ResponseStatus(HttpStatus.CREATED)
	public ArtworkPostIdResponse createPost(@RequestBody @Valid ArtworkPostCreateRequest artworkPostCreateRequest,
		@MomentiaUser User user) {
		return artworkPostService.createPost(user, artworkPostCreateRequest);
	}

	/**
	 * [GET] 작품 상세 보기
	 */
	@GetMapping("/post/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public ArtworkPostResponse getPost(@MomentiaUser User user, @PathVariable Long postId
	) {
		return artworkPostService.getPost(user, postId, true);
	}

	/**
	 * [GET] 작품 상세 보기 - 쿠키 설정 테스트
	 */
	@GetMapping("/post2/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public ArtworkPostResponse getPostWithCookie(@MomentiaUser User user, @PathVariable Long postId
		, HttpServletRequest request, HttpServletResponse response
	) {
		// 게시글을 본 적이 없으면 쿠키에 추가하고 업데이트
		boolean updateView = updateViewedPosts(postId, request, response);

		return artworkPostService.getPost(user, postId, updateView);
	}

	private boolean updateViewedPosts(Long postId, HttpServletRequest request, HttpServletResponse response) {
		String cookieName = "viewed_posts";
		int cookieExpireTime = 60; // 1분
		String viewedPosts = CookieUtil.getCookieValue(request, cookieName).orElse("");

		// 게시글을 본 적이 없으면
		boolean isNewPost = !viewedPosts.contains(":" + postId + ":");

		if (isNewPost) {
			viewedPosts += ":" + postId + ":";
			CookieUtil.addCookie(response, cookieName, viewedPosts, cookieExpireTime);
		}

		return isNewPost; // 게시글이 처음이면 true 반환
	}

	/**
	 * [DELETE] 작품 삭제
	 */
	@DeleteMapping("/post/{postId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePost(@PathVariable Long postId, @MomentiaUser User user) {
		artworkPostService.deletePost(user, postId);
	}

	/**
	 * [PATCH] 작품 수정
	 */
	@PatchMapping("/post/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public ArtworkPostIdResponse updatePost(@PathVariable Long postId,
		@Valid @RequestBody ArtworkPostUpdateRequest artworkPostUpdateRequest,
		@MomentiaUser User user) {
		return artworkPostService.updatePost(user, postId, artworkPostUpdateRequest);
	}

	/**
	 * [GET] 작품 목록
	 */
	@GetMapping("/posts")
	@ResponseStatus(HttpStatus.OK)
	public PaginationResponse<ArtworkPostModel> getPostList(
		@EnumValue(enumClass = ArtworkPostSort.class, ignoreCase = true)
		@RequestParam String sort,
		@RequestParam(required = false) String search,
		@RequestParam @Min(0) Integer page,
		@RequestParam @Min(1) Integer size,
		@EnumValue(enumClass = Category.class, nullable = true)
		@RequestParam(required = false) String artworkField,
		@MomentiaUser User user
	) {
		return artworkPostService.getPostList(user, sort, search, page, size, artworkField);
	}

	/**
	 * [GET] 팔로잉 게시물
	 */
	@GetMapping("/followingUsers/posts")
	@ResponseStatus(HttpStatus.OK)
	public ArtworkFollowingUserPostsResponse getPostList(@MomentiaUser User user) {
		return artworkPostService.getFollowingUserPosts(user);
	}

	/**
	 * [GET] 프로필 페이지 - 작품리스트
	 * /v1/artwork/user/posts?sort=recent&page=0&size=20&userId=2
	 */
	@GetMapping("/user/posts")
	@ResponseStatus(HttpStatus.OK)
	public PaginationWithIsMineResponse<ArtworkPostModel> getUserPostList(
		@EnumValue(enumClass = ArtworkPostSort.class, ignoreCase = true)
		@RequestParam String sort,
		@RequestParam @Min(0) Integer page,
		@RequestParam @Min(1) Integer size,
		@RequestParam(required = false) Long userId, // null 이면 본인 프로필
		@MomentiaUser User user
	) {
		return artworkPostService.getUserPosts(sort, page, size, userId, user);
	}

	/**
	 * [GET] 좋아요한 작품 리스트
	 * /v1/artwork/like/posts?sort=recent&page=0&size=20
	 */
	@GetMapping("/like/posts")
	@ResponseStatus(HttpStatus.OK)
	public PaginationResponse<ArtworkPostModel> getLikePostList(
		@EnumValue(enumClass = ArtworkPostSort.class, ignoreCase = true)
		@RequestParam String sort,
		@RequestParam @Min(0) Integer page,
		@RequestParam @Min(1) Integer size,
		@MomentiaUser User user
	) {
		return artworkPostService.getLikePosts(sort, page, size, user);
	}

}
