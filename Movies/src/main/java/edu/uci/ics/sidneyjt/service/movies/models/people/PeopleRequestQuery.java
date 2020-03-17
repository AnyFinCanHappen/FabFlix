package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseRequestBase;

public class PeopleRequestQuery extends SearchBrowseRequestBase
{
    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonCreator
    public PeopleRequestQuery(String name,
    @JsonProperty(value = "limit") Integer limit,
    @JsonProperty(value = "offset") Integer offset,
    @JsonProperty(value = "orderby") String orderby,
    @JsonProperty(value = "direction") String direction,
    @JsonProperty(value = "hidden") Boolean hidden) {
        super(hidden, limit, offset, orderby, direction);
        this.name = name;
    }


    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
