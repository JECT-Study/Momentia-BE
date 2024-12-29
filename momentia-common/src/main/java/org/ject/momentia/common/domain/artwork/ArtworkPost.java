package org.ject.momentia.common.domain.artwork;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.user.User;

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

//    @NotNull
//    @Column(name = "post_image", nullable = false)
//    private Long postImage;

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
    @Column(name = "viewCount", nullable = false)
    private Long viewCount;

    @NotNull
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @NotNull
    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

//    @NotNull
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private Category category;

    public void updatePost(String status, String category, String title, String explanation) {
        if (status != null) this.status = ArtworkPostStatus.valueOf(status);
        if (category != null) this.category = Category.valueOf(category);
        if (title != null) this.title = title;
        if (explanation != null) this.explanation = explanation;
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

}