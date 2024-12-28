package org.ject.momentia.api.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<User> findByEmail(String email);

	List<User> findAllByIdIn(Collection<Long> id);
}
