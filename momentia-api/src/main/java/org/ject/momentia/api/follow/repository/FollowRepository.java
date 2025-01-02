package org.ject.momentia.api.follow.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ject.momentia.common.domain.follow.Follow;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    @Query(value = "SELECT u " +
            "FROM User u " +
            "JOIN Follow f ON u = f.id.user " +
            "WHERE f.id.follower = :follower")
    List<User> findFollowingUsers(@Param("follower") User follower);
}