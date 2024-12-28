package org.ject.momentia.common.domain.collection;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.ject.momentia.common.domain.artwork.ArtworkPost;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CollectionArtworkId implements java.io.Serializable {
    private static final long serialVersionUID = -2283208000310801664L;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name="collection_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name="artwork_id",referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ArtworkPost artwork;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CollectionArtworkId entity = (CollectionArtworkId) o;
        return Objects.equals(this.artwork, entity.artwork) &&
                Objects.equals(this.collection, entity.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artwork, collection);
    }

}