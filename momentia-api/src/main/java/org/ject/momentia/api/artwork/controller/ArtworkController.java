package org.ject.momentia.api.artwork.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.api.global.annotation.EnumValue;
import org.ject.momentia.api.global.pagination.model.PaginationResponse;
import org.ject.momentia.api.artwork.service.ArtworkService;
import org.ject.momentia.api.artwork.service.Temp;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkController {

    private final ArtworkService artworkService;
    private final Temp temp;

    /**
     * [POST] 작품 업로드
     */
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ArtworkPostIdResponse createPost(@RequestBody @Valid ArtworkPostCreateRequest artworkPostCreateRequest) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        ArtworkPostIdResponse artworkPostId = artworkService.createPost(user, artworkPostCreateRequest);
        return artworkPostId;
    }

    /**
     * [GET] 작품 상세 보기
     */
    @GetMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ArtworkPostResponse getPost(@PathVariable Long postId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        ArtworkPostResponse artworkPostResponse = artworkService.getPost(user, postId);
        return artworkPostResponse;
    }

    /**
     * [DELETE] 작품 삭제
     */
    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();
        artworkService.deletePost(user, postId);
    }

    /**
     * [PATCH] 작품 수정
     */
    @PatchMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long postId, @RequestBody ArtworkPostUpdateRequest artworkPostUpdateRequest) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();
        artworkService.updatePost(user, postId, artworkPostUpdateRequest);
    }

    /**
        [GET] 작품 목록
     */
    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public PaginationResponse<ArtworkPostModel> getPostList(@RequestParam String sort,
                                                            @RequestParam(required = false) String search,
                                                            @RequestParam Integer page,
                                                            @RequestParam Integer size,
                                                            @EnumValue(enumClass = Category.class, ignoreCase = true,nullable = true)
                                                            @RequestParam(required = false) String field
    ) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        return artworkService.getPostList(user, sort, search, page, size, field);
    }

    @GetMapping("/followingUsers/posts")
    @ResponseStatus(HttpStatus.OK)
    public ArtworkFolloingUserPostsResponse getPostList(){
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        return artworkService.getFollowingUserPosts(user);
    }


}
