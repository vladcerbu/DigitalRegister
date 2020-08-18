package Domain;

import java.util.Objects;

/**
 * Entity Object - base class
 * @param <ID> - the entity's ID
 */
public class Entity<ID> {
    private ID id;

    public ID getId() { return id; }

    public void setId(ID id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //noinspection rawtypes
        Entity entity = (Entity) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return id.toString() + ','; }
}
