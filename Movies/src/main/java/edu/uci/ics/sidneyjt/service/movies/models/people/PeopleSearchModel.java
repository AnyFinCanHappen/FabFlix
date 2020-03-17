package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.basic.PeopleModel;

public class PeopleSearchModel extends PeopleModel
{
    @JsonProperty(value = "birthday")
    private String birthday;

    @JsonProperty(value = "popularity")
    private  String popularity;

    @JsonProperty(value = "profile_path")
    private String profile_path;

    @JsonCreator
    public PeopleSearchModel(@JsonProperty(value = "person_id", required = true) String person_id,
                             @JsonProperty(value = "name", required = true) String name,
                             @JsonProperty(value = "birthday") String birthday,
                             @JsonProperty(value = "popularity")String popularity,
                             @JsonProperty(value = "profile_path") String profile_path) {
        super(person_id, name);
        this.birthday = birthday;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }


    public PeopleSearchModel(){}


    @JsonProperty(value = "birthday")
    public String getBirthday() {
        return birthday;
    }
    @JsonProperty(value = "popularity")
    public String getPopularity() {
        return popularity;
    }
    @JsonProperty(value = "profile_path")
    public String getProfile_path() {
        return profile_path;
    }
}
