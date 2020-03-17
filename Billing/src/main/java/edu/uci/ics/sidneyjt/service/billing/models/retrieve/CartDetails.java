package edu.uci.ics.sidneyjt.service.billing.models.retrieve;

public class CartDetails
{
    private Float discount;
    private Float unit_price;
    private Integer quantity;
    private String movie_id;

    public CartDetails(Float discount, Float unit_price, Integer quantity, String movie_id) {
        this.discount = discount;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.movie_id = movie_id;
    }

    public Float getDiscount() {
        return discount;
    }

    public Float getUnit_price() {
        return unit_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMovie_id() {
        return movie_id;
    }
}
