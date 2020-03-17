package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingMovieIdQuantityRequestModel;

public class UpdateQuery
{
    private static String updateQuery(BillingMovieIdQuantityRequestModel requestModel)
    {
        return "UPDATE cart \n" +
                "SET quantity = " + requestModel.getQuantity() + "\n" +
                "WHERE 1=1 && movie_id = '" + requestModel.getMovie_id() + "' && \n" +
                "email = '" + requestModel.getEmail() + "';";
    }

    public static void updateResponse(BillingResponseModel responseModel, BillingMovieIdQuantityRequestModel requestModel)
    {
        String query = updateQuery(requestModel);
        Integer rows = Query.modifyTable(query);
        if(rows == 0)
        {
            responseModel.setResult(Result.CART_NOT_FOUND);
        }
        else if(rows == -1){
            responseModel.setResult(Result.FAILED_CART);
        }
        else if(rows == -2){
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else{
            responseModel.setResult(Result.UPDATE_CART);
        }
    }
}
