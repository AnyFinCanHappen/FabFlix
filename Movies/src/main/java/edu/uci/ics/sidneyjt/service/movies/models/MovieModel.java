package edu.uci.ics.sidneyjt.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieModel
{
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "year", required = true)
    private Integer year;

    @JsonProperty(value = "director", required = true)
    private String director;

    @JsonProperty(value = "rating", required = true)
    private float rating;

    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;

    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonProperty(value = "hidden")
    private Boolean hidden;

    @JsonCreator
    public MovieModel(@JsonProperty(value = "movie_id", required = true) String movie_id,
                      @JsonProperty(value = "title", required = true)String title,
                      @JsonProperty(value = "year", required = true) Integer year,
                      @JsonProperty(value = "director", required = true) String director,
                      @JsonProperty(value = "rating", required = true)float rating,
                      @JsonProperty(value = "backdrop_path") String backdrop_path,
                      @JsonProperty(value = "poster_path") String poster_path,
                      @JsonProperty(value = "hidden") boolean hidden)
    {
        this.movie_id = movie_id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.hidden = hidden;
    }

    public MovieModel(){}

    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }
    @JsonProperty(value = "title", required = true)
    public String getTitle() {
        return title;
    }
    @JsonProperty(value = "year", required = true)
    public Integer getYear() {
        return year;
    }
    @JsonProperty(value = "director", required = true)
    public String getDirector() {
        return director;
    }
    @JsonProperty(value = "rating", required = true)
    public float getRating() {
        return rating;
    }
    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }
    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty(value = "hidden")
    public Boolean isHidden() {
        return hidden;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
