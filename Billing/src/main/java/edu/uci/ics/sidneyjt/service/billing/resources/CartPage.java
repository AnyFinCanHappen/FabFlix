package edu.uci.ics.sidneyjt.service.billing.resources;

import edu.uci.ics.sidneyjt.service.billing.BillingService;
import edu.uci.ics.sidneyjt.service.billing.core.Util;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingMovieIdQuantityRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingMovieIdRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.login.PrivilegeRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.login.PrivilegeResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.RetrieveResponseModel;
import edu.uci.ics.sidneyjt.service.billing.query.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cart")
public class CartPage
{
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response Insert(@Context HttpHeaders headers, String jsonString)
    {
        String servicePath = BillingService.getIdmConfigs().getScheme() + BillingService.getIdmConfigs().getHostName() + ":" +
                BillingService.getIdmConfigs().getPort() + BillingService.getIdmConfigs().getPath();
        String endpointPath = BillingService.getIdmConfigs().getPrivilegePath();
        //ServiceLogger.LOGGER.info(("service path: " + servicePath + endpointPath));
        BillingResponseModel responseModel = new BillingResponseModel();
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        PrivilegeRequestModel requestModelP = new PrivilegeRequestModel(email, 4);
        PrivilegeResponseModel responseModelP = Util.checkUserPLevel(servicePath, endpointPath, PrivilegeResponseModel.class, requestModelP);
        Util.setResultFromUserPLevel(responseModelP);
        if(responseModelP.getResultCode() == Result.INTERNAL_SERVER_ERROR.getResultCode()){
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModel.buildResponse();
        }
        else if(responseModelP.getResultCode() == Result.USER_NOT_FOUND.getResultCode()){
            responseModel.setResult(Result.USER_NOT_FOUND);
            return responseModel.buildResponse();
        }
        BillingMovieIdQuantityRequestModel requestModel = Util.modelMapper(jsonString, BillingMovieIdQuantityRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            return responseModel.buildResponse();
        }
        if(!Query.checkMovieId(requestModel.getMovie_id()))
        {
            responseModel.setResult(Result.FAILED_CART);
            return responseModel.buildResponse(email,session_id,transaction_id);
        }
        if(requestModel.getQuantity() <= 0){
            responseModel.setResult(Result.INVALID_QUANTITY);
            return responseModel.buildResponse();
        }
        InsertQuery.updateResponse(responseModel, requestModel);
        return responseModel.buildResponse(email, session_id, transaction_id);
    }

    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        BillingResponseModel responseModel = new BillingResponseModel();
        //ServiceLogger.LOGGER.info(jsonString);
        BillingMovieIdQuantityRequestModel requestModel = Util.modelMapper(jsonString, BillingMovieIdQuantityRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();
        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            return responseModel.buildResponse();
        }
        if(requestModel.getQuantity() <= 0){
            responseModel.setResult(Result.INVALID_QUANTITY);
            return responseModel.buildResponse(email,session_id,transaction_id);
        }
        UpdateQuery.updateResponse(responseModel, requestModel);
        return responseModel.buildResponse(email,session_id,transaction_id);
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        BillingResponseModel responseModel = new BillingResponseModel();
        //ServiceLogger.LOGGER.info(jsonString);
        BillingMovieIdRequestModel requestModel = Util.modelMapper(jsonString, BillingMovieIdRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            ServiceLogger.LOGGER.warning("Header and Request emails do not match.");
            return responseModel.buildResponse();
        }
        DeleteQuery.updateResponse(responseModel,requestModel);
        return responseModel.buildResponse(email,session_id,transaction_id);
    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        RetrieveResponseModel responseModel = new RetrieveResponseModel();
        BillingRequestModel requestModel = Util.modelMapper(jsonString, BillingRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            ServiceLogger.LOGGER.warning("Header and Request emails do not match.");
            return responseModel.buildResponse();
        }
        RetrieveQuery.updateResponse(responseModel,requestModel);
        return responseModel.buildResponse(email,session_id,transaction_id);
    }

    @Path("clear")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        BillingResponseModel responseModel = new BillingResponseModel();
        BillingRequestModel requestModel = Util.modelMapper(jsonString, BillingRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            ServiceLogger.LOGGER.warning("Header and Request emails do not match.");
            return responseModel.buildResponse();
        }
        ClearQuery.updateResponse(responseModel, requestModel);
        return responseModel.buildResponse(email,session_id, transaction_id);
    }


}
