package edu.uci.ics.sidneyjt.service.movies.models.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;

public class GetResponseModel extends ResponseModel
{
    @JsonProperty(value = "movie", required = true)
    private GetMovieModel movie;

    @JsonCreator
    public GetResponseModel(GetMovieModel movie) {
        this.movie = movie;
    }
    @JsonCreator public GetResponseModel(){}

    @JsonProperty(value = "movie")
    public GetMovieModel getMovie() {
        return movie;
    }

    @JsonIgnore
    public void setMovies(GetMovieModel movie)
    {
        this.movie = movie;
        if(movie == null)
        {
            this.setResult(Result.NO_MOVIE_FOUND);
        }
        else
            this.setResult(Result.FOUND_MOVIE);
    }
}
