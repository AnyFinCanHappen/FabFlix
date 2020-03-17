package edu.uci.ics.sidneyjt.service.movies.models.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrowseRequestModel extends SearchBrowseRequestBase
{
    @JsonProperty(value = "phrase", required = true)
    private String[] phrase;



    @JsonCreator
    public BrowseRequestModel( @JsonProperty(value = "phrase", required = true) String[] phrase,
                               @JsonProperty(value = "hidden") Boolean hidden,
                               @JsonProperty(value = "limit") Integer limit,
                               @JsonProperty(value = "offset") Integer offset,
                               @JsonProperty(value = "orderby") String orderby,
                               @JsonProperty(value = "direction")String direction)
    {
        super(hidden,limit,offset,orderby,direction);
        this.phrase = phrase;
    }

    @JsonProperty(value = "phrase")
    public String[] getPhrase() {
        return phrase;
    }

}
