package org.ject.momentia.common.domain.artwork;

import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
	name = "artwork_post",
	schema = "momentia"
)
public class ArtworkPost extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 10)
	private ArtworkPostStatus status;

	@Size(max = 50)
	@NotNull
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@Size(max = 1000)
	@Column(name = "explanation", length = 1000)
	private String explanation;

	@NotNull
	@Column(name = "view_count", nullable = false)
	private Long viewCount;

	@NotNull
	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@NotNull
	@Column(name = "comment_count", nullable = false)
	private Long commentCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@NotNull
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 20)
	private Category category;

	public void updatePost(String status, String category, String title, String explanation) {
		if (status != null)
			this.status = ArtworkPostStatus.valueOf(status);
		if (category != null)
			this.category = Category.valueOf(category);
		if (title != null)
			this.title = title;
		if (explanation != null)
			this.explanation = explanation.isBlank() ? null : explanation;
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}

	public void increaseCommentCount() {
		this.commentCount++;
	}

	public void decreaseCommentCount() {
		this.commentCount--;
	}

	public void increaseViewCount(Long view) {
		this.viewCount += view;
	}

}