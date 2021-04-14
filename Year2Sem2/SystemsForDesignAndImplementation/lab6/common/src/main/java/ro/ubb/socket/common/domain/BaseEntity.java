package ro.ubb.socket.common.domain;

import java.io.Serializable;

/**
 * Class for BaseEntity.
 */
public class BaseEntity<ID> implements Serializable {
    private ID id;

    /**
     * Getter for base entity's id.
     */
    public ID getId() {
        return id;
    }

    /**
     * Setter for base entity's id.
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Override toString() function.
     */
    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
