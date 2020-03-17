package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingRequestModel;

public class ClearQuery
{
    private static String constructQuery(String email)
    {
        return "DELETE FROM cart WHERE email = '" + email + "';";
    }

    public static void updateResponse(BillingResponseModel responseModel, BillingRequestModel requestModel)
    {
        String query = constructQuery(requestModel.getEmail());
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
            responseModel.setResult(Result.CLEARED_CART);
        }
    }

    public static void clearCartWithEmail(String email)
    {
        String query = constructQuery(email);
        Query.modifyTable(query);
    }


}
