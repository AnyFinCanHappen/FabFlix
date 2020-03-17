package edu.uci.ics.sidneyjt.service.billing.resources;

import com.paypal.orders.Order;
import edu.uci.ics.sidneyjt.service.billing.core.PayPalOrderClient;
import edu.uci.ics.sidneyjt.service.billing.core.Paypal;
import edu.uci.ics.sidneyjt.service.billing.core.Util;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.base.BillingResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.order.PlaceResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.order.retrieve.*;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.Item;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.RetrieveResponseModel;
import edu.uci.ics.sidneyjt.service.billing.query.ClearQuery;
import edu.uci.ics.sidneyjt.service.billing.query.RetrieveQuery;
import edu.uci.ics.sidneyjt.service.billing.query.order.CompleteQuery;
import edu.uci.ics.sidneyjt.service.billing.query.order.OrderRetrieveQuery;
import edu.uci.ics.sidneyjt.service.billing.query.order.PlaceQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

@Path("order")
public class OrderPage
{
    @Path("place")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response place(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        RetrieveResponseModel responseModelB = new RetrieveResponseModel();
        BillingRequestModel requestModel = Util.modelMapper(jsonString, BillingRequestModel.class, responseModelB);
        if(requestModel == null)
            return responseModelB.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModelB.setResult(Result.FAILED_CART);
            ServiceLogger.LOGGER.warning("Header and Request emails do not match.");
            return responseModelB.buildResponse();
        }
        RetrieveQuery.updateResponse(responseModelB,requestModel);
        PlaceResponseModel responseModel = new PlaceResponseModel();
        if(responseModelB.getResult().getResultCode() == Result.RETRIEVE_CART.getResultCode())
        {
            Item[] items = responseModelB.getItems();
            float cartTotal = 0;
            for(Item I : items)
            {
                cartTotal += (I.getUnit_price() - I.getDiscount()) * I.getQuantity();
            }
            String cartTotalString = String.valueOf(cartTotal);
            PayPalOrderClient paypalClient = new PayPalOrderClient();
            Order order = Paypal.createPayPalOrder(paypalClient, cartTotalString);
            //ServiceLogger.LOGGER.info("PAYPAL Date: " + order.createTime());
            if(order == null){
                responseModel.setResult(Result.ORDER_CREATION_FAIL);
                return responseModel.buildResponse(email,session_id,transaction_id);
            }
            String approve_url = order.links().get(1).href();
            String token = order.id();
            PlaceQuery.updateSaleTransactions(items, token);
            responseModel.setApprove_url(approve_url);
            responseModel.setToken(token);
            responseModel.setResult(Result.ORDER_SUCCESS);
        }
        else if(responseModelB.getResult().getResultCode() == Result.CART_NOT_FOUND.getResultCode()) {
            responseModel.setResult(Result.CART_NOT_FOUND);
        }
        else{
            responseModel.setResult(Result.ORDER_CREATION_FAIL);
        }
        return responseModel.buildResponse(email, session_id, transaction_id);
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response complete(@QueryParam("token") String token, @QueryParam("payer_id") String payer_id)
    {

        BillingResponseModel responseModel = new BillingResponseModel();
        if(!CompleteQuery.checkToken(token))
        {
            responseModel.setResult(Result.TOKEN_NOT_FOUND);
            return responseModel.buildResponse();
        }
        ServiceLogger.LOGGER.info("Token Found.");
        PayPalOrderClient paypalClient = new PayPalOrderClient();
        Order order = Paypal.captureOrder(token,paypalClient);
        if(order == null) {
            responseModel.setResult(Result.ORDER_ERROR);
            return responseModel.buildResponse();
        }
        String capture_id = order.purchaseUnits().get(0).payments().captures().get(0).id();
        if(!CompleteQuery.updateTransactionTable(token, capture_id)){
            responseModel.setResult(Result.ORDER_ERROR);
            return responseModel.buildResponse();
        }
        String email = CompleteQuery.getEmail(token);
        ServiceLogger.LOGGER.info("EMail: " + email);
        if(email == null)
        {
            responseModel.setResult(Result.ORDER_ERROR);
            return responseModel.buildResponse();
        }
        else{
            ClearQuery.clearCartWithEmail(email);
        }
        responseModel.setResult(Result.ORDER_COMPLETED);
        return responseModel.buildResponse();
    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        OrderRetrieveResponseModel responseModel = new OrderRetrieveResponseModel();
        BillingRequestModel requestModel = Util.modelMapper(jsonString, BillingRequestModel.class, responseModel);
        if(requestModel == null)
            return responseModel.buildResponse();

        if(requestModel.getEmail().compareTo(email) != 0){
            responseModel.setResult(Result.FAILED_CART);
            ServiceLogger.LOGGER.warning("Header and Request emails do not match.");
            return responseModel.buildResponse();
        }

        ArrayList<String> tokenList = OrderRetrieveQuery.getTokens(requestModel.getEmail());
        if(tokenList == null){
            responseModel.setResult(Result.ORDER_DOES_NOT_EXIST);
            return responseModel.buildResponse();
        }
        PayPalOrderClient client = new PayPalOrderClient();
        try {
            ArrayList<Transaction> transactions = new ArrayList<>();
            for (String token : tokenList)
            {
                Order order = Paypal.getOrder(token, client);
                /*
                ServiceLogger.LOGGER.info("Value: " + order.purchaseUnits().get(0).amountWithBreakdown().value());
                ServiceLogger.LOGGER.info("Currency: " + order.purchaseUnits().get(0).amountWithBreakdown().currencyCode());
                ServiceLogger.LOGGER.info("Status:" + order.status());
                ServiceLogger.LOGGER.info(order.purchaseUnits().get(0).payments().captures().get(0).sellerReceivableBreakdown().paypalFee().value());
                */
                String total = order.purchaseUnits().get(0).amountWithBreakdown().value();
                String fee = order.purchaseUnits().get(0).payments().captures().get(0).sellerReceivableBreakdown().paypalFee().value();
                String capture_id = order.purchaseUnits().get(0).payments().captures().get(0).id();
                String status = order.status();
                String currency = order.purchaseUnits().get(0).amountWithBreakdown().currencyCode();
                String create_time = order.createTime();
                String update_time = order.updateTime();

                Amount amount = new Amount(total, currency);
                TransactionFee transactionFee = new TransactionFee(fee, currency);
                OrderItemModel[] items = OrderRetrieveQuery.getItems(token);

                Transaction transaction = new Transaction(capture_id, status, amount, transactionFee, create_time, update_time, items);
                transactions.add(transaction);
            }
            Transaction[] transactionArray = new Transaction[transactions.size()];
            transactionArray = transactions.toArray(transactionArray);
            responseModel.setTransactions(transactionArray);
        } catch (IOException e){
            ServiceLogger.LOGGER.warning("Paypal Error: " + e.getMessage());
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        //responseModel.setResult(Result.ORDER_RETRIEVED);
        return responseModel.buildResponse(email,session_id,transaction_id);
    }
}
