package edu.uci.ics.sidneyjt.service.movies.models.thumbnail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Thumbnail
{
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;

    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonCreator
    public Thumbnail(@JsonProperty(value = "movie_id", required = true) String movie_id,
                     @JsonProperty(value = "title", required = true) String title,
                     @JsonProperty(value = "backdrop_path") String backdrop_path,
                     @JsonProperty(value = "poster_path") String poster_path) {
        this.movie_id = movie_id;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
    }
    @JsonProperty(value = "movie_id")
    public String getMovie_id() {
        return movie_id;
    }
    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
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
