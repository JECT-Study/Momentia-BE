package org.ject.momentia.api.artwork.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ject.momentia.api.artwork.model.*;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.user.User;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtworkPostConverter {
    public static ArtworkPost toArtworPost(ArtworkPostCreateRequest artworkPostCreateRequest, User user) {
        return ArtworkPost.builder()
                .id(null)
                .user(user)
                .postImage(artworkPostCreateRequest.postImage())
                .category(Category.valueOf(artworkPostCreateRequest.artworkField()))
                .title(artworkPostCreateRequest.title())
                .explanation(artworkPostCreateRequest.explanation())
                .likeCount(0L)
                .commentCount(0L)
                .viewCount(0L)
                .status(ArtworkPostStatus.valueOf(artworkPostCreateRequest.status()))
                .build();
    }

    public static ArtworkPostIdResponse toArtworkPostIdResponse(ArtworkPost artworkPost) {
        return new ArtworkPostIdResponse(artworkPost.getId());
    }

    public static ArtworkPostResponse toArtworkPostResponse(ArtworkPost artworkPost, boolean isMine, boolean isLiked, String imageUrl) {
        return ArtworkPostResponse.builder()
                .postId(artworkPost.getId())
                .postImage(imageUrl)
                .explanation(artworkPost.getExplanation())
                .title(artworkPost.getTitle())
                .view(artworkPost.getViewCount())
                .likeCount(artworkPost.getLikeCount())
                .createdTime(artworkPost.getCreatedAt())
                .userId(artworkPost.getUser().getId())
                .isMine(isMine)
                .isLiked(isLiked)
                .category(artworkPost.getCategory().name())
                .build();
    }

    public static ArtworkComment from(ArtworkCommentRequest artworkCommentCreateRequest, ArtworkPost post, User user) {
        return ArtworkComment.builder()
                .content(artworkCommentCreateRequest.content())
                .post(post)
                .user(user)
                .build();
    }

    public static ArtworkPostModel toArtworkPostModel(ArtworkPost artworkPost, Boolean isLiked, String imageUrl) {
        return ArtworkPostModel.builder()
                .postId(artworkPost.getId())
                .postImage(imageUrl)
                .view(artworkPost.getViewCount())
                .nickname(artworkPost.getUser().getNickname())
                .likeCount(artworkPost.getLikeCount())
                .isLiked(isLiked) //
                .likeCount(artworkPost.getLikeCount())
                .title(artworkPost.getTitle())
                .userId(artworkPost.getUser().getId())
                .build();
    }

    public static FollowingUserModel toFollowingUserModel(User user, String imageUrl) {
        return FollowingUserModel.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .userImage(imageUrl)
                .isFollow(true)
                .userField(user.getField() == null ? null : user.getField().name())
                .posts(new ArrayList<>())
                .build();
    }


    public static ArtworkFollowingUserPostModel toArtworkFollowingUserPostModel(
            ArtworkPost artworkPost, String postImage) {
        return ArtworkFollowingUserPostModel.builder()
                .postId(artworkPost.getId())
                .title(artworkPost.getTitle())
                .commentCount(artworkPost.getCommentCount())
                .likeCount(artworkPost.getLikeCount())
                .postImage(postImage)
                .viewCount(artworkPost.getViewCount())
                .createdTime(artworkPost.getCreatedAt())
                .build();
    }


}
