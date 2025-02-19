package org.ject.momentia.api.collection.service;

import java.util.List;

import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.ArtworkPostModel;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.service.module.ArtworkLikeModuleService;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.collection.converter.CollectionArtworkConverter;
import org.ject.momentia.api.collection.model.CollectionArtworkCreateResponse;
import org.ject.momentia.api.collection.repository.CollectionArtworkRepository;
import org.ject.momentia.api.collection.service.module.CollectionArtworkModuleService;
import org.ject.momentia.api.collection.service.module.CollectionModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.pagination.converter.PaginationConverter;
import org.ject.momentia.api.pagination.model.PaginationModel;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineAndNameResponse;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CollectionArtworkService {

	private final CollectionArtworkRepository collectionArtworkRepository;
	private final ArtworkPostRepository artworkPostRepository;

	private final ArtworkPostModuleService artworkService;
	private final CollectionArtworkModuleService collectionArtworkModuleService;
	private final CollectionModuleService collectionService;
	private final ImageService imageService;
	private final ArtworkLikeModuleService artworkLikeService;

	@Transactional
	public CollectionArtworkCreateResponse create(Long collectionId, Long artworkId, User user) {
		Collection collection = collectionService.findCollectionElseThrowException(collectionId);
		collectionArtworkModuleService.hasPermissionAtCollectionElseThrowException(collection, user);
		ArtworkPost artwork = artworkService.findPostByIdElseThrowError(artworkId);
		CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection, artwork);
		if (collectionArtworkRepository.existsById(collectionArtwork.getId()))
			throw ErrorCd.DUPLICATE_COLLECTION_ARTWORK.serviceException();
		collectionArtworkRepository.save(collectionArtwork);
		return new CollectionArtworkCreateResponse(collection.getId(), artwork.getId());
	}

	@Transactional
	public void delete(Long collectionId, Long artworkId, User user) {
		Collection collection = collectionService.findCollectionElseThrowException(collectionId);
		collectionArtworkModuleService.hasPermissionAtCollectionElseThrowException(collection, user);
		ArtworkPost artwork = artworkService.findPostByIdElseThrowError(artworkId);
		CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection, artwork);
		if (!collectionArtworkRepository.existsById(collectionArtwork.getId()))
			throw ErrorCd.COLLECTION_ARTWORK_ALREADY_REMOVED.serviceException();
		collectionArtworkRepository.delete(collectionArtwork);
	}

	@Transactional
	public PaginationWithIsMineAndNameResponse<ArtworkPostModel> getCollectionPosts(
		Long collectionId, String sort, Integer page, Integer size, User user) {
		Collection collection = collectionService.findCollectionElseThrowException(collectionId);
		if (collection.getStatus().equals(CollectionStatus.PRIVATE) && !collection.getUser().equals(user)) {
			throw ErrorCd.NO_PERMISSION.serviceException();
		}
		Boolean isMine = collection.getUser().equals(user);
		String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		Page<ArtworkPost> posts = collectionArtworkRepository.findPostByCollectionAndIsMine(user, isMine, collection,
			pageable);

		List<ArtworkPostModel> postModelList = posts.getContent().stream()
			.map((p) -> {
					String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
					Boolean isLiked = artworkLikeService.isLiked(user, p);
					Long addCount = artworkLikeService.getLikeCountRDBAndCacheSum(p.getId());
					return ArtworkPostConverter.toArtworkPostModel(p, isLiked, imageUrl, addCount);
				}
			)
			.toList();
		PaginationModel paginationResponse = PaginationConverter.pageToPaginationModel(posts);

		return new PaginationWithIsMineAndNameResponse<>(
			isMine,
			collection.getName(),
			postModelList,
			paginationResponse
		);
	}

}
