package edu.uci.ics.sidneyjt.service.billing.models.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ThumbnailResponseClass
{
    @JsonProperty(value = "thumbnails", required = true)
    private Thumbnail[] thumbnails;

    @JsonProperty(value = "resultCode", required = true)
    private Integer resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;
    /*
    @JsonCreator
    public ThumbnailResponseClass(@JsonProperty(value = "thumbnails", required = true) Thumbnail[] thumbnails) {
        this.thumbnails = thumbnails;
    }
     */
    @JsonCreator
    public ThumbnailResponseClass(@JsonProperty(value = "thumbnails", required = true) Thumbnail[] thumbnails,
                                  @JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                  @JsonProperty(value = "message", required = true) String message) {
        this.thumbnails = thumbnails;
        this.resultCode = resultCode;
        this.message = message;
    }
    @JsonCreator
    public ThumbnailResponseClass() {}
    @JsonProperty(value = "resultCode")
    public Integer getResultCode() {
        return resultCode;
    }
    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "thumbnails")
    public Thumbnail[] getThumbnails() {
        return thumbnails;
    }

}
