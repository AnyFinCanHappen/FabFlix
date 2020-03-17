package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;

public class PeopleGetResponseModel extends ResponseModel
{
    @JsonProperty(value = "person", required = true)
    private PeopleGetModel person;

    @JsonCreator
    public PeopleGetResponseModel(@JsonProperty(value = "person", required = true) PeopleGetModel person) {
        this.person = person;
    }

    public PeopleGetResponseModel(){}

    @JsonProperty(value = "person")
    public PeopleGetModel getPerson() {
        return person;
    }

    @JsonIgnore
    public void setPerson(PeopleGetModel person)
    {
        if(person == null)
            this.setResult(Result.INTERNAL_SERVER_ERROR);
        this.person = person;
    }
}
