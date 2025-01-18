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

	@Query("""
		SELECT new org.ject.momentia.api.follow.model.FollowInfo(
		    u.id, i.imageSrc, u.nickname, u.introduction
		)
		FROM User u
		JOIN Follow f ON u = f.id.user
		LEFT JOIN Image i ON u.profileImage = i.id
		WHERE f.id.follower = :follower
		""")
	List<FollowInfo> findFollowingUsersInfo(@Param("follower") User follower);

	@Query("""
		SELECT new org.ject.momentia.api.follow.model.FollowInfo(
		    u.id, i.imageSrc, u.nickname, u.introduction
		)
		FROM User u
		JOIN Follow f ON u = f.id.follower
		LEFT JOIN Image i ON u.profileImage = i.id
		WHERE f.id.user = :following
		""")
	List<FollowInfo> findFollowerUsersInfo(@Param("following") User following);
}