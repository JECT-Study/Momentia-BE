package org.ject.momentia.api.artwork.service.module;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkLikeCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkLikeCacheModel;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkLikeModuleService {
	private final ArtworkLikeRepository artworkLikeRepository;
	private final ArtworkLikeCacheRepository artworkLikeCacheRepository;
	private final ArtworkPostRepository artworkPostRepository;
	private final RedisTemplate<String, String> redisTemplate;

	public Boolean isLiked(User user, ArtworkPost post) {
		if (user == null)
			return false;
		// Redis에서 확인
		Optional<ArtworkLikeCacheModel> cache = artworkLikeCacheRepository.findById(post.getId());
		if (cache.isPresent() && cache.get().hasLiked(user.getId())) {
			return true;
		}
		return artworkLikeRepository.existsById(new ArtworkLikeId(user, post));
	}

	// public Boolean isLikedByPostId(User user, Long postId) {
	// 	return user != null && artworkLikeRepository.existsByPostIdAndUserId(postId, user);
	// }

	/**
	 * 특정 유저가 특정 게시글에 좋아요를 눌렀는지 확인 (postId 기반)
	 */
	public Boolean isLikedByPostId(User user, Long postId) {
		if (user == null) {
			return false;
		}

		// Redis에서 확인
		Optional<ArtworkLikeCacheModel> cache = artworkLikeCacheRepository.findById(postId);
		if (cache.isPresent() && cache.get().hasLiked(user.getId())) {
			return true;
		}
		// 없으면 DB 조회
		return artworkLikeRepository.existsByPostIdAndUserId(postId, user);
	}

	public void deleteAllByArtwork(ArtworkPost artworkPost) {
		// 캐시에서도 삭제
		artworkLikeCacheRepository.deleteById(artworkPost.getId());
		artworkLikeRepository.deleteAllByArtwork(artworkPost);
	}

	// public List<Long> getIdList(User user) {
	// 	List<ArtworkLike> likeList = artworkLikeRepository.findByUser(user);
	// 	return likeList.stream().map((l) -> {
	// 		return l.getId().getPost().getId();
	// 	}).collect(Collectors.toList());
	// }

	public List<Long> getIdList(User user) {
		if (user == null) {
			return Collections.emptyList();
		}

		// 1Redis에서 가져오기
		List<Long> redisLikedPostIds = getLikedPostIds(user.getId());
		// StreamSupport.stream(
		// 	artworkLikeCacheRepository.findAll().spliterator(), false) // false = 순차 스트림
		// .filter(cache -> cache.hasLiked(user.getId()))
		// .map(ArtworkLikeCacheModel::getArtworkId)
		// .collect(Collectors.toList());

		// DB에서 가져오기
		List<Long> dbLikedPostIds = artworkLikeRepository.findByUser(user).stream()
			.map(like -> like.getId().getPost().getId())
			.collect(Collectors.toList());

		// Redis + DB 데이터 합쳐서 중복 제거
		return Stream.concat(redisLikedPostIds.stream(), dbLikedPostIds.stream())
			.distinct()
			.collect(Collectors.toList());
	}

	private List<Long> getLikedPostIds(Long userId) {
		// Redis에서 모든 좋아요된 게시물 ID 가져오기
		Set<String> idSet = redisTemplate.opsForSet().members("ArtworkLikeCacheModel");

		if (idSet == null || idSet.isEmpty()) {
			return Collections.emptyList();
		}

		// 유저가 좋아요한 게시물만 필터링
		return idSet.stream()
			.map(id -> artworkLikeCacheRepository.findById(Long.valueOf(id))) // Optional<ArtworkLikeCacheModel>
			.flatMap(Optional::stream) // Optional을 스트림으로 변환하여 값이 있는 경우만 처리
			.filter(cache -> cache.hasLiked(userId)) // 유저가 좋아요한 게시물만 필터링
			.map(ArtworkLikeCacheModel::getArtworkId) // 게시물 ID 가져오기
			.toList();
	}

	public Long getLikeCountRDBAndCacheSum(Long postId) {
		ArtworkLikeCacheModel likeCacheModel = artworkLikeCacheRepository.findById(postId).orElse(null);
		if (likeCacheModel == null) {
			return 0L;
		}
		return likeCacheModel.getLikeCount();
	}

}
