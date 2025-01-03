package org.ject.momentia.common.domain.follow;

import jakarta.persistence.*;
import lombok.*;
import org.ject.momentia.common.domain.base.BaseEntity;

@Getter
@Entity
@Table(name = "follow")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends BaseEntity {
    @EmbeddedId
    private FollowId id;

}