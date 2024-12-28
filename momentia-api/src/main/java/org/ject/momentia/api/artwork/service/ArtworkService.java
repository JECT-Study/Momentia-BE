package org.ject.momentia.api.artwork.service;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.global.pagination.converter.PaginationConverter;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.api.global.pagination.model.PaginationModel;
import org.ject.momentia.api.global.pagination.model.PaginationResponse;
import org.ject.momentia.api.artwork.repository.ArtworkCommentRepository;
import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
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
    private final ArtworkCommentRepository artworkCommentRepository;

    public ArtworkPostIdResponse createPost(User user, ArtworkPostCreateRequest artworkPostCreateRequest) {
        ArtworkPost artworkPost = ArtworkPostConverter.toArtworPost(artworkPostCreateRequest, user);

        /// Todo : image 처리
        artworkPost = artworkPostRepository.save(artworkPost);

        return ArtworkPostConverter.toArtworkPostIdResponse(artworkPost);
    }

    public ArtworkPostResponse getPost(User user, Long postId) {
        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);

        boolean isMine = false;
        boolean isLiked = false;
        if (user != null) {
            Long userId = user.getId();
            isLiked = artworkLikeRepository.existsById(new ArtworkLikeId(user, artworkPost));
            isMine = userId.equals(artworkPost.getUser().getId());
        }

        ///  Todo : imageId -> url 로 변환
        ///  Todo : 조회수 처리

        return ArtworkPostConverter.toArtworkPostResponse(artworkPost, isMine, isLiked, "empty");
    }

    public void deletePost(User user, Long postId) {
        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        if (user != null && user.getId() != artworkPost.getUser().getId()) ErrorCd.NO_PERMISSION.serviceException();
        artworkPostRepository.delete(artworkPost);
    }

    public void updatePost(User user, Long postId, ArtworkPostUpdateRequest updateRequest) {
        ArtworkPost artworkPost = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        if (user == null || artworkPost.getUser().getId() != user.getId()) ErrorCd.NO_PERMISSION.serviceException();
        artworkPost.updatePost(updateRequest.status(), updateRequest.artworkField(), updateRequest.title(), updateRequest.explanation());
        artworkPostRepository.save(artworkPost);
    }

    public PaginationResponse<ArtworkPostModel> getPostList(User user, String sort, String keyword, Integer page, Integer size, String categoryName) {
        Category category = (categoryName == null) ? null : Category.valueOf(categoryName);
        String sortBy = sort.equals("popular") ? "likeCount" : sort.equals("view") ? "view" : "createdAt";

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ArtworkPost> posts = artworkPostRepository.findByCategoryAndStatusAndTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCase(
                category,
                keyword,
                pageable
        );

        List<ArtworkPostModel> postModelList = posts.getContent().stream()
                .map((p) -> {
                            Boolean isLiked = user != null && artworkLikeRepository.existsById(new ArtworkLikeId(user, p));
                            return ArtworkPostConverter.toArtworkPostModel(p, isLiked, "empty");
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
        List<ArtworkPost> posts = artworkPostRepository.findAllByUserInOrderByCreatedAtDesc(userList);

        List<FollowingUserModel> userModelList = userList.stream().map((u) -> {
            /// todo : 이미지 처리
            return ArtworkPostConverter.toFollowingUserModel(u, "empty");
        }).collect(Collectors.toList());

        Iterator<FollowingUserModel> iterator = userModelList.iterator();
        while (iterator.hasNext()) {
            FollowingUserModel u = iterator.next();
            int count = 0;
            // 작가에 해당하는 post 조회
            for (int i = 0; i < posts.size(); i++) {
                ArtworkPost post = posts.get(i);
                if (post.getUser().getId() == u.getUserId()) {
                    // todo : 이미지 처리
                    u.getPosts().add(ArtworkPostConverter.toArtworkFollowingUserPostModel(post,"empty"));
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
