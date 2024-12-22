package org.ject.momentia.api.user.repository;

import java.util.Optional;

import org.ject.momentia.common.domain.user.SocialAccount;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
	Optional<SocialAccount> findBySocialIdAndSocialType(String socialId, OAuthProvider socialType);
}
