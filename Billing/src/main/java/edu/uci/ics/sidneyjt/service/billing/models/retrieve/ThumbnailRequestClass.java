package edu.uci.ics.sidneyjt.service.billing.models.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.RequestModel;

public class ThumbnailRequestClass extends RequestModel
{
    @JsonProperty(value = "movie_ids", required = true)
    private String[] movie_ids;

    @JsonCreator
    public ThumbnailRequestClass(@JsonProperty(value = "movie_ids", required = true) String[] movie_ids) {
        this.movie_ids = movie_ids;
    }
    @JsonProperty(value = "movie_ids")
    public String[] getMovie_ids() {
        return movie_ids;
    }
}
