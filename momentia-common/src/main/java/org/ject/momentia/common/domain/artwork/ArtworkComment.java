package org.ject.momentia.common.domain.artwork;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.user.User;

@Getter
@Builder
@Entity
@Table(name = "artwork_comment")
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 500)
    @NotNull
    @Column(name = "content", nullable = false, length = 500)
    private String content;

//    @NotNull
//    @Column(name = "post_id", nullable = false)
//    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private ArtworkPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User user;

    public void update(String content){
        this.content = content;
    }

}