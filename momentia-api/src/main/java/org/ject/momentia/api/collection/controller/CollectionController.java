package org.ject.momentia.api.collection.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.service.Temp;
import org.ject.momentia.api.collection.model.CollecionListResponse;
import org.ject.momentia.api.collection.model.CollectionCreateResquest;
import org.ject.momentia.api.collection.model.CollectionIdResponse;
import org.ject.momentia.api.collection.model.CollectionUpdateRequest;
import org.ject.momentia.api.collection.service.CollectionService;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CollectionController {

    private final Temp temp;
    private final CollectionService collectionService;

    /**
        [POST] 컬렉션 생성
     */
    @PostMapping("/collection")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionIdResponse createCollection(@RequestBody @Valid CollectionCreateResquest request) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        CollectionIdResponse response =  collectionService.createCollection(request, user);
        return response;
    }

    /**
     * [DELETE] 컬렉션 삭제
     */
    @DeleteMapping("/collection/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(@PathVariable @Valid Long collectionId) {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        collectionService.deleteCollection(collectionId,user);
    }


    /**
     * [PATCH] 컬렉션 이름, 공개 여부 수정
     */
    @PatchMapping("/collection/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCollection(@PathVariable Long collectionId, @RequestBody @Valid CollectionUpdateRequest request){
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        collectionService.updateCollection(collectionId,request,user);
    }



    /**
     * [GET] 컬렉션 전체 리스트 - 썸네일 사진(공개 작품 or 본인 작품), 공개/비공개 상관없이 모두 반환
     */
    @GetMapping("/collections/all")
    @ResponseStatus(HttpStatus.OK)
    public CollecionListResponse getAllCollections(){
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        return collectionService.getAllCollections(user);
    }


    /**
     * [GET] 컬렉션 페이지네이션 - 썸네일 사진, 본인 컬렉션일 경우 모두 반환, 본인 아닐경우 public만
     */


}
