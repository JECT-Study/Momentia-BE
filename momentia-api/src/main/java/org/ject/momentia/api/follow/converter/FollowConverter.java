package org.ject.momentia.api.follow.converter;

import org.ject.momentia.common.domain.follow.Follow;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;

public class FollowConverter {
    static public Follow toFollow(FollowId followId){
        return Follow.builder().id(followId).build();
    }

    static public FollowId toFollowId(User user,User followingUser){
        return FollowId.builder().follower(user).user(followingUser).build();
    }
}
