package org.ject.momentia.api.artwork.repository.cache.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "ArtworkLikeCacheModel") // Redis에 저장될 키 이름
public class ArtworkLikeCacheModel {
	@Id
	Long artworkId;

	private Map<Long, LocalDateTime> userLikes = new HashMap<>(); // <userId, 좋아요 시간>

	public void addLike(Long userId) {
		userLikes.put(userId, LocalDateTime.now()); // 현재 시간 저장
	}

	public void removeLike(Long userId) {
		userLikes.remove(userId);
	}

	public boolean hasLiked(Long userId) {
		return userLikes.containsKey(userId);
	}

	public Long getLikeCount() {
		return (long)this.userLikes.size();
	}
}
