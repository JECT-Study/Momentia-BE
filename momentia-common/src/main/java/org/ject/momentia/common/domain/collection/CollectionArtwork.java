package org.ject.momentia.common.domain.collection;

import jakarta.persistence.*;
import lombok.*;
import org.ject.momentia.common.domain.base.BaseEntity;

@Getter
@Entity
@Table(name = "collection_artwork")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionArtwork extends BaseEntity {
    @EmbeddedId
    private CollectionArtworkId id;

}