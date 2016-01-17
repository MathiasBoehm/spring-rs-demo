package de.struktuhr.rs.entity;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:22
 */
public class Customer {

    private final String id;

    private final String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
