package org.ject.momentia.api.artwork.repository.cache.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "ArtworkViewCacheModel")
public class ArtworkViewCacheModel {
	@Id
	Long artworkId;

	Long view;

	public ArtworkViewCacheModel(Long artworkId) {
		this.artworkId = artworkId;
		view = 0L;
	}

	public void increaseView() {
		view++;
	}
}
