package org.ject.momentia.common.domain.monthly;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.user.User;

@Getter
@Setter
@Entity
@Table(name = "monthly_user")
public class MonthlyUser extends BaseEntity {

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
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @NotNull
    @Column(name = "rank", nullable = false)
    private Integer rank;

}