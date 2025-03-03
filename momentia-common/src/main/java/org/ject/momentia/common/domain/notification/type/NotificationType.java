package org.ject.momentia.common.domain.notification.type;

import java.util.function.Function;

import org.ject.momentia.common.domain.notification.NotificationSetting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	LIKE_ON_MY_ARTWORK(0, NotificationSetting::getLikeOnMyArtwork,
		NotificationBaseType.ACTION_ON_ARTWORK),                // 내 작품에 좋아요 달림
	COMMENT_ON_MY_ARTWORK(1, NotificationSetting::getCommentOnMyArtwork,
		NotificationBaseType.ACTION_ON_ARTWORK),            // 내 작품에 댓글 달림
	FOLLOWED_BY_USER(2, NotificationSetting::getFollowedByUser,
		NotificationBaseType.FOLLOW),                // 사용자가 나를 팔로우함
	POPULAR_ARTWORK(3, NotificationSetting::getPopularArtwork,
		NotificationBaseType.PICKED_USER),                // 인기 작품 선정
	ARTIST_OF_THE_MONTH(4, NotificationSetting::getArtistOfTheMonth,
		NotificationBaseType.PICKED_ARTWORK),            // 이달의 작가 선정
	LIKE_ON_COMMUNITY_POST(5, NotificationSetting::getLikeOnCommunityPost,
		NotificationBaseType.UNUSED),            // 커뮤니티 게시물에 좋아요 달림
	COMMENT_ON_COMMUNITY_POST(6, NotificationSetting::getCommentOnCommunityPost,
		NotificationBaseType.UNUSED),        // 커뮤니티 게시물에 댓글 달림
	COMMENT_ON_REPLIED_POST(7, NotificationSetting::getCommentOnRepliedPost,
		NotificationBaseType.UNUSED);            // 내가 댓글을 단 게시물에 답글이 달림

	private final int notificationIdx;
	private final Function<NotificationSetting, Boolean> optionGetter;
	private final NotificationBaseType notificationBaseType;
}
