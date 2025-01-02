package org.ject.momentia.api.monthly.repository;

import jakarta.validation.constraints.NotNull;
import org.ject.momentia.common.domain.monthly.MonthlyArtwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonthlyPostRepository extends JpaRepository<MonthlyArtwork,Long> {

    List<MonthlyArtwork> findAllByMonthAndYearOrderByRankAsc(@NotNull Integer month, @NotNull Integer year);
}
