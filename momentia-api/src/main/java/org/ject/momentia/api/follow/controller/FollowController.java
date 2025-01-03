package org.ject.momentia.api.follow.controller;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.follow.model.FollowUserRequest;
import org.ject.momentia.api.follow.service.FollowService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void follow(@RequestBody FollowUserRequest request, @MomentiaUser User user) {
        followService.follow(user, request);
    }

    @DeleteMapping("/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(@RequestBody FollowUserRequest request, @MomentiaUser User user) {
        followService.unfollow(user, request);
    }
}
