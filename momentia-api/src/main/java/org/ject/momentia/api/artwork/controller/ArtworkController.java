package org.ject.momentia.api.artwork.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.pagination.model.PaginationResponse;
import org.ject.momentia.api.artwork.service.ArtworkPostService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ArtworkPostIdResponse createPost(@RequestBody @Valid ArtworkPostCreateRequest artworkPostCreateRequest, @MomentiaUser User user) {
        return artworkPostService.createPost(user, artworkPostCreateRequest);
    }

    /**
     * [GET] 작품 상세 보기
     */
    @GetMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ArtworkPostResponse getPost(@MomentiaUser User user, @PathVariable Long postId) {
        return artworkPostService.getPost(user, postId);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long postId, @RequestBody ArtworkPostUpdateRequest artworkPostUpdateRequest, @MomentiaUser User user) {
        artworkPostService.updatePost(user, postId, artworkPostUpdateRequest);
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
    public ArtworkFolloingUserPostsResponse getPostList(@MomentiaUser User user) {
        return artworkPostService.getFollowingUserPosts(user);
    }


}
