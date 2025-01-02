package org.ject.momentia.api.artwork.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.type.ArtworkPostSort;
import org.ject.momentia.api.collection.service.CollectionArtworkService;
import org.ject.momentia.api.follow.service.FollowService;
import org.ject.momentia.api.global.pagination.converter.PaginationConverter;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.api.global.pagination.model.PaginationModel;
import org.ject.momentia.api.global.pagination.model.PaginationResponse;
import org.ject.momentia.api.artwork.repository.ArtworkCommentRepository;
import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtworkService {

    private final ArtworkPostRepository artworkPostRepository;
    private final ArtworkLikeRepository artworkLikeRepository;
    private final FollowRepository followRepository;

    private final ImageService imageService;
    private final CollectionArtworkService collectionArtworkService;
    private final FollowService followService;
    private final ArtworkCommentRepository artworkCommentRepository;

    @Transactional
    public ArtworkPostIdResponse createPost(User user, ArtworkPostCreateRequest artworkPostCreateRequest) {
        ArtworkPost artworkPost = ArtworkPostConverter.toArtworkPost(artworkPostCreateRequest, user);

        artworkPost = artworkPostRepository.save(artworkPost);

        imageService.useImageToService(artworkPostCreateRequest.postImage(), ImageTargetType.ARTWORK,artworkPost.getId());

        return ArtworkPostConverter.toArtworkPostIdResponse(artworkPost);
    }

    public ArtworkPostResponse getPost(User user, Long postId) {
        ///  Todo : 조회수 처리

        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);

        boolean isMine = user != null && Objects.equals(user.getId(), artworkPost.getUser().getId());
        boolean isLiked = user != null && artworkLikeRepository.existsById(new ArtworkLikeId(user, artworkPost));
        boolean isFollow = user != null && followService.isFollowing(user, artworkPost.getUser());

        String postImage = imageService.getImageUrl(ImageTargetType.ARTWORK,artworkPost.getId());
        String profileImage = artworkPost.getUser().getProfileImage() != null ? imageService.getImageUrl(ImageTargetType.PROFILE,artworkPost.getUser().getId()) : null;

        return ArtworkPostConverter.toArtworkPostResponse(artworkPost, isMine, isLiked, postImage,isFollow,profileImage);
    }

    @Transactional
    public void deletePost(User user, Long postId) {
        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        if (user != null && !Objects.equals(user.getId(), artworkPost.getUser().getId())) ErrorCd.NO_PERMISSION.serviceException();

        artworkPostRepository.delete(artworkPost);

        /// todo : (일단 이달의 작품에서는 삭제하지 않음 -> 동작에 일단 이상 x)
        collectionArtworkService.deleteAllArtworksInCollection(artworkPost);
        artworkCommentRepository.deleteAllByPost(artworkPost);
        artworkLikeRepository.deleteAllByArtwork(artworkPost);

    }

    @Transactional
    public void updatePost(User user, Long postId, ArtworkPostUpdateRequest updateRequest) {
        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        if (user == null || !Objects.equals(artworkPost.getUser().getId(), user.getId())) ErrorCd.NO_PERMISSION.serviceException();
        artworkPost.updatePost(updateRequest.status(), updateRequest.artworkField(), updateRequest.title(), updateRequest.explanation());
        artworkPostRepository.save(artworkPost);
    }

    public PaginationResponse<ArtworkPostModel> getPostList(User user, String sort, String keyword, Integer page, Integer size, String categoryName) {
        Category category = (categoryName == null) ? null : Category.valueOf(categoryName);
        String sortBy = ArtworkPostSort.valueOf(sort.toUpperCase()).getColumnName();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        if (keyword != null && (keyword = keyword.trim().replaceAll(" ", "")).isBlank()) keyword = null;

        Page<ArtworkPost> posts = artworkPostRepository.findByCategoryAndStatusAndTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCase(
                category,
                keyword,
                pageable
        );

        List<ArtworkPostModel> postModelList = posts.getContent().stream()
                .map((p) -> {
                            String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK, p.getId());
                            Boolean isLiked = isLiked(user, p);
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

    public ArtworkFolloingUserPostsResponse getFollowingUserPosts(User user) {
        if (user == null) throw ErrorCd.NO_PERMISSION.serviceException();
        List<User> userList = followRepository.findFollowingUsers(user);
        List<ArtworkPost> posts = artworkPostRepository.findAllByStatusAndUserInOrderByCreatedAtDesc(ArtworkPostStatus.PUBLIC,userList);

        List<FollowingUserModel> userModelList = userList.stream().map((u) -> {
            String imageUrl = null;
            if(u.getProfileImage()!=null) imageUrl = imageService.getImageUrl(ImageTargetType.PROFILE,u.getId());
            return ArtworkPostConverter.toFollowingUserModel(u, imageUrl);
        }).collect(Collectors.toList());

        Iterator<FollowingUserModel> iterator = userModelList.iterator();
        while (iterator.hasNext()) {
            FollowingUserModel u = iterator.next();
            int count = 0;
            // 작가에 해당하는 post 조회
            for (int i = 0; i < posts.size(); i++) {
                ArtworkPost post = posts.get(i);
                if (post.getUser().getId().equals(u.getUserId())) {
                    String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK,post.getId());
                    Boolean isLiked = isLiked(user, post);
                    u.getPosts().add(ArtworkPostConverter.toArtworkFollowingUserPostModel(post,imageUrl,isLiked));
                    count++;
                    if (count == 2) {
                        break;
                    }
                }
            }
            // 작품이 없다면 user 객체 삭제
            if (count == 0) iterator.remove();
        }

        sortByFirstPost(userModelList);
        return new ArtworkFolloingUserPostsResponse(userModelList);
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

    public List<ArtworkPost> getPostsByIds(List<Long> postIds) {
        return artworkPostRepository.findAllByIdIn(postIds);
    }

    public Boolean isLiked(User user,ArtworkPost post){
        return user != null && artworkLikeRepository.existsById(new ArtworkLikeId(user, post));
    }

    public ArtworkPost getPopularArtworkByUser(User user){
        return artworkPostRepository.findFirstByUserAndStatusOrderByLikeCountDesc(user, ArtworkPostStatus.PUBLIC).orElse(null);
    }

}
