package org.ject.momentia.api.follow.repository;

import java.util.List;

import org.ject.momentia.api.follow.model.FollowInfo;
import org.ject.momentia.common.domain.follow.Follow;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
	@Query(value = "SELECT u " +
		"FROM User u " +
		"JOIN Follow f ON u = f.id.user " +
		"WHERE f.id.follower = :follower")
	List<User> findFollowingUsers(@Param("follower") User follower);

	// @Query("""
	// 	SELECT new org.ject.momentia.api.follow.model.FollowInfo(
	// 	    u.id, i.imageSrc, u.nickname, u.introduction,false
	// 	)
	// 	FROM User u
	// 	JOIN Follow f ON u = f.id.user
	// 	LEFT JOIN Image i ON u.profileImage = i
	// 	WHERE f.id.follower = :follower
	// 	""")
	// List<FollowInfo> findFollowingUsersInfo(@Param("follower") User follower);

	@Query("""
		SELECT new org.ject.momentia.api.follow.model.FollowInfo(
		    u.id, i.imageSrc, u.nickname, u.introduction,
			CASE
		        WHEN :currentUser IS NULL THEN false
		        WHEN u = :currentUser THEN null
		        WHEN f2.id IS NOT NULL THEN true
		        ELSE false
		    END
		)
		FROM User u
		JOIN Follow f ON u = f.id.user
		LEFT JOIN Image i ON u.profileImage = i
		LEFT JOIN Follow f2 ON f2.id.user = u AND f2.id.follower = :currentUser
		WHERE f.id.follower = :follower
		""")
	List<FollowInfo> findFollowingUsersInfo(@Param("follower") User follower, @Param("currentUser") User currentUser);

	// @Query("""
	// 	SELECT new org.ject.momentia.api.follow.model.FollowInfo(
	// 	    u.id, i.imageSrc, u.nickname, u.introduction,false
	// 	)
	// 	FROM User u
	// 	JOIN Follow f ON u = f.id.follower
	// 	LEFT JOIN Image i ON u.profileImage = i
	// 	WHERE f.id.user = :following
	// 	""")
	// List<FollowInfo> findFollowerUsersInfo(@Param("following") User following);

	@Query("""
		SELECT new org.ject.momentia.api.follow.model.FollowInfo(
		    u.id, i.imageSrc, u.nickname, u.introduction,
		    CASE
		        WHEN :currentUser IS NULL THEN false
		        WHEN u = :currentUser THEN null
		        WHEN f2.id IS NOT NULL THEN true
		        ELSE false
		    END
		)
		FROM User u
		JOIN Follow f ON u = f.id.follower
		LEFT JOIN Image i ON u.profileImage = i
		LEFT JOIN Follow f2 ON f2.id.user = u AND f2.id.follower = :currentUser
		WHERE f.id.user = :following
		""")
	List<FollowInfo> findFollowerUsersInfo(
		@Param("following") User following,
		@Param("currentUser") User currentUser
	);

	@Query("""
		   SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
		   FROM Follow f
		   WHERE f.id.user.id = :userId AND f.id.follower.id = :followerId
		""")
	Boolean existsByUserId(@Param("followerId") Long followerId, @Param("userId") Long userId);

	// @Query("""
	// SELECT new org.ject.momentia.api.follow.model.FollowInfo(
	//     u.id, i.imageSrc, u.nickname, u.introduction,
	//     CASE WHEN f2.id IS NOT NULL THEN true ELSE false END
	// )
	// FROM User u
	// JOIN Follow f ON u = f.id.follower
	// LEFT JOIN Image i ON u.profileImage = i
	// LEFT JOIN Follow f2 ON f2.id.user = :currentUser AND f2.id.follower = u
	// WHERE f.id.user = :following
	// """)
	// List<FollowInfo> findFollowerUsersInfo(
	// 	@Param("following") User following,
	// 	@Param("currentUser") User currentUser
	// );

}