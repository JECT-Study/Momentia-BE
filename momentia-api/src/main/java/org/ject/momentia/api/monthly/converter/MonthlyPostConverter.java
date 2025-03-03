package org.ject.momentia.api.monthly.converter;

import org.ject.momentia.api.monthly.model.ExhibitionPostModel;
import org.ject.momentia.common.domain.artwork.ArtworkPost;

public class MonthlyPostConverter {
	public static ExhibitionPostModel toExhibitionPostModel(ArtworkPost artworkPost, String artworkImage) {
		return ExhibitionPostModel.builder()
			.postId(artworkPost.getId())
			.userId(artworkPost.getUser().getId())
			.postImage(artworkImage)
			.explanation(artworkPost.getExplanation())
			.title(artworkPost.getTitle())
			.nickname(artworkPost.getUser().getNickname())
			.build();
	}
}
