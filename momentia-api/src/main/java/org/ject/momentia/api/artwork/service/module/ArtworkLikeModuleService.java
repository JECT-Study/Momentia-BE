package org.ject.momentia.api.artwork.service.module;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkLikeCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkLikeCacheModel;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkLikeModuleService {
	private final ArtworkLikeRepository artworkLikeRepository;
	private final ArtworkLikeCacheRepository artworkLikeCacheRepository;

	// public Boolean isLiked(User user, ArtworkPost post) {
	// 	return user != null && artworkLikeRepository.existsById(new ArtworkLikeId(user, post));
	// }
	public Boolean isLiked(User user, ArtworkPost post) {
		if (user == null) {
			return false;
		}

		// Redis에서 확인
		Optional<ArtworkLikeCacheModel> cache = artworkLikeCacheRepository.findById(post.getId());
		if (cache.isPresent() && cache.get().hasLiked(user.getId())) {
			return true;
		}

		// 없으면 DB 조회
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
		List<Long> redisLikedPostIds = StreamSupport.stream(
				artworkLikeCacheRepository.findAll().spliterator(), false) // false = 순차 스트림
			.filter(cache -> cache.hasLiked(user.getId()))
			.map(ArtworkLikeCacheModel::getArtworkId)
			.collect(Collectors.toList());

		// DB에서 가져오기 (Redis 데이터가 부족할 수 있으므로 보완)
		List<Long> dbLikedPostIds = artworkLikeRepository.findByUser(user).stream()
			.map(like -> like.getId().getPost().getId())
			.collect(Collectors.toList());

		// Redis + DB 데이터 합쳐서 중복 제거
		return Stream.concat(redisLikedPostIds.stream(), dbLikedPostIds.stream())
			.distinct()
			.collect(Collectors.toList());
	}
}
