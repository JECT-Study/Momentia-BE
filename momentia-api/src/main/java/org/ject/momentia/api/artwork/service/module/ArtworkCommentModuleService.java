package org.ject.momentia.api.artwork.service.module;

import org.ject.momentia.api.artwork.repository.jpa.ArtworkCommentRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkCommentModuleService {
	private final ArtworkCommentRepository artworkCommentRepository;

	public void deleteAllByPost(ArtworkPost artworkPost) {
		artworkCommentRepository.deleteAllByPost(artworkPost);
	}
}
