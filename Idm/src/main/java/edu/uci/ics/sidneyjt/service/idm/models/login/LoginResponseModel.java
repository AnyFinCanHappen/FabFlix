package edu.uci.ics.sidneyjt.service.idm.models.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseModel
{
    @JsonProperty(value = "resultCode", required = true)
    private Integer resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonProperty(value = "session_id")
    private String session_id;

    @JsonCreator
    public LoginResponseModel(@JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                 @JsonProperty(value = "message", required = true) String message,
                              @JsonProperty(value = "session_id") String session_id)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.session_id = session_id;
    }
    @JsonProperty(value = "resultCode")
    public Integer getResultCode() {
        return resultCode;
    }
    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }
    @JsonProperty(value = "session_id")
    public String getSession_id() {
        return session_id;
    }
}
