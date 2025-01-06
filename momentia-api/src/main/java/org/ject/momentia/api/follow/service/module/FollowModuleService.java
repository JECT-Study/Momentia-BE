package org.ject.momentia.api.follow.service.module;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class FollowModuleService {
    private final FollowRepository followRepository;

    public Boolean isFollowing(User user, User following) {
        if(user==null) return false;
        return followRepository.existsById(new FollowId(following,user));
    }
    public List<User> findFollowers(User user) {
        return followRepository.findFollowingUsers(user);
    }
}
