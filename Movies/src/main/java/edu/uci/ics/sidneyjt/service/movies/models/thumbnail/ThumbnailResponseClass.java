package edu.uci.ics.sidneyjt.service.movies.models.thumbnail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;

public class ThumbnailResponseClass extends ResponseModel
{
    @JsonProperty(value = "thumbnails", required = true)
    private Thumbnail[] thumbnails;

    @JsonCreator
    public ThumbnailResponseClass(Thumbnail[] thumbnails) {
        this.thumbnails = thumbnails;
    }
    @JsonCreator
    public ThumbnailResponseClass() {}

    @JsonProperty(value = "thumbnails")
    public Thumbnail[] getThumbnails() {
        return thumbnails;
    }
    @JsonIgnore
    public void setThumbnails(Thumbnail[] thumbnails)
    {
        if(thumbnails == null || thumbnails.length == 0)
            this.setResult(Result.NO_MOVIE_FOUND);
        else
            this.setResult(Result.FOUND_MOVIE);
        this.thumbnails = thumbnails;
    }
}
