package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.MovieModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;

public class PeopleSearchResponseModel extends ResponseModel
{
    @JsonProperty(value = "people", required = true)
    private PeopleSearchModel[] people;

    public PeopleSearchResponseModel(){}

    @JsonProperty(value = "people")
    public PeopleSearchModel[] getPeople() {
        return people;
    }

    @JsonIgnore
    public void setPeople(PeopleSearchModel[] people)
    {
        this.people = people;
        if(people == null)
        {
            this.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else if (people.length == 0){
            this.setResult(Result.NO_MOVIE_FOUND);
        }
        else
            this.setResult(Result.FOUND_MOVIE);
    }
}
