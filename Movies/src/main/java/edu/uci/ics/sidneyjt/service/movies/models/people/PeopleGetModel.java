package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PeopleGetModel extends PeopleSearchModel
{

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "biography")
    private String biography;

    @JsonProperty(value = "birthplace")
    private String birthplace;


    @JsonCreator
    public PeopleGetModel(@JsonProperty(value = "person_id") String person_id,
                          @JsonProperty(value = "name") String name,
                          @JsonProperty(value = "birthday") String birthday,
                          @JsonProperty(value = "popularity") String popularity,
                          @JsonProperty(value = "profile_path") String profile_path,
                          @JsonProperty(value = "gender") String gender,
                          @JsonProperty(value = "biography") String biography,
                          @JsonProperty(value = "birthplace") String birthplace) {
        super(person_id, name, birthday, popularity, profile_path);
        this.gender = gender;
        this.biography = biography;
        this.birthplace = birthplace;
    }

    public PeopleGetModel(){}

    @JsonProperty(value = "gender")
    public String getGender() {
        return gender;
    }
    @JsonProperty(value = "biography")
    public String getBiography() {
        return biography;
    }
    @JsonProperty(value = "birthplace")
    public String getBirthplace() {
        return birthplace;
    }


}
