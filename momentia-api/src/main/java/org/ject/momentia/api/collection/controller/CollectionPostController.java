package org.ject.momentia.api.collection.controller;

import org.ject.momentia.api.artwork.model.dto.ArtworkPostModel;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.collection.model.CollectionArtworkCreateResponse;
import org.ject.momentia.api.collection.service.CollectionArtworkService;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineAndNameResponse;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

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
	public CollectionArtworkCreateResponse createCollectionPost(@PathVariable Long collectionId,
		@PathVariable Long postId, @MomentiaUser User user) {

		return collectionArtworkService.create(collectionId, postId, user);
	}

	/**
	 * [DELETE] 컬렉션 내부 - 작품 삭제
	 */
	@DeleteMapping("/{collectionId}/post/{postId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCollectionPost(@PathVariable Long collectionId, @PathVariable Long postId,
		@MomentiaUser User user) {

		collectionArtworkService.delete(collectionId, postId, user);
	}

	/**
	 * [GET] 컬렉션 내부 - 작품 리스트
	 * 본인 컬렉션일 경우 -> public 작품 + 본인 private 작품
	 * 본인 컬렉션 아닐 경우 -> public 작품만.
	 */
	@GetMapping("/{collectionId}/posts")
	@ResponseStatus(HttpStatus.OK) //?page=0&size=30&sort=recent
	public PaginationWithIsMineAndNameResponse<ArtworkPostModel> getCollectionPosts(
		@PathVariable Long collectionId,
		@EnumValue(enumClass = ArtworkPostSort.class, ignoreCase = true)
		@RequestParam String sort,
		@RequestParam @Min(0) Integer page,
		@RequestParam @Min(1) Integer size,
		@MomentiaUser User user
	) {
		return collectionArtworkService.getCollectionPosts(collectionId, sort, page, size, user);
	}

}
