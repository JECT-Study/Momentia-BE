package org.ject.momentia.api.artwork.service.module;

import java.util.List;

import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkPostModuleService {
	private final ArtworkPostRepository artworkPostRepository;

	public List<ArtworkPost> getPostsByIds(List<Long> postIds) {
		return artworkPostRepository.findAllByIdIn(postIds);
	}

	public List<ArtworkPost> getPostsByIdsAndStatus(List<Long> postIds, ArtworkPostStatus status) {
		return artworkPostRepository.findAllByIdInAndStatus(postIds, status);
	}

	public ArtworkPost getPopularArtworkByUser(User user) {
		return artworkPostRepository.findFirstByUserAndStatusOrderByLikeCountDesc(user, ArtworkPostStatus.PUBLIC)
			.orElse(null);
	}

	public ArtworkPost findPostByIdElseThrowError(Long postId) {
		return artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
	}

	public ArtworkPost findPostByIdElseReturnNull(Long postId) {
		return artworkPostRepository.findById(postId).orElse(null);
	}
}
