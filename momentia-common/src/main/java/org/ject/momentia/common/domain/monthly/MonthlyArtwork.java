package org.ject.momentia.common.domain.monthly;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "monthly_artwork")
public class MonthlyArtwork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name="post_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ArtworkPost post;

    @NotNull
    @Column(name = "rank", nullable = false)
    private Integer rank;

}