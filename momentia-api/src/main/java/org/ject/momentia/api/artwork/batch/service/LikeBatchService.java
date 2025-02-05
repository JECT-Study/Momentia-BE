package org.ject.momentia.api.artwork.batch.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkLikeCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkPostCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.PageIdsCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.model.ArtworkLikeCacheModel;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeBatchService {

	private final ArtworkPostModuleService artworkService;
	private final ArtworkLikeRepository artworkLikeRepository;

	private final ArtworkLikeCacheRepository artworkLikeCacheRepository;
	private final UserRepository userRepository;
	private final ArtworkPostRepository artworkPostRepository;
	private final PageIdsCacheRepository pagesIdsCacheRepository;
	private final ArtworkPostCacheRepository artworkPostCacheRepository;

	@Transactional
	public void migrateLikesToDB() {
		// Redis에서 좋아요 데이터를 가져옴
		List<ArtworkLikeCacheModel> likeCacheModels = (List<ArtworkLikeCacheModel>)artworkLikeCacheRepository.findAll();

		// 각 캐시 데이터를 DB로 옮기기
		for (ArtworkLikeCacheModel cacheModel : likeCacheModels) {
			// Redis에서 데이터를 가져와서 DB에 저장
			saveLikeToDatabase(cacheModel);
		}
	}

	private void saveLikeToDatabase(ArtworkLikeCacheModel cacheModel) {
		// DB에 저장할 ArtworkLike 엔티티 생성
		ArtworkPost artworkPost = artworkService.findPostByIdElseReturnNull(cacheModel.getArtworkId());
		Map<Long, LocalDateTime> userLikes = cacheModel.getUserLikes();

		for (Map.Entry<Long, LocalDateTime> entry : userLikes.entrySet()) {
			Long userId = entry.getKey();
			LocalDateTime likeTime = entry.getValue(); // 좋아요 시간을 얻음

			// 사용자가 좋아요를 눌렀다면, DB에 삽입
			User user = userRepository.findById(userId).orElse(null);
			if (user == null) {
				continue;
			} // -> 알려줘야 할거 같은데..

			ArtworkLikeId artworkLikeId = new ArtworkLikeId(user, artworkPost);

			// 이미 DB에 존재하는지 확인하고 없으면 저장
			if (artworkLikeRepository.findById(artworkLikeId).isEmpty()) {
				// ArtworkLike 엔티티를 생성하여 저장
				ArtworkLike like = new ArtworkLike(artworkLikeId, likeTime, likeTime);
				artworkLikeRepository.save(like);

				artworkPost.increaseLikeCount();
			}
		}
		artworkPostRepository.save(artworkPost);
		artworkPostCacheRepository.deleteById(artworkPost.getId());
		artworkLikeCacheRepository.deleteById(cacheModel.getArtworkId());
		pagesIdsCacheRepository.deleteAll();
	}
}
