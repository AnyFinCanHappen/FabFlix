package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingMovieIdRequestModel;

public class DeleteQuery
{
    private static String constructQuery(BillingMovieIdRequestModel requestModel)
    {
        return "DELETE FROM cart WHERE movie_id = '" + requestModel.getMovie_id() + "' && email = '" +
                requestModel.getEmail() + "';";
    }

    public static void updateResponse(BillingResponseModel responseModel, BillingMovieIdRequestModel requestModel)
    {
        String query = constructQuery(requestModel);
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
            responseModel.setResult(Result.DELETE_CART);
        }
    }

}
