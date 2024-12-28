package org.ject.momentia.common.domain.artwork;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ject.momentia.common.domain.base.BaseEntity;

@Getter
@Entity
@Table(name = "artwork_like")
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkLike extends BaseEntity {
    @EmbeddedId
    private ArtworkLikeId id;

}