package org.ject.momentia.common.domain.artwork;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.ject.momentia.common.domain.user.User;

import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkLikeId implements java.io.Serializable {
    private static final long serialVersionUID = -4702156581591520289L;

//    @NotNull
//    @Column(name = "post_id", nullable = false)
//    private Long postId;
//
//    @NotNull
//    @Column(name = "user_id", nullable = false)
//    private Long userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private ArtworkPost post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArtworkLikeId entity = (ArtworkLikeId) o;
        return Objects.equals(this.post, entity.post) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user);
    }

}