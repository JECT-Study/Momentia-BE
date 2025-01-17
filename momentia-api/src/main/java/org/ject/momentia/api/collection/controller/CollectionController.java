package org.ject.momentia.api.collection.controller;

import org.ject.momentia.api.collection.model.CollectionCreateRequest;
import org.ject.momentia.api.collection.model.CollectionIdResponse;
import org.ject.momentia.api.collection.model.CollectionListModel;
import org.ject.momentia.api.collection.model.CollectionListResponse;
import org.ject.momentia.api.collection.model.CollectionUpdateRequest;
import org.ject.momentia.api.collection.model.type.CollectionSort;
import org.ject.momentia.api.collection.service.CollectionService;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineResponse;
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
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CollectionController {

	private final CollectionService collectionService;

	/**
	 [POST] 컬렉션 생성
	 */
	@PostMapping("/collection")
	@ResponseStatus(HttpStatus.CREATED)
	public CollectionIdResponse createCollection(@RequestBody @Valid CollectionCreateRequest request,
		@MomentiaUser User user) {
		return collectionService.createCollection(request, user);
	}

	/**
	 * [DELETE] 컬렉션 삭제
	 */
	@DeleteMapping("/collection/{collectionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCollection(@PathVariable @Valid Long collectionId, @MomentiaUser User user) {
		collectionService.deleteCollection(collectionId, user);
	}

	/**
	 * [PATCH] 컬렉션 이름, 공개 여부 수정
	 */
	@PatchMapping("/collection/{collectionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCollection(@PathVariable Long collectionId, @RequestBody @Valid CollectionUpdateRequest request,
		@MomentiaUser User user) {
		collectionService.updateCollection(collectionId, request, user);
	}

	/**
	 * [GET] 컬렉션 전체 리스트 - 썸네일 사진(공개 작품 or 본인 작품), 공개/비공개 상관없이 모두 반환
	 */
	@GetMapping("/collections/all")
	@ResponseStatus(HttpStatus.OK)
	public CollectionListResponse getAllCollections(@MomentiaUser User user) {
		return collectionService.getAllCollections(user);
	}

	/**
	 * [GET] 컬렉션 페이지네이션 - 썸네일 사진, 본인 컬렉션일 경우 모두 반환, 본인 아닐경우 public만
	 */
	@GetMapping("/collections")
	@ResponseStatus(HttpStatus.OK)
	public PaginationWithIsMineResponse<CollectionListModel> getCollections(
		@MomentiaUser User user,
		@EnumValue(enumClass = CollectionSort.class, ignoreCase = true)
		@RequestParam String sort,
		@RequestParam @Min(0) Integer page,
		@RequestParam @Min(1) Integer size,
		@RequestParam(required = false) Long userId
	) {
		return collectionService.getCollections(user, userId, sort, page, size);
	}

}
