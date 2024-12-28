package org.ject.momentia.api.artwork.service;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.user.infra.CustomUserDetails;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Temp {

    private final UserRepository userRepository;

    public User getUserObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user;
        if(authentication.getPrincipal().equals("anonymousUser")){
            user = null;
        }
        else{
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            user = userRepository.findById(userId).orElseThrow();
        }
        return user;
    }
}
