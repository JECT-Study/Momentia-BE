package org.ject.momentia.api.artwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
public record ArtworkCommentListResponse (
        List<ArtworkCommentModel> comments
) {

}
