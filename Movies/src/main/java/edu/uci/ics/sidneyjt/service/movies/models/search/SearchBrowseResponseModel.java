package edu.uci.ics.sidneyjt.service.movies.models.search;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.MovieModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;

public class SearchBrowseResponseModel extends ResponseModel
{
    @JsonProperty(value = "movies", required = true)
    private MovieModel movies[];

    /*
    @JsonCreator public SearchResponseModel(@JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                            @JsonProperty(value = "message", required = true) String message,
                                            @JsonProperty(value = "movies", required = true) MovieModel[] movies)
    {
        super(resultCode) = resultCode;

    }*/

    public SearchBrowseResponseModel() {
    }



    @JsonProperty(value = "movies")
    public MovieModel[] getMovies() {
        return movies;
    }


    @JsonIgnore
    public void setMovies(MovieModel[] movies)
    {
        this.movies = movies;
        if(movies == null)
        {
            this.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else if (movies.length == 0){
            this.setResult(Result.NO_MOVIE_FOUND);
        }
        else
            this.setResult(Result.FOUND_MOVIE);
    }




}
