package org.ject.momentia.api.follow.service.module;

import java.util.List;
import java.util.Objects;

import org.ject.momentia.api.follow.repository.FollowRepository;
import org.ject.momentia.common.domain.follow.FollowId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowModuleService {
	private final FollowRepository followRepository;

	// returnNullIfSelf -> if문에서 사용하면 false ,
	public Boolean isFollowing(User user, User following, boolean returnNullIfSelf) {
		if (user == null)
			return false;
		if (Objects.equals(user.getId(), following.getId()))
			return returnNullIfSelf ? null : false;
		return followRepository.existsById(new FollowId(following, user));
	}
    
	public List<User> findFollowers(User user) {
		return followRepository.findFollowingUsers(user);
	}
}
