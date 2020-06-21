package com.company;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "customers")
public class Customer implements Comparable<Customer> {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName = null;
    private String lastName = null;
    private Integer age = null;

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY)
    private Set<Order> orderList;

    public Customer() {
    }

    public Customer(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Set<Order> getOrderList() {
        return orderList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public int compareTo(Customer o) {
        return Integer.compare(this.getAge(), o.getAge());
    }

    @Override
    public String toString() {
        return "name:  "+ getFirstName() +"  "+ getLastName();
    }
}
