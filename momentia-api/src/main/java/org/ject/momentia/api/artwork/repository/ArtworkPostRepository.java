package org.ject.momentia.api.artwork.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.ject.momentia.api.artwork.model.FollowingUserPostProjection;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;

@Repository
public interface ArtworkPostRepository extends JpaRepository<ArtworkPost, Long> {

	List<ArtworkPost> findAllByIdIn(Collection<Long> ids);

	List<ArtworkPost> findAllByIdInAndStatus(Collection<Long> ids, @NotNull ArtworkPostStatus status);

	Optional<ArtworkPost> findFirstByUserAndStatusOrderByLikeCountDesc(User user, ArtworkPostStatus status);

	Page<ArtworkPost> findByCategoryAndStatus(Category category, ArtworkPostStatus status, Pageable pageable);

	Page<ArtworkPost> findByStatus(ArtworkPostStatus status, Pageable pageable);

	@Query("SELECT p FROM ArtworkPost p LEFT JOIN p.user u " +
		"WHERE (:category IS NULL OR p.category = :category) " +
		"AND p.status = 'PUBLIC' " +
		"AND (:keyword IS NULL " +
		"OR (REPLACE(p.title, ' ', '') LIKE %:keyword% OR REPLACE(u.nickname, ' ', '') LIKE %:keyword%))")
	Page<ArtworkPost> search(
		@Param("category") Category category,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query(value = """
		    SELECT *
		    FROM (
		        SELECT 
		            *,
		            ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY created_at DESC) AS num
		        FROM artwork_post
		        WHERE user_id IN :userIds AND status = 'PUBLIC'
		    ) posts
		    WHERE num <= 2 ORDER BY created_at DESC
		""", nativeQuery = true)
	List<FollowingUserPostProjection> findTwoRecentPostsByUserIds(@Param("userIds") List<Long> userIds);

	Page<ArtworkPost> findByUser(User user, Pageable pageable);

	Page<ArtworkPost> findByUserAndStatus(User user, ArtworkPostStatus status, Pageable pageable);

	@Query("SELECT p FROM ArtworkPost  p " +
		"WHERE p.id IN :ids " +
		"AND (p.user = :user OR p.status = 'PUBLIC')"
	)
	Page<ArtworkPost> findAllByIdIn(Collection<Long> ids, User user, Pageable pageable);

}
