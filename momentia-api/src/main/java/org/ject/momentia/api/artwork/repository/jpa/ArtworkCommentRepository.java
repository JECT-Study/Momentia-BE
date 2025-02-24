package org.ject.momentia.api.artwork.repository.jpa;

import java.util.List;

import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ArtworkCommentRepository extends JpaRepository<ArtworkComment, Long> {

	@Query(value = "SELECT ac FROM ArtworkComment ac where ac.post.id = :postId ORDER BY ac.createdAt DESC\n" +
		"LIMIT :size OFFSET :skip")
	List<ArtworkComment> findByArtworkIdOrderByCreatedAtDescForInfiniteScrolling(
		@Param("postId") Long postId, @Param("skip") Long skip, @Param("size") Long size);

	@Modifying
	@Query(value = "delete from ArtworkComment c where c.post = :artworkPost")
	void deleteAllByPost(ArtworkPost artworkPost);

}
