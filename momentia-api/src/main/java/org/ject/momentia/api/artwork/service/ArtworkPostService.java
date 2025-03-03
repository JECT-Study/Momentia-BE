package org.ject.momentia.api.artwork.service;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.ArtworkFollowingUserPostModel;
import org.ject.momentia.api.artwork.model.ArtworkFollowingUserPostsResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostCreateRequest;
import org.ject.momentia.api.artwork.model.ArtworkPostIdResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostModel;
import org.ject.momentia.api.artwork.model.ArtworkPostResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostUpdateRequest;
import org.ject.momentia.api.artwork.model.FollowingUserModel;
import org.ject.momentia.api.artwork.model.FollowingUserPostProjection;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkPostCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkViewCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.PageIdsCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkPostCacheModel;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkViewCacheModel;
import org.ject.momentia.api.artwork.repository.cache.model.PageIdsCacheModel;
import org.ject.momentia.api.artwork.service.module.ArtworkCommentModuleService;
import org.ject.momentia.api.artwork.service.module.ArtworkLikeModuleService;
import org.ject.momentia.api.collection.service.module.CollectionArtworkModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.service.module.FollowModuleService;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.pagination.converter.PaginationConverter;
import org.ject.momentia.api.pagination.model.PaginationModel;
import org.ject.momentia.api.pagination.model.PaginationResponse;
import org.ject.momentia.api.pagination.model.PaginationWithIsMineResponse;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
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
public class ArtworkPostService {

	private final ArtworkPostRepository artworkPostRepository;
	private final UserRepository userRepository;

	private final ImageService imageService;

	private final CollectionArtworkModuleService collectionArtworkService;
	private final FollowModuleService followService;
	private final ArtworkLikeModuleService artworkLikeService;
	private final ArtworkCommentModuleService artworkCommentService;
	private final ArtworkPostCacheRepository artworkPostCacheRepository;

	private final PageIdsCacheRepository pageIdsCacheRepository;
	private final ArtworkViewCacheRepository artworkViewCacheRepository;

	@Transactional
	public ArtworkPostIdResponse createPost(User user, ArtworkPostCreateRequest artworkPostCreateRequest) {
		ArtworkPost artworkPost = ArtworkPostConverter.toArtworkPost(artworkPostCreateRequest, user);

		artworkPost = artworkPostRepository.save(artworkPost);

		imageService.useImageToService(artworkPostCreateRequest.postImage(), ImageTargetType.ARTWORK,
			artworkPost.getId());

		return ArtworkPostConverter.toArtworkPostIdResponse(artworkPost);
	}

	@Transactional
	public ArtworkPostResponse getPost(User user, Long postId, Boolean updateView) {

		ArtworkPost artworkPost = artworkPostRepository.findById(postId)
			.orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);

		boolean isMine = user != null && Objects.equals(user.getId(), artworkPost.getUser().getId());
		boolean isLiked = artworkLikeService.isLiked(user, artworkPost);
		Boolean isFollow = followService.isFollowing(user, artworkPost.getUser(), true);

		String postImage = imageService.getImageUrl(ImageTargetType.ARTWORK, artworkPost.getId());

		if (updateView) {
			ArtworkViewCacheModel artworkViewCacheModel = artworkViewCacheRepository.findById(artworkPost.getId())
				.orElse(null);
			if (artworkViewCacheModel == null) {
				artworkViewCacheModel = new ArtworkViewCacheModel(artworkPost.getId());
			}
			artworkViewCacheModel.increaseView();
			artworkViewCacheRepository.save(artworkViewCacheModel);
		}

		return ArtworkPostConverter.toArtworkPostResponse(artworkPost, isMine, isLiked, postImage, isFollow);
	}

	@Transactional
	public void deletePost(User user, Long postId) {
		ArtworkPost artworkPost = artworkPostRepository.findById(postId)
			.orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
		if (user != null && !Objects.equals(user.getId(), artworkPost.getUser().getId()))
			ErrorCd.NO_PERMISSION.serviceException();

		artworkPostRepository.delete(artworkPost);

		/// todo : (일단 이달의 작품에서는 삭제하지 않음 -> 동작에 일단 이상 x)
		collectionArtworkService.deleteAllArtworksInCollection(artworkPost);
		artworkCommentService.deleteAllByPost(artworkPost);
		artworkLikeService.deleteAllByArtwork(artworkPost);

		artworkPostCacheRepository.deleteById(artworkPost.getId());
		pageIdsCacheRepository.deleteAll();
	}

	@Transactional
	public ArtworkPostIdResponse updatePost(User user, Long postId, ArtworkPostUpdateRequest updateRequest) {
		ArtworkPost artworkPost = artworkPostRepository.findById(postId)
			.orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
		if (user == null || !Objects.equals(artworkPost.getUser().getId(), user.getId()))
			ErrorCd.NO_PERMISSION.serviceException();
		artworkPost.updatePost(updateRequest.status(), updateRequest.artworkField(), updateRequest.title(),
			updateRequest.explanation());
		artworkPostRepository.save(artworkPost);
		artworkPostCacheRepository.deleteById(artworkPost.getId());
		return new ArtworkPostIdResponse(artworkPost.getId());
	}

	@Transactional
	public PaginationResponse<ArtworkPostModel> getPostList(User user, String sort, String keyword, Integer page,
		Integer size, String categoryName) {

		PageIdsCacheModel pageIdsCacheModel = getPageIdsCacheModel(sort, keyword, page, size, categoryName);

		List<Long> ids = pageIdsCacheModel.getIds();

		List<ArtworkPostModel> postModelList = IdListToArtworkPostModelList(ids, user);

		PaginationModel paginationResponse = new PaginationModel(pageIdsCacheModel.getTotalDataCnt(),
			pageIdsCacheModel.getTotalPages(),
			pageIdsCacheModel.getIsLastPage(), pageIdsCacheModel.getIsFirstPage(), pageIdsCacheModel.getRequestPage(),
			pageIdsCacheModel.getRequestSize());

		return new PaginationResponse<>(
			postModelList,
			paginationResponse
		);
	}

	private PageIdsCacheModel getPageIdsCacheModel(String sort, String keyword, Integer page, Integer size,
		String categoryName) {

		Category category = (categoryName == null) ? null : Category.valueOf(categoryName);
		String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy, "id").descending());
		if (keyword != null && (keyword = keyword.trim().replaceAll(" ", "")).isBlank())
			keyword = null;

		String key = "artworkPostList:sort:" + sortBy + ":keyword:" + keyword + ":page:" + page + ":size:" + size
			+ ":category:"
			+ (category == null ? null : category.name());

		Page<Long> pages;

		/// 레디스에 키 값에 해당하는 id List 검색
		/// ********* 캐시 삭제 에러로 캐시 조회 부분 잠시 삭제 -> 버그 수정하고 다시 캐시 적용 할 예정
		PageIdsCacheModel pageIdsCacheModel = null;  //pageIdsCacheRepository.findById(key).orElse(null);

		/// 캐시에 저장되어 있지 않다면, rdb에서 가져온다.
		if (pageIdsCacheModel == null) {
			if (keyword == null)
				pages = artworkPostRepository.findByIdListCategoryAndStatus(category, pageable);
			else
				pages = artworkPostRepository.findByIdListCategoryAndStatusAndKeyword(category, keyword, pageable);
			/// 잠시 삭제 pageIdsCacheModel = pageIdsCacheRepository.save(new PageIdsCacheModel(key, pages));
			pageIdsCacheModel = new PageIdsCacheModel(key, pages);
		}

		return pageIdsCacheModel;

	}

	private List<ArtworkPostModel> IdListToArtworkPostModelList(List<Long> ids, User user) {
		return ids.stream().map((id) -> {
			/// Todo : idList 넘겨서 rdb 한번 찌르는 로직으로 수정

			ArtworkPostCacheModel artworkPostCacheModel = artworkPostCacheRepository.findById(id).orElse(null);
			// 작품이 캐시에 없다면
			if (artworkPostCacheModel == null) {
				ArtworkPost artworkPost = artworkPostRepository.findById(id).get();
				String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, artworkPost.getId());
				Boolean isLiked = artworkLikeService.isLiked(user, artworkPost);
				ArtworkPostCacheModel newArtworkPostCacheModel = new ArtworkPostCacheModel(artworkPost, imageUrl);
				artworkPostCacheRepository.save(newArtworkPostCacheModel);
				return ArtworkPostConverter.toArtworkPostModel(artworkPost, isLiked, imageUrl);
			}
			// 있으면
			else {
				Boolean isLiked = artworkLikeService.isLikedByPostId(user, artworkPostCacheModel.getId());
				User author = userRepository.findById(artworkPostCacheModel.getUserId())
					.orElseThrow(ErrorCd.USER_NOT_FOUND::serviceException);
				String nickname = author.getNickname();
				return ArtworkPostConverter.ArtworkPostCacheModeltoArtworkPostModel(artworkPostCacheModel, isLiked,
					nickname);
			}

		}).toList();
	}

	@Transactional
	public ArtworkFollowingUserPostsResponse getFollowingUserPosts(User user) {
		if (user == null)
			throw ErrorCd.NO_PERMISSION.serviceException();
		/// 팔로우 하고 있는 유저 리스트
		List<User> userList = followService.findFollowers(user);

		List<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
		List<FollowingUserPostProjection> lists = artworkPostRepository.findTwoRecentPostsByUserIds(ids);

		List<FollowingUserModel> userModelList = userList.stream().map((u) -> {
			return ArtworkPostConverter.toFollowingUserModel(u);
		}).collect(Collectors.toList());

		Iterator<FollowingUserModel> iterator = userModelList.iterator();
		while (iterator.hasNext()) {
			FollowingUserModel u = iterator.next();
			for (FollowingUserPostProjection p : lists) {
				if (p.getUserId().equals(u.getUserId())) {
					String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
					Boolean isLiked = artworkLikeService.isLikedByPostId(user, p.getId());
					u.getPosts().add(
						ArtworkPostConverter.toArtworkFollowingUserPostModel(p, imageUrl, isLiked)
					);
				}
			}
			if (u.getPosts().isEmpty())
				iterator.remove();
		}

		sortByFirstPost(userModelList);
		return new ArtworkFollowingUserPostsResponse(userModelList);
	}

	@Transactional
	public PaginationWithIsMineResponse<ArtworkPostModel> getUserPosts(String sort, Integer page, Integer size,
		Long userId,
		User user) {
		if (user == null && userId == null)
			throw ErrorCd.NO_PERMISSION.serviceException();

		User targetUser =
			userId == null ? user :
				userRepository.findById(userId).orElseThrow(ErrorCd.USER_NOT_FOUND::serviceException);

		String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		Boolean isMine = user != null && user.equals(targetUser);

		Page<ArtworkPost> posts = isMine
			? artworkPostRepository.findByUser(targetUser, pageable)
			: artworkPostRepository.findByUserAndStatus(targetUser, ArtworkPostStatus.PUBLIC, pageable);

		List<ArtworkPostModel> postModelList = posts.getContent().stream()
			.map((p) -> {
					String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
					Boolean isLiked = artworkLikeService.isLiked(user, p);
					return ArtworkPostConverter.toArtworkPostModel(p, isLiked, imageUrl);
				}
			)
			.toList();
		PaginationModel paginationResponse = PaginationConverter.pageToPaginationModel(posts);

		return new PaginationWithIsMineResponse<>(
			isMine,
			postModelList,
			paginationResponse
		);

	}

	@Transactional
	public PaginationResponse<ArtworkPostModel> getLikePosts(String sort, Integer page, Integer size, User user) {
		if (user == null)
			throw ErrorCd.NO_PERMISSION.serviceException();
		String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		List<Long> postIds = artworkLikeService.getIdList(user);
		Page<ArtworkPost> posts = artworkPostRepository.findAllByIdIn(
			postIds,
			user,
			pageable);

		List<ArtworkPostModel> postModelList = posts.getContent().stream()
			.map((p) -> {
					String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
					Boolean isLiked = true; // 무조건 true로 세팅
					return ArtworkPostConverter.toArtworkPostModel(p, isLiked, imageUrl);
				}
			)
			.toList();

		PaginationModel paginationResponse = PaginationConverter.pageToPaginationModel(posts);

		return new PaginationResponse<>(
			postModelList,
			paginationResponse
		);

	}

	private void sortByFirstPost(List<FollowingUserModel> list) {
		list.sort((user1, user2) -> {
			if (user1.getPosts().isEmpty() || user2.getPosts().isEmpty()) {
				return 0;  // 포스트가 없으면 동일하게 취급
			}

			ArtworkFollowingUserPostModel post1 = user1.getPosts().get(0);
			ArtworkFollowingUserPostModel post2 = user2.getPosts().get(0);

			return post2.createdTime().compareTo(post1.createdTime());
		});
	}

}
