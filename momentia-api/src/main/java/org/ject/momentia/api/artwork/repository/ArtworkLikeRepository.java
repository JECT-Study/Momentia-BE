package org.ject.momentia.api.artwork.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtworkLikeRepository extends JpaRepository<ArtworkLike, ArtworkLikeId> {
}
