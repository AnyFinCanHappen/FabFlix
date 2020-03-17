package edu.uci.ics.sidneyjt.service.billing.models.base;
import javax.ws.rs.core.Response.Status;

public enum Result
{
    JSON_PARSE_EXCEPTION   (-3, "JSON Parse Exception.",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),
    USER_NOT_FOUND          (14, "User not found.", Status.OK),
    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),
    SUFFICIENT_P_LEVEL  (140, "Sufficient P_Level.",  Status.OK),
    INSUFFICIENT_P_LEVEL  (141, "Insufficient P_Level.",  Status.OK),
    FOUND_MOVIE             (210, "Found movie(s) with search parameters.",  Status.OK),
    NO_MOVIE_FOUND         (211, "No movies found with search parameters.",   Status.OK),
    PERSON_FOUND            (212, "Found people with search parameters.", Status.OK),
    NO_PERSON_FOUND         (213, "No people found with search parameters.", Status.OK),


    DUPLICATE_INSERTION     (311, "Duplicate insertion.", Status.OK),
    CART_NOT_FOUND (312, " Shopping cart item does not exist.", Status.OK),
    ORDER_DOES_NOT_EXIST (313, "Order history does not exist.", Status.OK),
    ORDER_CREATION_FAIL (342, "Order creation failed.", Status.OK),
    INSERT_CART (3100, "Shopping cart item inserted successfully.", Status.OK),
    UPDATE_CART (3110, "Shopping cart item updated successfully.", Status.OK),
    DELETE_CART (3120, "Shopping cart item deleted successfully.", Status.OK),
    RETRIEVE_CART (3130, "Shopping cart retrieved successfully.", Status.OK),
    CLEARED_CART (3140, "Shopping cart cleared successfully.", Status.OK),
    FAILED_CART (3150, "Shopping cart operation failed.", Status.OK),
    ORDER_SUCCESS (3400, "Order placed successfully.", Status.OK),
    ORDER_RETRIEVED (3410, "Order retrieved successfully.", Status.OK),
    ORDER_COMPLETED (3420, "Order is completed successfully.", Status.OK),
    TOKEN_NOT_FOUND (3421, "Token not found.", Status.OK),
    ORDER_ERROR (3422, "Order can not be completed.", Status.OK),
    INVALID_QUANTITY (33, "Quantity has invalid value.", Status.OK);


    private final int resultCode;
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
