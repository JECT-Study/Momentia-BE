package org.ject.momentia.api.artwork.repository;

import java.util.List;

import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ArtworkLikeRepository extends JpaRepository<ArtworkLike, ArtworkLikeId> {

	@Modifying
	@Query("delete from ArtworkLike al where al.id.post = :post")
	void deleteAllByArtwork(@Param("artworkId") ArtworkPost post);

	@Query("SELECT COUNT(l) > 0 FROM ArtworkLike l WHERE l.id.post.id  = :postId AND l.id.user = :user")
	boolean existsByPostIdAndUserId(@Param("postId") Long postId, @Param("user") User user);

	@Query("SELECT l FROM ArtworkLike l WHERE l.id.user = :user")
	List<ArtworkLike> findByUser(@Param("user") User user);
}
