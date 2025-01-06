package org.ject.momentia.api.artwork.service.module;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.repository.ArtworkCommentRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArtworkCommentModuleService {
    private final ArtworkCommentRepository artworkCommentRepository;

    public void deleteAllByPost(ArtworkPost artworkPost) {
        artworkCommentRepository.deleteAllByPost(artworkPost);
    }
}
