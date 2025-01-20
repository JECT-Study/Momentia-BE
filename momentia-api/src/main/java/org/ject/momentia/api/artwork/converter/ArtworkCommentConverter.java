package org.ject.momentia.api.artwork.converter;

import org.ject.momentia.api.artwork.model.ArtworkCommentModel;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.user.User;

public class ArtworkCommentConverter {

	public static ArtworkCommentModel ArtworkCommentToArtworkCommentModel(ArtworkComment comment, String imageUrl,
		User user) {
		boolean isMine = false;
		if (user != null)
			isMine = comment.getUser().getId().equals(user.getId());
		return ArtworkCommentModel.builder()
			.commentId(comment.getId())
			.userId(comment.getUser().getId())
			.content(comment.getContent())
			.isMine(isMine)
			.profileImage(imageUrl)
			.createdTime(comment.getCreatedAt())
			.nickname(comment.getUser().getNickname())
			.build();
	}
}
