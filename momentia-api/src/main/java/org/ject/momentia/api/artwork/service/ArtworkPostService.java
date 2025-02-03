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

	@Transactional
	public ArtworkPostIdResponse createPost(User user, ArtworkPostCreateRequest artworkPostCreateRequest) {
		ArtworkPost artworkPost = ArtworkPostConverter.toArtworkPost(artworkPostCreateRequest, user);

		artworkPost = artworkPostRepository.save(artworkPost);

		imageService.useImageToService(artworkPostCreateRequest.postImage(), ImageTargetType.ARTWORK,
			artworkPost.getId());

		return ArtworkPostConverter.toArtworkPostIdResponse(artworkPost);
	}

	@Transactional
	public ArtworkPostResponse getPost(User user, Long postId) {
		///  Todo : 조회수 처리

		ArtworkPost artworkPost = artworkPostRepository.findById(postId)
			.orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);

		boolean isMine = user != null && Objects.equals(user.getId(), artworkPost.getUser().getId());
		boolean isLiked = artworkLikeService.isLiked(user, artworkPost);
		boolean isFollow = followService.isFollowing(user, artworkPost.getUser());

		String postImage = imageService.getImageUrl(ImageTargetType.ARTWORK, artworkPost.getId());
		String profileImage = artworkPost.getUser().getProfileImage() != null ?
			imageService.getImageUrl(ImageTargetType.PROFILE, artworkPost.getUser().getId()) : null;

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
		return new ArtworkPostIdResponse(artworkPost.getId());
	}

	@Transactional
	public PaginationResponse<ArtworkPostModel> getPostList(User user, String sort, String keyword, Integer page,
		Integer size, String categoryName) {
		Category category = (categoryName == null) ? null : Category.valueOf(categoryName);
		String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		if (keyword != null && (keyword = keyword.trim().replaceAll(" ", "")).isBlank())
			keyword = null;

		Page<ArtworkPost> posts;

		// 검색 할 경우
		if (keyword != null)
			posts = artworkPostRepository.search(category, keyword, pageable);
		else {
			posts = (category != null)
				? artworkPostRepository.findByCategoryAndStatus(category, ArtworkPostStatus.PUBLIC, pageable)
				: artworkPostRepository.findByStatus(ArtworkPostStatus.PUBLIC, pageable);
		}

		List<ArtworkPostModel> postModelList = posts.getContent().stream()
			.map((p) -> {
					String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
					Boolean isLiked = artworkLikeService.isLiked(user, p);
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

	@Transactional
	public ArtworkFollowingUserPostsResponse getFollowingUserPosts(User user) {
		if (user == null)
			throw ErrorCd.NO_PERMISSION.serviceException();
		/// 팔로우 하고 있는 유저 리스트
		List<User> userList = followService.findFollowers(user);

		List<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
		List<FollowingUserPostProjection> lists = artworkPostRepository.findTwoRecentPostsByUserIds(ids);

		List<FollowingUserModel> userModelList = userList.stream().map((u) -> {
			String imageUrl = null;
			if (u.getProfileImage() != null)
				imageUrl = imageService.getImageUrl(ImageTargetType.PROFILE, u.getId());
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
