package org.ject.momentia.api.artwork.controller;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.service.ArtworkLikeService;
import org.ject.momentia.api.artwork.service.Temp;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/artwork")
public class ArtworkLikeController {

    private final Temp temp;
    private final ArtworkLikeService artworkLikeService;

    /**
     * [POST] 작품 좋아요
     */
    @PostMapping("/post/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void like(@PathVariable Long postId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();
        artworkLikeService.like(user,postId);
    }


    /**
     * [DELETE] 작품 좋아요 취소
     */
    @DeleteMapping("/post/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable Long postId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();
        artworkLikeService.unlike(user,postId);
    }


}
