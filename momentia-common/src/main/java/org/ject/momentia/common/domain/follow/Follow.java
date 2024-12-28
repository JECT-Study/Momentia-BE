package org.ject.momentia.common.domain.follow;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ject.momentia.common.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "follow")
public class Follow extends BaseEntity {
    @EmbeddedId
    private FollowId id;

}