package org.ject.momentia.api.collection.service;

import java.util.ArrayList;
import java.util.List;

import org.ject.momentia.api.collection.converter.CollectionConverter;
import org.ject.momentia.api.collection.model.CollecionListResponse;
import org.ject.momentia.api.collection.model.CollectionCreateResquest;
import org.ject.momentia.api.collection.model.CollectionIdResponse;
import org.ject.momentia.api.collection.model.CollectionListModel;
import org.ject.momentia.api.collection.model.CollectionUpdateRequest;
import org.ject.momentia.api.collection.model.type.CollectionSort;
import org.ject.momentia.api.collection.repository.CollectionRepository;
import org.ject.momentia.api.collection.service.module.CollectionArtworkModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.pagination.converter.PaginationConverter;
import org.ject.momentia.api.pagination.model.PaginationModel;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineResponse;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
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
public class CollectionService {

	private final CollectionRepository collectionRepository;
	private final UserRepository userRepository;

	private final ImageService imageService;
	private final CollectionArtworkModuleService collectionArtworkService;

	@Transactional
	public CollectionIdResponse createCollection(CollectionCreateResquest request, User user) {
		Collection collection = CollectionConverter.toCollection(request, user);

		// 이름 중복 체크
		if (collectionRepository.existsCollectionsByNameAndUser(request.name(), user)) {
			throw ErrorCd.DUPLICATE_COLLECTION_NAME.serviceException();
		}

		collection = collectionRepository.save(collection);
		return new CollectionIdResponse(collection.getId());
	}

	@Transactional
	public void deleteCollection(Long collectionId, User user) {
		Collection collection = collectionRepository.findById(collectionId)
			.orElseThrow(ErrorCd.COLLECTION_NOT_FOUND::serviceException);
		if (user == null || !collection.getUser().getId().equals(user.getId()))
			throw ErrorCd.NO_PERMISSION.serviceException();
		collectionRepository.delete(collection);
		collectionArtworkService.deleteAllByCollection(collection);
	}

	@Transactional
	public void updateCollection(Long collectionId, CollectionUpdateRequest request, User user) {
		Collection collection = collectionRepository.findById(collectionId)
			.orElseThrow(ErrorCd.COLLECTION_NOT_FOUND::serviceException);
		if (user == null || !collection.getUser().getId().equals(user.getId()))
			throw ErrorCd.NO_PERMISSION.serviceException();
		collection.update(request.name(), request.status());
	}

	@Transactional
	public CollecionListResponse getAllCollections(User user) {
		if (user == null)
			throw ErrorCd.NO_PERMISSION.serviceException();
		List<Collection> collectionList = collectionRepository.findAllByUserOrderByCreatedAtDesc(user);
		List<CollectionListModel> collectionListModelList = new ArrayList<>();
		for (Collection collection : collectionList) {
			ArtworkPost collectionArtwork =
				collectionArtworkService.findByArtworkPostByCollectionIdElseReturnNull(true, user, collection.getId());
			String postImage = null;
			if (collectionArtwork != null) {
				postImage = imageService.getImageUrl(ImageTargetType.ARTWORK, collectionArtwork.getId());
			}
			collectionListModelList.add(CollectionConverter.toCollectionListModel(collection, postImage));
		}
		return new CollecionListResponse(collectionListModelList);
	}

	@Transactional
	public PaginationWithIsMineResponse<CollectionListModel> getCollections(User user, Long userId, String sort,
		Integer page, Integer size) {
		if (user == null && userId == null)
			throw ErrorCd.NO_PERMISSION.serviceException();
		User targetUser =
			userId == null ? user :
				userRepository.findById(userId).orElseThrow(ErrorCd.USER_NOT_FOUND::serviceException);
		Boolean isMine = user != null && user.equals(targetUser);
		String sortBy = CollectionSort.valueOf(sort.toUpperCase()).getColumnName();

		Sort sortObject = sortBy.equals("name") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sortObject);
		Page<Collection> collections = isMine
			// 내 컬렉션일 경우
			? collectionRepository.findAllByUser(targetUser, pageable)
			// 내 컬렉션 리스트 아닐 경우, 공개 데이터만 가져옴
			: collectionRepository.findAllByUserAndStatus(targetUser, CollectionStatus.PUBLIC, pageable);

		List<CollectionListModel> collectionListModelList = collections.getContent().stream().map((c) -> {
			ArtworkPost collectionArtwork =
				collectionArtworkService.findByArtworkPostByCollectionIdElseReturnNull(isMine, user, c.getId());
			String postImage = null;
			if (collectionArtwork != null) {
				postImage = imageService.getImageUrl(ImageTargetType.ARTWORK, collectionArtwork.getId());
			}
			return CollectionConverter.toCollectionListModel(c, postImage);
		}).toList();

		PaginationModel paginationResponse = PaginationConverter.pageToPaginationModel(collections);

		return new PaginationWithIsMineResponse<CollectionListModel>(
			isMine,
			collectionListModelList,
			paginationResponse
		);
	}

}
