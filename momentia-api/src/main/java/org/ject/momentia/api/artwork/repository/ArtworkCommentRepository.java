package org.ject.momentia.api.artwork.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkCommentRepository extends JpaRepository<ArtworkComment, Long> {

    @Query(value = "SELECT ac FROM ArtworkComment ac where ac.post.id = :postId ORDER BY ac.createdAt DESC\n" +
            "LIMIT :size OFFSET :skip")
    List<ArtworkComment> findByArtworkIdOrderByCreatedAtDescForInfiniteScrolling(
            @Param("postId") Long postId, @Param("skip") Long skip, @Param("size") Long size);

    void deleteAllByPost(ArtworkPost artworkPost);
}
