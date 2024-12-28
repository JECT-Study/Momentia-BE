package org.ject.momentia.api.artwork.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.api.artwork.service.ArtworkCommentService;
import org.ject.momentia.api.artwork.service.Temp;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkCommentController {

    private final Temp temp;
    private final ArtworkCommentService artworkCommentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/post/{postId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ArtworkCommentIdResponse createComment(@RequestBody @Valid ArtworkCommentRequest artworkCommentCreateRequest, @PathVariable Long postId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        ArtworkCommentIdResponse artworkCommentIdResponse = artworkCommentService.createComment(user,postId,artworkCommentCreateRequest);
        return artworkCommentIdResponse;
    }


    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        artworkCommentService.deleteComment(user,commentId);
    }


    /**
     * 댓글 리스트 가져오기
     */
    @GetMapping("/post/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ArtworkCommentListResponse getCommentList(@PathVariable Long postId, @RequestParam(required = true) Long skip, @RequestParam(required = true) Long size) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        ArtworkCommentListResponse artworkCommentListResponse = artworkCommentService.getCommentList(user,postId,skip,size);
        return artworkCommentListResponse;
    }


    /**
     * 댓글 수정
     */
    @PatchMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(@PathVariable Long commentId, @RequestBody @Valid ArtworkCommentRequest artworkCommentCreateRequest) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();
        artworkCommentService.updateComment(user,commentId,artworkCommentCreateRequest);
    }

}
