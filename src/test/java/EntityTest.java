import Domain.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityTest {
    private Entity<Integer> entity;

    @BeforeEach
    void setUp() { entity = new Entity<>(); }

    @Test
    void get_setId() {
        entity.setId(1234);
        assertEquals(1234,entity.getId());
    }

    @Test
    void testEquals() {
        entity.setId(1234);
        Entity<Integer> entity2 = new Entity<>();
        entity2.setId(1234);
        assert(entity.equals(entity2));
    }
}