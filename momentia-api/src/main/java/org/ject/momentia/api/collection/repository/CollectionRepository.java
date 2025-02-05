package org.ject.momentia.api.collection.repository;

import java.util.List;

import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

	Boolean existsCollectionsByNameAndUser(String name, User user);

	List<Collection> findAllByUserOrderByCreatedAtDesc(User user);

	Page<Collection> findAllByUser(User user, Pageable pageable);

	Page<Collection> findAllByUserAndStatus(User user, CollectionStatus status, Pageable pageable);

}
