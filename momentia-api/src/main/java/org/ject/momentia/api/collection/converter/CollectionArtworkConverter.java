package org.ject.momentia.api.collection.converter;

import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.collection.CollectionArtworkId;

public class CollectionArtworkConverter {
    public static CollectionArtwork toCollectionArtwork(Collection collection, ArtworkPost artworkPost){
        return CollectionArtwork.builder()
                .id(new CollectionArtworkId(collection,artworkPost))
                .build();
    }


}
