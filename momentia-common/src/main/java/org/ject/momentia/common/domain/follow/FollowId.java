package org.ject.momentia.common.domain.follow;

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
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FollowId implements java.io.Serializable {
    private static final long serialVersionUID = 284658595118420341L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="follower_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User follower;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FollowId entity = (FollowId) o;
        return Objects.equals(this.follower, entity.follower) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, user);
    }

}