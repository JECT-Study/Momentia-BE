package org.ject.momentia.api.artwork.converter;

import java.util.ArrayList;

import org.ject.momentia.api.artwork.model.ArtworkCommentRequest;
import org.ject.momentia.api.artwork.model.ArtworkFollowingUserPostModel;
import org.ject.momentia.api.artwork.model.ArtworkPostCreateRequest;
import org.ject.momentia.api.artwork.model.ArtworkPostIdResponse;
import org.ject.momentia.api.artwork.model.ArtworkPostModel;
import org.ject.momentia.api.artwork.model.ArtworkPostResponse;
import org.ject.momentia.api.artwork.model.FollowingUserModel;
import org.ject.momentia.api.artwork.model.FollowingUserPostProjection;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkPostCacheModel;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.user.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtworkPostConverter {

	public static ArtworkPost toArtworkPost(ArtworkPostCreateRequest artworkPostCreateRequest, User user) {
		return ArtworkPost.builder()
			.id(null)
			.user(user)
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

	public static ArtworkPostResponse toArtworkPostResponse(ArtworkPost artworkPost, boolean isMine, boolean isLiked,
		String imageUrl, Boolean isFollow, Long addLikeCount) {
		return ArtworkPostResponse.builder()
			.postId(artworkPost.getId())
			.postImage(imageUrl)
			.explanation(artworkPost.getExplanation())
			.title(artworkPost.getTitle())
			.viewCount(artworkPost.getViewCount())
			.likeCount(artworkPost.getLikeCount() + addLikeCount)
			.commentCount(artworkPost.getCommentCount())
			.createdTime(artworkPost.getCreatedAt())
			.artworkField(artworkPost.getCategory().getKoreanName())
			.status(artworkPost.getStatus().name())
			.userId(artworkPost.getUser().getId())
			.userField(
				artworkPost.getUser().getField() == null ? null : artworkPost.getUser().getField().getKoreanName())
			.nickname(artworkPost.getUser().getNickname())
			.introduction(artworkPost.getUser().getIntroduction())
			.isFollow(isFollow)
			.profileImage(artworkPost.getUser().getProfileImage() == null ? null :
				artworkPost.getUser().getProfileImage().getImageSrc())
			.isMine(isMine)
			.isLiked(isLiked)
			.build();
	}

	public static ArtworkComment from(ArtworkCommentRequest artworkCommentCreateRequest, ArtworkPost post, User user) {
		return ArtworkComment.builder()
			.content(artworkCommentCreateRequest.content())
			.post(post)
			.user(user)
			.build();
	}

	public static ArtworkPostModel toArtworkPostModel(ArtworkPost artworkPost, Boolean isLiked, String imageUrl,
		Long addLikeCount) {
		return ArtworkPostModel.builder()
			.postId(artworkPost.getId())
			.postImage(imageUrl)
			.viewCount(artworkPost.getViewCount())
			.nickname(artworkPost.getUser().getNickname())
			.likeCount(artworkPost.getLikeCount())
			.isLiked(isLiked)
			.likeCount(artworkPost.getLikeCount() + addLikeCount)
			.title(artworkPost.getTitle())
			.userId(artworkPost.getUser().getId())
			.commentCount(artworkPost.getCommentCount())
			.status(artworkPost.getStatus().name())
			.build();
	}

	public static FollowingUserModel toFollowingUserModel(User user) {
		return FollowingUserModel.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.userImage(user.getProfileImage() == null ? null : user.getProfileImage().getImageSrc())
			.isFollow(true) // 팔로잉한 유저에서 가져온 데이터 이므로 무조건 true로 반환
			.userField(user.getField() == null ? null : user.getField().getKoreanName())
			.posts(new ArrayList<>())
			.build();
	}

	public static ArtworkFollowingUserPostModel toArtworkFollowingUserPostModel(
		FollowingUserPostProjection artworkPost, String postImage, Boolean isLiked) {
		return ArtworkFollowingUserPostModel.builder()
			.postId(artworkPost.getId())
			.title(artworkPost.getTitle())
			.commentCount(artworkPost.getCommentCount())
			.likeCount(artworkPost.getLikeCount())
			.postImage(postImage)
			.viewCount(artworkPost.getViewCount())
			.isLiked(isLiked)
			.createdTime(artworkPost.getCreatedAt())
			.build();
	}

	public static ArtworkPostModel ArtworkPostCacheModeltoArtworkPostModel(ArtworkPostCacheModel artworkPostCacheModel,
		Boolean isLiked, String nickname, Long addLikeCount) {
		return ArtworkPostModel.builder()
			.postId(artworkPostCacheModel.getId())
			.title(artworkPostCacheModel.getTitle())
			.postImage(artworkPostCacheModel.getImageUrl())
			.userId(artworkPostCacheModel.getUserId())
			.nickname(nickname)
			.viewCount(artworkPostCacheModel.getViewCount())
			.commentCount(artworkPostCacheModel.getCommentCount())
			.likeCount(artworkPostCacheModel.getLikeCount() + addLikeCount)
			.isLiked(isLiked)
			.status(artworkPostCacheModel.getStatus().name())
			.build();

	}
}
