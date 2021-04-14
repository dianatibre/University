package ro.ubb.socket.common.domain;

import java.io.Serializable;

/**
 * Class for a Client.
 */

public class Client extends BaseEntity<Integer> implements Serializable {
    private String name;
    private int age;
    private Integer address;
    private String email;

    public Client() {
    }

    /**
     * Client constructor
     * @param name  name of the client
     * @param age  age of the client
     * @param address  address of the client
     * @param email  email of the client
     */
    public Client(String name, int age, Integer address, String email) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
    }

    /**
     * Getter for the client's name
     * @return the name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the client's age
     * @return the age of the client
     */
    public int getAge() {
        return age;
    }

    /**
     * Getter for the client's address
     * @return the address of the client
     */
    public Integer getAddress() {
        return address;
    }

    /**
     * Getter for the client's email
     * @return the email of the client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the client's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the client's age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Setter for the client's address
     */
    public void setAddress(Integer address) {
        this.address = address;
    }

    /**
     * Setter for the client's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *Override the equals() method.
     */
    @Override
    public boolean equals(Object o) {
        //overrides the method equals
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;

        if (!email.equals(client.email)) return false;
        if (address!=client.address) return false;
        if (age!=client.age) return false;

        return name.equals(client.name);
    }

    /**
     *Override the hashCode() method.
     */
    @Override
    public int hashCode() {
        //overrides the method hashcode
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + address;
        result = 31 * result + email.hashCode();
        return result;
    }

    /**
     *Override the toString() method.
     */
    @Override
    public String toString() {
        return "Client{" +
                "id='" + getId()+'\''+
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
