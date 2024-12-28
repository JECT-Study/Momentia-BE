package org.ject.momentia.api.collection.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.collection.CollectionArtworkId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionArtworkRepository extends JpaRepository<CollectionArtwork, CollectionArtworkId>{

    @Query("SELECT ap from CollectionArtwork ca " +
            "inner join ArtworkPost ap on ap.id = ca.id.artwork.id "+
            "where ca.id.collection.id = :collectionId " +
            "and (ap.status = 'PUBLIC' OR ap.user = :user) "+
            "ORDER BY ca.createdAt DESC limit 1")
    Optional<ArtworkPost> findByArtworkPostByCollectionId(@Param("collectionId") Long collectionId, @Param("userId") User user);

}
