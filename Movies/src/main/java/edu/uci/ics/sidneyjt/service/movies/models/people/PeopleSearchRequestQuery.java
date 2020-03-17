package edu.uci.ics.sidneyjt.service.movies.models.people;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PeopleSearchRequestQuery extends PeopleRequestQuery
{
    @JsonProperty(value = "birthday")
    private String birthday;

    @JsonProperty(value = "movie_title")
    private String movie_title;

    public PeopleSearchRequestQuery(String name, String birthday, String movie_title, Integer limit, Integer offset, String orderby, String direction, Boolean hidden) {
        super(name, limit, offset, orderby, direction, hidden);
        this.birthday = birthday;
        this.movie_title = movie_title;
    }

    @JsonProperty(value = "birthday")
    public String getBirthday() {
        return birthday;
    }
    @JsonProperty(value = "movie_title")
    public String getMovie_title() {
        return movie_title;
    }
}
