package org.ject.momentia.api.artwork.model.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record ArtworkCommentListResponse(
	List<ArtworkCommentModel> comments
) {

}
