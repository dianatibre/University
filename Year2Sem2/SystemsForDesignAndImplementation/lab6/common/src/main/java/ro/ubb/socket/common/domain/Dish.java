package ro.ubb.socket.common.domain;

import java.io.Serializable;

/**
 * Class for a dish.
 */

public class Dish extends BaseEntity<Integer>implements Serializable {
    private String name;
    private float price;
    private int quantity;
    private String category;

    public Dish() {
    }

    /**
     * Dish constructor
     * @param name  name of the dish
     * @param price  price of the dish
     * @param quantity  quantity of the dish
     * @param category  category of the dish
     */
    public Dish(String name, float price, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    /**
     * Getter for the dish's name
     * @return the name of the dish
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the dish's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the dish's price
     * @return the price of the dish
     */
    public float getPrice() {
        return price;
    }

    /**
     * Setter for the dish's price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Getter for the dish's quantity
     * @return the quantity of the dish
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter for the dish's quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter for the dish's category
     * @return the category of the dish
     */
    public String getCategory() {
        return category;
    }

    /**
     * Setter for the dish's category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *Override the equals() method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;

        if (price!=dish.price) return false;
        if (quantity!=dish.quantity) return false;
        if (!category.equals(dish.category)) return false;

        return name.equals(dish.name);
    }

    /**
     *Override the hashCode() method.
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = (int) (31 * result + price);
        result = 31 * result + quantity;
        result = 31 * result + category.hashCode();
        return result;
    }

    /**
     *Override the toString() method.
     */
    @Override
    public String toString() {
        return "Dish{" +
                "id='" + getId()+'\''+
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                '}';
    }
}
