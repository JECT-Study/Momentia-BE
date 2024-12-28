package org.ject.momentia.common.domain.collection;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;
import org.ject.momentia.common.domain.user.User;

@Getter
@Entity
@Table(name = "collection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private CollectionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotNull
    private User user;

    public void update(String name,String status){
        if(name != null) this.name = name;
        if(status != null) this.status = CollectionStatus.valueOf(status);
    }

}