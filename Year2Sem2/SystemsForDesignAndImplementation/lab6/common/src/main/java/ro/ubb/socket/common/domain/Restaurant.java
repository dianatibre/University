package ro.ubb.socket.common.domain;

import java.io.Serializable;
import java.util.Objects;

public class Restaurant extends BaseEntity<Integer>implements Serializable {

    private String name;
    private Integer rating;
    private Integer capacity;
    private Boolean delivery;

    public Restaurant() {
    }

    /**
     * Restaurant entity constructor
     * @param name the name of the restaurant
     * @param rating the rating of the restaurant
     * @param capacity the capacity of the restaurant
     * @param delivery the delivery of the restaurant
     */
    public Restaurant(String name, Integer rating, Integer capacity, Boolean delivery) {
        this.name = name;
        this.rating = rating;
        this.capacity = capacity;
        this.delivery = delivery;
    }

    /**
     * getter for name field
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name field
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for rating field
     * @return rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * setter for rating field
     * @param rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * getter for capacity field
     * @return capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * setter for capacity field
     * @param capacity
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * getter for delivery field
     * @return
     */
    public Boolean getDelivery() {
        return delivery;
    }

    /**
     * setter for delivery field
     * @param delivery
     */
    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    /**
     * overrides the equals() method
     * @param o - other
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(rating, that.rating) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(delivery, that.delivery);
    }

    /**
     * overrides the hashCode() method
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + delivery.hashCode();
        result = 31 * result + rating;
        result = 31 * result + capacity;
        return result;
    }

    /**
     * Overrides the toString() method
     */
    @Override
    public String toString() {
        return "Restaurant{" +
                "id='"+ getId()+'\''+
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", capacity=" + capacity +
                ", delivery=" + delivery +
                '}';
    }
}
