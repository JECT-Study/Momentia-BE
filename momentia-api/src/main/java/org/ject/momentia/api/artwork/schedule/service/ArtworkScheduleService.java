package org.ject.momentia.api.artwork.schedule.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkLikeCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkPostCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkViewCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.PageIdsCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkLikeCacheModel;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkScheduleService {

	private final ArtworkPostModuleService artworkService;
	private final ArtworkLikeRepository artworkLikeRepository;

	private final ArtworkLikeCacheRepository artworkLikeCacheRepository;
	private final UserRepository userRepository;
	private final ArtworkPostRepository artworkPostRepository;
	private final PageIdsCacheRepository pagesIdsCacheRepository;
	private final ArtworkPostCacheRepository artworkPostCacheRepository;
	private final ArtworkViewCacheRepository artworkViewCacheRepository;
	private final ArtworkPostModuleService artworkPostModuleService;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public void migrateLikesToDB() {
		// Redis에서 좋아요 데이터를 가져옴
		Set<String> idSet = redisTemplate.opsForSet().members("ArtworkLikeCacheModel");

		// 작품 id 리스트 가져옴
		List<Long> idList = (idSet == null || idSet.isEmpty())
			? Collections.emptyList()
			: idSet.stream()
			.map(Long::valueOf)
			.toList();

		for (Long id : idList) {
			artworkLikeCacheRepository.findById(id).ifPresent(this::saveLikeToDatabase);
		}
		//artworkPostModuleService.deleteAllPageIdsCache();
	}

	@Transactional
	public void migrateViewToDB() {
		Set<String> idSet = redisTemplate.opsForSet().members("ArtworkViewCacheModel");

		List<Long> idList = (idSet == null || idSet.isEmpty())
			? Collections.emptyList()
			: idSet.stream()
			.map(Long::valueOf)
			.toList();

		for (Long id : idList) {
			artworkViewCacheRepository.findById(id).ifPresent(artworkView -> {
				ArtworkPost artwork = artworkPostRepository.findById(artworkView.getArtworkId()).orElse(null);
				if (artwork != null) {
					artwork.increaseViewCount(artworkView.getView());
				}
			});
			artworkViewCacheRepository.deleteById(id);
			artworkPostCacheRepository.deleteById(id);
		}
	}

	@Transactional
	public void deleteAllPostIds() {
		artworkPostModuleService.deleteAllPageIdsCache();
	}

	void saveLikeToDatabase(ArtworkLikeCacheModel cacheModel) {
		// DB에 저장할 ArtworkLike 엔티티 생성
		ArtworkPost artworkPost = artworkPostRepository.findById(cacheModel.getArtworkId()).orElse(null);
		Map<Long, LocalDateTime> userLikes = cacheModel.getUserLikes();
		if (artworkPost != null) {
			for (Map.Entry<Long, LocalDateTime> entry : userLikes.entrySet()) {
				Long userId = entry.getKey();
				LocalDateTime likeTime = entry.getValue(); // 좋아요 시간을 얻음

				// 사용자가 좋아요를 눌렀다면, DB에 삽입
				User user = userRepository.findById(userId).orElse(null);
				if (user == null)
					continue;

				ArtworkLikeId artworkLikeId = new ArtworkLikeId(user, artworkPost);
				// 이미 DB에 존재하는지 확인하고 없으면 저장
				if (!artworkLikeRepository.existsById(artworkLikeId)) {
					ArtworkLike like = new ArtworkLike(artworkLikeId, likeTime, likeTime);
					artworkLikeRepository.save(like);
					artworkPost.increaseLikeCount();
				}
			}
			artworkPostRepository.save(artworkPost);
			artworkPostCacheRepository.deleteById(artworkPost.getId());
		}
		artworkLikeCacheRepository.deleteById(cacheModel.getArtworkId());
	}
}
