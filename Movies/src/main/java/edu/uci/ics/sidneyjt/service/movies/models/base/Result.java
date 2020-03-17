package edu.uci.ics.sidneyjt.service.movies.models.base;
import javax.ws.rs.core.Response.Status;

public enum Result
{
    JSON_PARSE_EXCEPTION   (-3, "JSON Parse Exception.",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),
    SUFFICIENT_P_LEVEL  (140, "Sufficient P_Level.",  Status.OK),
    INSUFFICIENT_P_LEVEL  (141, "Insufficient P_Level.",  Status.OK),
    FOUND_MOVIE             (210, "Found movie(s) with search parameters.",  Status.OK),
    NO_MOVIE_FOUND         (211, "No movies found with search parameters.",   Status.OK),
    PERSON_FOUND            (212, "Found people with search parameters.", Status.OK),
    NO_PERSON_FOUND         (213, "No people found with search parameters.", Status.OK);

    private final int    resultCode;
    private final String message;
    private final Status status;

    Result(int resultCode, String message, Status status)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    public int getResultCode()
    {
        return resultCode;
    }

    public String getMessage()
    {
        return message;
    }

    public Status getStatus()
    {
        return status;
    }
}
