package org.ject.momentia.common.domain.notification;

import org.ject.momentia.common.domain.notification.type.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "notification_setting", schema = "momentia")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationSetting {
	@Id
	@Column(name = "user_id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "like_on_my_artwork", nullable = false)
	private Boolean likeOnMyArtwork = false;

	@NotNull
	@Column(name = "comment_on_my_artwork", nullable = false)
	private Boolean commentOnMyArtwork = false;

	@NotNull
	@Column(name = "followed_by_user", nullable = false)
	private Boolean followedByUser = false;

	@NotNull
	@Column(name = "popular_artwork", nullable = false)
	private Boolean popularArtwork = false;

	@NotNull
	@Column(name = "artist_of_the_month", nullable = false)
	private Boolean artistOfTheMonth = false;

	@NotNull
	@Column(name = "like_on_community_post", nullable = false)
	private Boolean likeOnCommunityPost = false;

	@NotNull
	@Column(name = "comment_on_community_post", nullable = false)
	private Boolean commentOnCommunityPost = false;

	@NotNull
	@Column(name = "comment_on_replied_post", nullable = false)
	private Boolean commentOnRepliedPost = false;

	// fallback code: 이전 테스트용 코드의 경우는 유저의 설정이 존재하지 않을수 있음
	public static NotificationSetting createSetting(Long userId) {
		return NotificationSetting.builder()
			.id(userId)
			.likeOnMyArtwork(true)
			.commentOnMyArtwork(true)
			.followedByUser(true)
			.popularArtwork(true)
			.artistOfTheMonth(true)
			.likeOnCommunityPost(true)
			.commentOnCommunityPost(true)
			.commentOnRepliedPost(true)
			.build();
	}

	public void updateSetting(NotificationType type, boolean value) {
		switch (type) {
			case NotificationType.LIKE_ON_MY_ARTWORK -> likeOnMyArtwork = value;
			case NotificationType.COMMENT_ON_MY_ARTWORK -> commentOnMyArtwork = value;
			case NotificationType.FOLLOWED_BY_USER -> followedByUser = value;
			case NotificationType.POPULAR_ARTWORK -> popularArtwork = value;
			case NotificationType.ARTIST_OF_THE_MONTH -> artistOfTheMonth = value;
			case NotificationType.LIKE_ON_COMMUNITY_POST -> likeOnCommunityPost = value;
			case NotificationType.COMMENT_ON_COMMUNITY_POST -> commentOnCommunityPost = value;
			case NotificationType.COMMENT_ON_REPLIED_POST -> commentOnRepliedPost = value;
		}
	}
}