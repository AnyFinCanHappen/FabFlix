package edu.uci.ics.sidneyjt.service.movies.models.login;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.movies.models.base.RequestModel;

public class PrivilegeRequestModel extends RequestModel
{
    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "plevel", required = true)
    private Integer plevel;

    @JsonCreator
    public PrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                 @JsonProperty(value = "plevel", required = true) Integer plevel) {
        this.email = email;
        this.plevel = plevel;
    }

    public PrivilegeRequestModel(){}

    @JsonProperty(value = "email", required = true)
    public String getEmail() {
        return email;
    }
    @JsonProperty(value = "plevel", required = true)
    public Integer getPlevel() {
        return plevel;
    }
}
