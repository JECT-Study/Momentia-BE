package org.ject.momentia.api.artwork.model.cache;

import java.io.Serializable;

import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "ArtworkPostCacheModel", timeToLive = 1000L) // Redis에 저장될 키 이름
public class ArtworkPostCacheModel implements Serializable {

	@Id
	private Long id;

	private String metadata;  // 모든 필드를 하나의 문자열로 결합하여 저장. 용량 1/2로 감소

	public ArtworkPostCacheModel(ArtworkPost artworkPost, String imageUrl) {
		this.id = artworkPost.getId();

		// 모든 필드를 하나의 문자열로 결합 (구분자는 쉼표)
		this.metadata = String.join(",",
			artworkPost.getStatus().name(),  // status (ArtworkPostStatus -> String)
			artworkPost.getTitle(),           // title
			artworkPost.getExplanation(),     // explanation
			String.valueOf(artworkPost.getViewCount()),  // viewCount (Long -> String)
			String.valueOf(artworkPost.getLikeCount()),  // likeCount (Long -> String)
			String.valueOf(artworkPost.getCommentCount()),  // commentCount (Long -> String)
			String.valueOf(artworkPost.getUser().getId()),  // userId (Long -> String)
			artworkPost.getCategory().name(),  // category (Category -> String)
			imageUrl                           // imageUrl
		);
	}

	// 각 필드를 get 메서드를 통해 반환 (metadata에서 분리)
	public ArtworkPostStatus getStatus() {
		return ArtworkPostStatus.valueOf(metadata.split(",")[0]);  // 첫 번째 요소: status
	}

	public String getTitle() {
		return metadata.split(",")[1];  // 두 번째 요소: title
	}

	public String getExplanation() {
		return metadata.split(",")[2];  // 세 번째 요소: explanation
	}

	public Long getViewCount() {
		return Long.parseLong(metadata.split(",")[3]);  // 네 번째 요소: viewCount (String -> Long)
	}

	public Long getLikeCount() {
		return Long.parseLong(metadata.split(",")[4]);  // 다섯 번째 요소: likeCount (String -> Long)
	}

	public Long getCommentCount() {
		return Long.parseLong(metadata.split(",")[5]);  // 여섯 번째 요소: commentCount (String -> Long)
	}

	public Long getUserId() {
		return Long.parseLong(metadata.split(",")[6]);  // 일곱 번째 요소: userId (String -> Long)
	}

	public Category getCategory() {
		return Category.valueOf(metadata.split(",")[7]);  // 여덟 번째 요소: category
	}

	public String getImageUrl() {
		return metadata.split(",")[8];  // 아홉 번째 요소: imageUrl
	}

	public void increaseLikeCount() {
		// metadata를 쉼표로 분리하여 배열로 변환
		String[] parts = metadata.split(",");

		// likeCount가 저장된 인덱스 (5번째 요소) 값을 가져와서 증가
		Long currentLikeCount = Long.parseLong(parts[4]);
		currentLikeCount++;

		// 증가된 값을 다시 설정
		parts[4] = String.valueOf(currentLikeCount);

		// 업데이트된 metadata 문자열을 다시 결합
		this.metadata = String.join(",", parts);
	}

}
// public class ArtworkPostCacheModel implements Serializable {
//
// 	@Id
// 	private Long id;
//
// 	private ArtworkPostStatus status;
//
// 	private String title;
//
// 	private String explanation;
//
// 	private Long viewCount;
//
// 	private Long likeCount;
//
// 	private Long commentCount;
//
// 	private Long userId;
//
// 	private Category category;
//
// 	private String imageUrl;
//
// 	public ArtworkPostCacheModel(ArtworkPost artworkPost, String imageUrl) {
// 		this.id = artworkPost.getId();
// 		this.status = artworkPost.getStatus();
// 		this.title = artworkPost.getTitle();
// 		this.explanation = artworkPost.getExplanation();
// 		this.viewCount = artworkPost.getViewCount();
// 		this.likeCount = artworkPost.getLikeCount();
// 		this.commentCount = artworkPost.getCommentCount();
// 		this.userId = artworkPost.getUser().getId();
// 		this.category = artworkPost.getCategory();
// 		this.imageUrl = imageUrl;
// 	}
// }
