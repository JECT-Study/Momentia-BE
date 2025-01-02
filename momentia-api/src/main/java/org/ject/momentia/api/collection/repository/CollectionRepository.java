package org.ject.momentia.api.collection.repository;

import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ject.momentia.common.domain.collection.Collection;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long>{

    Boolean existsCollectionsByNameAndUser(String name, User user);

    List<Collection> findAllByUserOrderByCreatedAtDesc(User user);
}
