package org.ject.momentia.api.monthly.model;

import lombok.Builder;

@Builder
public record ExhibitionPostModel(
	Long postId,
	String title,
	String explanation,
	String postImage,
	Long userId,
	String nickname
) {
}
