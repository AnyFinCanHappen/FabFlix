package edu.uci.ics.sidneyjt.service.billing.models.order.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemModel
{
    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;

    @JsonProperty(value = "unit_price", required = true)
    private Float unit_price;

    @JsonProperty(value = "discount", required = true)
    private Float discount;

    @JsonProperty(value = "sale_date", required = true)
    private String sale_date;

    @JsonCreator
    public OrderItemModel(@JsonProperty(value = "email", required = true)String email,
                          @JsonProperty(value = "movie_id", required = true)String movie_id,
                          @JsonProperty(value = "quantity", required = true) Integer quantity,
                          @JsonProperty(value = "unit_price", required = true) Float unit_price,
                          @JsonProperty(value = "discount", required = true) Float discount,
                          @JsonProperty(value = "sale_date", required = true) String sale_date) {
        this.email = email;
        this.movie_id = movie_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.sale_date = sale_date;
    }
    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }
    @JsonProperty(value = "movie_id")
    public String getMovie_id() {
        return movie_id;
    }
    @JsonProperty(value = "quantity")
    public Integer getQuantity() {
        return quantity;
    }
    @JsonProperty(value = "unit_price")
    public Float getUnit_price() {
        return unit_price;
    }
    @JsonProperty(value = "discount")
    public Float getDiscount() {
        return discount;
    }
    @JsonProperty(value = "sale_date")
    public String getSale_date() {
        return sale_date;
    }
}
