package edu.uci.ics.sidneyjt.service.billing.models.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "unit_price", required = true)
    private Float unit_price;
    @JsonProperty(value = "discount", required = true)
    private Float discount;
    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;
    @JsonProperty(value = "movie_title", required = true)
    private String movie_title;
    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;
    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonCreator
    public Item(@JsonProperty(value = "email", required = true) String email,
                @JsonProperty(value = "unit_price", required = true) Float unit_price,
                @JsonProperty(value = "discount", required = true) Float discount,
                @JsonProperty(value = "quantity", required = true) Integer quantity,
                @JsonProperty(value = "movie_id", required = true) String movie_id,
                @JsonProperty(value = "movie_title", required = true) String movie_title,
                @JsonProperty(value = "backdrop_path") String backdrop_path,
                @JsonProperty(value = "poster_path") String poster_path) {
        this.email = email;
        this.unit_price = unit_price;
        this.discount = discount;
        this.quantity = quantity;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
    }
    @JsonCreator
    public Item(){}

    public Item(String email, Float unit_price, Float discount, Integer quantity, Thumbnail thumbnail) {
        this.email = email;
        this.unit_price = unit_price;
        this.discount = discount;
        this.quantity = quantity;
        this.movie_id = thumbnail.getMovie_id();
        this.movie_title = thumbnail.getTitle();
        this.backdrop_path = thumbnail.getBackdrop_path();
        this.poster_path = thumbnail.getPoster_path();
    }

    @JsonProperty(value = "email", required = true)
    public String getEmail() {
        return email;
    }
    @JsonProperty(value = "unit_price", required = true)
    public Float getUnit_price() {
        return unit_price;
    }
    @JsonProperty(value = "discount", required = true)
    public Float getDiscount() {
        return discount;
    }
    @JsonProperty(value = "quantity", required = true)
    public Integer getQuantity() {
        return quantity;
    }
    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }
    @JsonProperty(value = "movie_title", required = true)
    public String getMovie_title() {
        return movie_title;
    }
    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }
    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }
}
