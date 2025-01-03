package org.ject.momentia.api.follow.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.follow.converter.FollowConverter;
import org.ject.momentia.api.follow.model.FollowUserRequest;
import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.api.user.service.AccountService;
import org.ject.momentia.common.domain.follow.Follow;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final AccountService accountService;

    public Boolean isFollowing(User user, User following) {
        if(user==null) return false;
        return followRepository.existsById(new FollowId(following,user));
    }

    @Transactional
    public void follow(User user, FollowUserRequest followingId) {
        User followingUser = accountService.findByIdElseThrowException(followingId.userId());
        FollowId id = FollowConverter.toFollowId(user, followingUser);
        if(isFollowing(user,followingUser)) throw ErrorCd.ALREADY_FOLLOW.serviceException();
        followRepository.save(FollowConverter.toFollow(id));

        // followCount 직접 세는 로직으로 변경 의논
        followingUser.increaseFollowerCount();
        user.increaseFollowingCount(); // Detached 상태이므로 save 필요
        userRepository.save(user);
    }

    @Transactional
    public void unfollow(User user,FollowUserRequest followingId){
        User followingUser = accountService.findByIdElseThrowException(followingId.userId());
        FollowId id = FollowConverter.toFollowId(user, followingUser);
        if(!isFollowing(user,followingUser)) throw ErrorCd.FOLLOW_NOT_FOUND.serviceException();
        followRepository.deleteById(id);

        // followCount 직접 세는 로직으로 변경 의논
        followingUser.decreaseFollowerCount();
        user.decreaseFollowingCount();
        userRepository.save(user);
    }
}
