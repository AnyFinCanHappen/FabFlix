package edu.uci.ics.sidneyjt.service.movies.models.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeopleModel
{
    @JsonProperty(value = "person_id", required = true)
    private String person_id;
    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonCreator
    public PeopleModel(@JsonProperty(value = "person_id", required = true) String person_id,
                       @JsonProperty(value = "name", required = true) String name) {
        this.person_id = person_id;
        this.name = name;
    }

    @JsonIgnore
    public PeopleModel(){}

    @JsonProperty(value = "person_id")
    public String getPerson_id() {
        return person_id;
    }
    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
