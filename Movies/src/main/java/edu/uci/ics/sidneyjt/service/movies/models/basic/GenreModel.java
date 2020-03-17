package edu.uci.ics.sidneyjt.service.movies.models.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel
{
    @JsonProperty(value = "genre_id", required = true)
    private Integer genre_id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonCreator
    public GenreModel(@JsonProperty(value = "genre_id", required = true)Integer genre_id,
                      @JsonProperty(value = "name", required = true) String name) {
        this.genre_id = genre_id;
        this.name = name;
    }


    @JsonProperty(value = "genre_id")
    public Integer getGenre_id() {
        return genre_id;
    }
    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
