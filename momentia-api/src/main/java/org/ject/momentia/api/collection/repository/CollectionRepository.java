package org.ject.momentia.api.collection.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ject.momentia.common.domain.collection.Collection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long>{

    Boolean existsCollectionsByNameAndUser(String name, User user);

    List<Collection> findAllByUserOrderByCreatedAtDesc(User user);
}
