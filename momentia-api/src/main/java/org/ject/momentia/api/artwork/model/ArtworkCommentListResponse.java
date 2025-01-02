package org.ject.momentia.api.artwork.model;
import lombok.Builder;
import java.util.List;

@Builder
public record ArtworkCommentListResponse (
        List<ArtworkCommentModel> comments
) {

}
