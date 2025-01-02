package org.ject.momentia.api.artwork.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkLikeRepository extends JpaRepository<ArtworkLike, ArtworkLikeId> {

    @Modifying
    @Query("delete from ArtworkLike al where al.id.post = :post")
    void deleteAllByArtwork(@Param("artworkId") ArtworkPost post);
}
