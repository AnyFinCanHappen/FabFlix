package edu.uci.ics.sidneyjt.service.billing.models.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveResponseModel extends ResponseModel
{
    @JsonProperty(value = "items")
    private Item[] items;


    @JsonCreator
    public RetrieveResponseModel(){}

    @JsonProperty(value = "items")
    public Item[] getItems() {
        return items;
    }

    @JsonIgnore
    public void setItems(Item[] items) {
        if(items == null || items.length == 0)
            this.setResult(Result.CART_NOT_FOUND);
        else
            this.setResult(Result.RETRIEVE_CART);
        this.items = items;
    }
}
