package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.BillingService;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingMovieIdQuantityRequestModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertQuery
{
    private static String constructQuery(BillingMovieIdQuantityRequestModel requestModel)
    {
        return "INSERT INTO cart(email, movie_id, quantity) \n" +
                "VALUES ('" + requestModel.getEmail() + "', '" + requestModel.getMovie_id() + "', " + requestModel.getQuantity()
                + ");";
    }



    public static void updateResponse(BillingResponseModel responseModel, BillingMovieIdQuantityRequestModel requestModel)
    {
        String query = constructQuery(requestModel);
        Integer rows = Query.modifyTable(query);
        if(rows == -1)
        {
            responseModel.setResult(Result.DUPLICATE_INSERTION);
        }
        else if(rows == -2){
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else if(rows == 0){
            responseModel.setResult(Result.FAILED_CART);
        }
        else{
            responseModel.setResult(Result.INSERT_CART);
        }
    }
}
