package org.ject.momentia.api.collection.repository;

import java.util.Optional;

import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.collection.CollectionArtworkId;
import org.ject.momentia.common.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CollectionArtworkRepository extends JpaRepository<CollectionArtwork, CollectionArtworkId> {

	// isMine : 본인 컬렉션 인지
	// 전달 이유 : 다른 유저 컬렉션 리스트 + 본인의 비공개 작품이 저장되어 있을 경우에
	// 본인 비공개 작품이 다른 사람 컬렉션 리스트에서 보이지 않게 하기 위해.
	@Query("SELECT ap from CollectionArtwork ca " +
		"inner join ArtworkPost ap on ap.id = ca.id.artwork.id " +
		"where ca.id.collection.id = :collectionId " +
		"and (ap.status = 'PUBLIC' OR (ap.user = :user AND :isMine = true)) " +
		"ORDER BY ca.createdAt DESC limit 1")
	Optional<ArtworkPost> findByArtworkPostByCollectionId(
		@Param("isMine") boolean isMine,
		@Param("collectionId") Long collectionId,
		@Param("userId") User user);

	@Modifying
	@Query("delete from CollectionArtwork ca where ca.id.artwork = :artworkPost")
	void deleteAllByArtworkPost(ArtworkPost artworkPost);

	@Modifying
	@Query("delete from CollectionArtwork ca where ca.id.collection = :collection")
	void deleteAllByCollection(Collection collection);

	@Query("SELECT ap from ArtworkPost ap " +
		"inner join CollectionArtwork ca on ap.id = ca.id.artwork.id " +
		"where ca.id.collection = :collection " +
		"and (ap.status = 'PUBLIC' OR (ap.user = :user AND :isMine = true)) "
	)
	Page<ArtworkPost> findPostByCollectionAndIsMine(
		User user, Boolean isMine,
		Collection collection,
		Pageable pageable);
}
