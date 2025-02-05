package org.ject.momentia.common.domain.artwork;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "artwork_like")
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkLike { //extends BaseEntity
	@EmbeddedId
	private ArtworkLikeId id;

	//@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	//@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}