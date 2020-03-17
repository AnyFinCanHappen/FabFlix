package edu.uci.ics.sidneyjt.service.movies.models.search;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.base.RequestModel;

public class SearchBrowseRequestBase extends RequestModel
{
    @JsonProperty(value = "hidden")
    private Boolean hidden;

    @JsonProperty(value = "limit")
    private Integer limit;

    @JsonProperty(value = "offset")
    private Integer offset;

    @JsonProperty(value = "orderby")
    private String orderby;

    @JsonProperty(value = "direction")
    private String direction;

    @JsonCreator
    public SearchBrowseRequestBase(@JsonProperty(value = "hidden") Boolean hidden,
                                   @JsonProperty(value = "limit") Integer limit,
                                   @JsonProperty(value = "offset") Integer offset,
                                   @JsonProperty(value = "orderby") String orderby,
                                   @JsonProperty(value = "direction")String direction)
    {
        this.hidden = hidden;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }
    @JsonProperty(value = "hidden")
    public Boolean isHidden() {
        return hidden;
    }
    @JsonProperty(value = "limit")
    public Integer getLimit() {
        return limit;
    }
    @JsonProperty(value = "offset")
    public Integer getOffset() {
        return offset;
    }
    @JsonProperty(value = "orderby")
    public String getOrderby() {
        return orderby;
    }
    @JsonProperty(value = "direction")
    public String getDirection() {
        return direction;
    }
}
