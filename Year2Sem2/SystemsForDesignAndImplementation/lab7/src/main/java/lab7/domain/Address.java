package lab7.domain;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Address extends BaseEntity<Integer>{

    private String city;
    private String street;
    private Integer number;
    private String zipCode;

    public Address() {
    }

    /**
     * Address constructor
     * @param city - the name of the city
     * @param street - the name of the street
     * @param number - the house number
     * @param zipCode - the zipCode
     */
    public Address(String city, String street, Integer number, String zipCode) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }

    /**
     * Getter for City
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter for the city
     * @param city name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter for street name
     * @return street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * setter for street name
     * @param street name
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Getter for house number
     * @return house number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * setter for house number
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * getter for zip code
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * setter for zip code
     * @param zipCode zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Overrides the method equals()
     * @param o - other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(number, address.number) &&
                Objects.equals(zipCode, address.zipCode);
    }

    /**
     * Overrides the hashCode() method
     */
    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + number;
        result = 31 * result + zipCode.hashCode();
        return result;
    }

    /**
     * Overrides the toString() method
     */
    @Override
    public String toString() {
        return "Address{" +
                "id='"+getId()+'\''+
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", zipCode=" + zipCode +
                '}';
    }
}
