package edu.uci.ics.sidneyjt.service.movies.models.search;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchBrowseRequestModel extends SearchBrowseRequestBase
{
    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "year")
    private Integer year;

    @JsonProperty(value = "director")
    private String director;

    @JsonProperty(value = "genre")
    private String genre;

    @JsonCreator
    public SearchBrowseRequestModel(@JsonProperty(value = "title") String title,
                                    @JsonProperty(value = "year") Integer year,
                                    @JsonProperty(value = "director") String director,
                                    @JsonProperty(value = "genre") String genre,
                                    @JsonProperty(value = "hidden") Boolean hidden,
                                    @JsonProperty(value = "limit") Integer limit,
                                    @JsonProperty(value = "offset") Integer offset,
                                    @JsonProperty(value = "orderby") String orderby,
                                    @JsonProperty(value = "direction")String direction)
    {
        super(hidden,limit, offset, orderby,direction);
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre = genre;
    }

    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
    }
    @JsonProperty(value = "year")
    public Integer getYear() {
        return year;
    }
    @JsonProperty(value = "director")
    public String getDirector() {
        return director;
    }
    @JsonProperty(value = "genre")
    public String getGenre() {
        return genre;
    }

}
