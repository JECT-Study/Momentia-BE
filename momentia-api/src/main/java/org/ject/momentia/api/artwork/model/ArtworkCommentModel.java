package org.ject.momentia.api.artwork.model;

import lombok.Builder;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDateTime;
@Builder
public record ArtworkCommentModel(
        Long commentId,
        Long userId,
        String profileImage,
        String content,
        LocalDateTime createdTime,
        boolean isMine
) {
}
