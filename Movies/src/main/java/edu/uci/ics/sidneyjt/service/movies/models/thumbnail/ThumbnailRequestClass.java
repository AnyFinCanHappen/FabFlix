package edu.uci.ics.sidneyjt.service.movies.models.thumbnail;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThumbnailRequestClass
{
    @JsonProperty(value = "movie_ids", required = true)
    private String[] movie_ids;

    @JsonCreator
    public ThumbnailRequestClass(@JsonProperty(value = "movie_ids", required = true) String[] movie_ids) {
        this.movie_ids = movie_ids;
    }
    @JsonProperty(value = "movie_ids")
    public String[] getMovie_ids() {
        return movie_ids;
    }
}
