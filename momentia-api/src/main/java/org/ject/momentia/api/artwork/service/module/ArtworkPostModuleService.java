package org.ject.momentia.api.artwork.service.module;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.repository.cache.PageIdsCacheRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkPostModuleService {
	private final ArtworkPostRepository artworkPostRepository;
	private final PageIdsCacheRepository pageIdsCacheRepository;
	private final RedisTemplate<String, String> redisTemplate;

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

	public void deleteAllPageIdsCache() {
		List<String> idList2 = Optional.ofNullable(redisTemplate.opsForSet().members("PageIdsCacheModel"))
			.orElse(Collections.emptySet()) // null이면 빈 Set을 반환
			.stream()
			.map(String::valueOf)
			.toList();
		for (String id : idList2) {
			pageIdsCacheRepository.deleteById(id);
		}
	}
}
