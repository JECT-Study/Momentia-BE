package org.ject.momentia.api.notification.type;

import java.util.function.Function;

import org.ject.momentia.api.notification.entity.NotificationSetting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	LIKE_ON_MY_ARTWORK(0, NotificationSetting::getLikeOnMyArtwork),                // 내 작품에 좋아요 달림
	COMMENT_ON_MY_ARTWORK(1, NotificationSetting::getCommentOnMyArtwork),            // 내 작품에 댓글 달림
	FOLLOWED_BY_USER(2, NotificationSetting::getFollowedByUser),                // 사용자가 나를 팔로우함
	POPULAR_ARTWORK(3, NotificationSetting::getPopularArtwork),                // 인기 작품 선정
	ARTIST_OF_THE_MONTH(4, NotificationSetting::getArtistOfTheMonth),            // 이달의 작가 선정
	LIKE_ON_COMMUNITY_POST(5, NotificationSetting::getLikeOnCommunityPost),            // 커뮤니티 게시물에 좋아요 달림
	COMMENT_ON_COMMUNITY_POST(6, NotificationSetting::getCommentOnCommunityPost),        // 커뮤니티 게시물에 댓글 달림
	COMMENT_ON_REPLIED_POST(7, NotificationSetting::getCommentOnRepliedPost);            // 내가 댓글을 단 게시물에 답글이 달림

	private final int notificationIdx;
	private final Function<NotificationSetting, Boolean> optionGetter;
}
