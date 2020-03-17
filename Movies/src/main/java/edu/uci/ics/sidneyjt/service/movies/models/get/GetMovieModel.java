package edu.uci.ics.sidneyjt.service.movies.models.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.MovieModel;
import edu.uci.ics.sidneyjt.service.movies.models.basic.GenreModel;
import edu.uci.ics.sidneyjt.service.movies.models.basic.PeopleModel;

import java.math.BigInteger;

public class GetMovieModel extends MovieModel
{


    @JsonProperty(value = "num_votes")
    private Integer num_votes;

    @JsonProperty(value = "revenue")
    private BigInteger revenue;

    @JsonProperty(value = "budget")
    private Integer budget;

    @JsonProperty(value = "overview")
    private String overview;

    @JsonProperty(value = "genres", required = true)
    private GenreModel[] genres;

    @JsonProperty(value = "people", required = true)
    private PeopleModel[] people;

    /*
    @JsonCreator
    public GetMovieModel(String movie_id, String title, Integer year, String director, float rating, String backdrop_path, String poster_path, boolean hidden, Integer num_votes, Integer budget, String overview, GenreModel[] genres, PeopleModel[] people) {
        super(movie_id, title, year, director, rating, backdrop_path, poster_path, hidden);
        this.num_votes = num_votes;
        this.budget = budget;
        this.overview = overview;
        this.genres = genres;
        this.people = people;
    }



    @JsonCreator
    public GetMovieModel(Integer num_votes, Integer budget, String overview, GenreModel[] genres, PeopleModel[] people) {
        this.num_votes = num_votes;
        this.budget = budget;
        this.overview = overview;
        this.genres = genres;
        this.people = people;
    }
     */

    @JsonIgnore
    public void setGenres(GenreModel[] genres) {
        this.genres = genres;
    }
    @JsonIgnore
    public void setPeople(PeopleModel[] people) {
        this.people = people;
    }

    @JsonProperty(value = "num_votes")
    public Integer getNum_votes() {
        return num_votes;
    }
    @JsonProperty(value = "revenue")
    public BigInteger getRevenue() {
        return revenue;
    }
    @JsonProperty(value = "budget")
    public Integer getBudget() {
        return budget;
    }
    @JsonProperty(value = "overview")
    public String getOverview() {
        return overview;
    }
}
