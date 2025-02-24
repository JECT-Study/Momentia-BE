package org.ject.momentia.api.artwork.service;

import java.util.HashMap;

import org.ject.momentia.api.artwork.model.cache.ArtworkLikeCacheModel;
import org.ject.momentia.api.artwork.repository.cache.ArtworkLikeCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.ArtworkPostCacheRepository;
import org.ject.momentia.api.artwork.repository.cache.PageIdsCacheRepository;
import org.ject.momentia.api.artwork.repository.jpa.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.jpa.ArtworkPostRepository;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.exception.ErrorCd;
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
public class ArtworkLikeService {

	private final ArtworkPostModuleService artworkService;
	private final ArtworkLikeRepository artworkLikeRepository;

	private final ArtworkLikeCacheRepository artworkLikeCacheRepository;
	private final UserRepository userRepository;
	private final ArtworkPostRepository artworkPostRepository;
	private final PageIdsCacheRepository pagesIdsCacheRepository;
	private final ArtworkPostCacheRepository artworkPostCacheRepository;

	@Transactional
	public void like(User user, Long postId) {
		ArtworkPost post = artworkService.findPostByIdElseThrowError(postId);
		ArtworkLikeId id = new ArtworkLikeId(user, post);
		if (artworkLikeRepository.findById(id).isPresent())
			throw ErrorCd.ALREADY_lIKE.serviceException();
		//artworkLikeRepository.save(new ArtworkLike(id, LocalDateTime.now(), LocalDateTime.now()));

		// 캐시에서 작품 id로 좋아요 데이터 검색
		ArtworkLikeCacheModel likeCacheModel = artworkLikeCacheRepository.findById(postId).orElse(null);
		if (likeCacheModel == null) {
			likeCacheModel = new ArtworkLikeCacheModel(postId, new HashMap<>());
		}
		if (likeCacheModel.hasLiked(user.getId())) {
			throw ErrorCd.ALREADY_lIKE.serviceException();
		}

		likeCacheModel.addLike(user.getId());

		artworkLikeCacheRepository.save(likeCacheModel);
		// if (artworkPostCacheRepository.findById(post.getId()).isPresent()) {
		// 	ArtworkPostCacheModel artworkPostCacheModel = artworkPostCacheRepository.findById(post.getId()).get();
		// 	artworkPostCacheModel.increaseLikeCount();
		// 	artworkPostCacheRepository.save(artworkPostCacheModel);
		// }

	}

	// @Transactional
	// public void unlike(User user, Long postId) {
	// 	ArtworkPost post = artworkService.findPostByIdElseThrowError(postId);
	// 	ArtworkLikeId id = new ArtworkLikeId(user, post);
	// 	ArtworkLike like = artworkLikeRepository.findById(id)
	// 		.orElseThrow(ErrorCd.LIKE_ALREADY_REMOVED::serviceException);
	// 	artworkLikeRepository.delete(like);
	// 	post.decreaseLikeCount();
	// }
	//

	@Transactional
	public void unlike(User user, Long postId) {
		// 캐시에서 해당 게시글의 좋아요 정보를 찾기
		ArtworkLikeCacheModel likeCacheModel = artworkLikeCacheRepository.findById(postId).orElse(null);

		// 캐시에서 좋아요 정보가 있다면, 캐시에서 삭제하고 바로 반환
		if (likeCacheModel != null && likeCacheModel.hasLiked(user.getId())) {
			likeCacheModel.removeLike(user.getId()); // 캐시에서 사용자 좋아요 제거
			artworkLikeCacheRepository.save(likeCacheModel); // 캐시 업데이트
			return; // 캐시에서 삭제했으므로 바로 반환
		}

		// 캐시에서 좋아요 정보가 없으면 DB에서 삭제 진행
		ArtworkPost post = artworkService.findPostByIdElseThrowError(postId);
		ArtworkLikeId id = new ArtworkLikeId(user, post);

		// DB에서 ArtworkLike 삭제
		ArtworkLike like = artworkLikeRepository.findById(id)
			.orElseThrow(ErrorCd.LIKE_ALREADY_REMOVED::serviceException);

		artworkLikeRepository.delete(like);

		// DB에서 좋아요 수 감소
		post.decreaseLikeCount();
		artworkPostCacheRepository.deleteById(post.getId());

		// // 게시글 정보 DB에 반영 (좋아요 수 감소 후 업데이트)
		artworkPostRepository.save(post);
	}

}
