package org.ject.momentia.api.collection.controller;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.collection.model.CollectionArtworkCreateResponse;
import org.ject.momentia.api.collection.service.CollectionArtworkService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/collection")
public class CollectionPostController {

    private final CollectionArtworkService collectionArtworkService;

    /**
     * [POST] 컬렉션 내부 - 작품 추가
     */
    @PostMapping("/{collectionId}/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionArtworkCreateResponse createCollectionPost(@PathVariable Long collectionId, @PathVariable Long postId,@MomentiaUser User user) {

        return collectionArtworkService.create(collectionId,postId,user);
    }

    /**
     * [DELETE] 컬렉션 내부 - 작품 삭제
     */
    @DeleteMapping("/{collectionId}/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollectionPost(@PathVariable Long collectionId, @PathVariable Long postId,@MomentiaUser User user) {

        collectionArtworkService.delete(collectionId,postId,user);
    }


    /**
     * [GET] 컬렉션 내부 - 작품 리스트 - ** public 작품들만
     */





}
