package org.ject.momentia.api.user.repository;

import org.ject.momentia.common.domain.user.NormalAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalAccountRepository extends JpaRepository<NormalAccount, Long> {
	boolean existsByIdAndPassword(Long id, String password);
}
