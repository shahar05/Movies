package com.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.time.LocalDate;

@Value
@JsonPropertyOrder({"title", "price", "purchaseDate"})
public class OrderDTO {
    @JsonIgnore
    private final Order order;

    public OrderDTO(Order order) {
        this.order = order;
    }

    @JsonIgnore
    public Long getId() {
        return this.order.getId();
    }

    public LocalDate getPurchaseDate() {
        return this.order.getPurchaseDate();
    }

    public String getTitle() {
        return this.order.getTitle();
    }

    public Double getPrice() {
        return this.order.getPrice();
    }


}
