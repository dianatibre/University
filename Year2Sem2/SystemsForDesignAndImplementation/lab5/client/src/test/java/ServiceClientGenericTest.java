import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.ServiceClientGeneric;
import ro.ubb.socket.common.domain.BaseEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceClientGenericTest {
    public ServiceClientGeneric<Integer, BaseEntity<Integer>> generic;

    @Test
    public void testAdd() {
        BaseEntity<Integer> i = new BaseEntity<>();
        assertThrows(NullPointerException.class, () -> this.generic.add(i));
    }

    @Test
    public void testDelete() {
        assertThrows(NullPointerException.class, () -> this.generic.delete(1));
    }

    @Test
    public void testUpdate() {
        BaseEntity<Integer> i = new BaseEntity<>();
        assertThrows(NullPointerException.class, () -> this.generic.add(i));
    }

    @Test
    public void testGet() {
        assertThrows(NullPointerException.class, () -> this.generic.get());
    }

    @Test
    public void testSort() {
        BaseEntity<Integer> i = new BaseEntity<>();
        assertThrows(NullPointerException.class, () -> this.generic.add(i));
    }

    @Test
    public void testFilter() {
        BaseEntity<Integer> i = new BaseEntity<>();
        assertThrows(NullPointerException.class, () -> this.generic.add(i));
    }
}
