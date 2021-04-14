package ro.ubb.socket.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Class for an order.
 */

public class Order extends BaseEntity<Integer>implements Serializable {

    private Integer restaurantID;
    private Integer clientID;
    private Integer dishID;
    private String paymentType;
    private Date date;

    public Order() {
    }

    /**
     * Constructor for Order
     * @param restaurantID the id of the restaurant
     * @param clientID the id of the client
     * @param dishID the id of the dish
     * @param paymentType the type of the payment
     * @param date the date
     */
    public Order(Integer restaurantID, Integer clientID, Integer dishID, String paymentType, Date date) {
        this.restaurantID = restaurantID;
        this.clientID = clientID;
        this.dishID = dishID;
        this.paymentType = paymentType;
        this.date = date;
    }

    /**
     * Getter for the order's clientId
     * @return the clientId of the order
     */
    public Integer getClientID() {
        return clientID;
    }

    /**
     * Setter for the order's clientId
     */
    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    /**
     * Getter for the order's dishId
     * @return the dishId of the order
     */
    public Integer getDishID() {
        return dishID;
    }

    /**
     * Setter for the order's dishId
     */
    public void setDishID(Integer dishID) {
        this.dishID = dishID;
    }

    /**
     * Getter for the order's payment Type
     * @return the payment Type of the order
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Setter for the order's payment Type
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Getter for the order's date
     * @return the date of the order
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the order's date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the order's restaurant ID
     * @return
     */
    public Integer getRestaurantID() {
        return restaurantID;
    }

    /**
     * Setter for the order's restaurant ID
     * @param restaurantID
     */
    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
     *Override the equals() method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;

        if (paymentType!=order.paymentType) return false;
        if (date!=order.date) return false;
        if (clientID!=order.clientID) return false;
        if (restaurantID!=order.restaurantID) return false;

        return dishID!=(order.dishID);
    }

    /**
     *Override the hashCode() method.
     */
    @Override
    public int hashCode() {
        int result = clientID.hashCode();
        result = 31 + result + restaurantID;
        result = 31 * result + dishID;
        result = 31 * result + paymentType.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    /**
     *Override the toString() method.
     */
    @Override
    public String toString() {
        return "Order{" +
                "id="+ getId()+
                ", restaurantID=" + restaurantID +
                ", clientID=" + clientID +
                ", dishID=" + dishID +
                ", paymentType='" + paymentType + '\'' +
                ", date=" + date +
                '}';
    }
}
