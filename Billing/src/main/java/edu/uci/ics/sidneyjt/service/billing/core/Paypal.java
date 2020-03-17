package edu.uci.ics.sidneyjt.service.billing.core;

import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.BillingService;

import com.braintreepayments.http.serializer.Json;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.exceptions.HttpException;
import com.paypal.orders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Paypal
{
    //intialize sandbox stuff

    private static final String clientId = "AfcTqkC-tlHdIcZfoRJf3bmhAIpvGgrj_n_1Rz2QOKG8Ock8Jn3eYGwARwYueE3CLU7hgSffIbVMBKbO";
    private static final String clientSecret = "EC_soyHt91Eit_RHleCyM5RCygAsSXO3uJxZY1mdNSb8N6gXNinwJbk7TMLxm7hsIyPiA5Brawf-RuEy";

    //setup paypal envrionment
    public static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    //Create client for environment
    private static PayPalHttpClient client = new PayPalHttpClient(environment);

    /*
    public static void main(String args[])
    {
        PayPalOrderClient ppOrderclient = new PayPalOrderClient();
        createPayPalOrder(ppOrderclient);

        System.out.println("Capturing order");
        try {
            captureOrder("6Y052301VR390470V");
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }

     */
    public static Order createPayPalOrder(PayPalOrderClient client, String price)
    {
        Order order = null;

        //Construct a request object and set desired parameters
        //Here orderscreaterequest creates a post request to v2/checkout/orders

        OrderRequest orderRequest = new OrderRequest();

        //MUST use this method instead of intent to create capture.
        orderRequest.checkoutPaymentIntent("CAPTURE");

        //Create application context with return url upon payer completion.
        String baseURL = BillingService.getBillingConfigs().getScheme() + BillingService.getBillingConfigs().getHostName() + ":" +
                          BillingService.getBillingConfigs().getPort() + BillingService.getBillingConfigs().getPath()
                        +"order/complete";
        ApplicationContext applicationContext = new ApplicationContext().returnUrl(baseURL);


        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(price)));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            //System.out.println("Order ID: " + order.id());
            ServiceLogger.LOGGER.info("Order ID: " + order.id());
            order.links().forEach(link -> ServiceLogger.LOGGER.info(link.rel() + " => " + link.method() + ":" + link.href()));
            //ServiceLogger.LOGGER.info(order.links().get(1).href());
            return order;
        } catch (IOException ioe) {
            System.err.println("*******COULD NOT CREATE ORDER*******");
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                //System.out.println(he.getMessage());
                ServiceLogger.LOGGER.info(he.getMessage());
                he.headers().forEach(x -> ServiceLogger.LOGGER.warning(x + " :" + he.headers().header(x)));
            } else {

                ServiceLogger.LOGGER.warning("Paypal Error.");
                // Something went wrong client-side
            }
            return null;
        }

    }

    public static Order captureOrder(String orderID, PayPalOrderClient orderClient)
    {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);
        try {

            // Call API with your client and get a response for your call
            HttpResponse<Order> response = orderClient.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            ServiceLogger.LOGGER.info("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> ServiceLogger.LOGGER.info(link.rel() + " => " + link.method() + ":" + link.href()));
            return order;
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                ServiceLogger.LOGGER.info(he.getMessage());
                he.headers().forEach(x -> ServiceLogger.LOGGER.info(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
                ServiceLogger.LOGGER.warning("Paypal Error.");
            }
            return null;
        }

    }

    public static Order getOrder(String orderId, PayPalOrderClient orderClient) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = orderClient.client.execute(request);
        ServiceLogger.LOGGER.info("Full response body:" + (new Json().serialize(response.result())));
        return response.result();
        // System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    }

    public static void captureOrder(String orderID)
    {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);

        try {

            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Payer ID: " + order.payer().payerId());

            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.payer().payerId();
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
            }
        }

    }

    /**
     * Method to perform sample GET on an order
     *
     * @throws IOException Exceptions from API if any
     */
    public static void getOrder(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = client.execute(request);
        System.out.println("Full response body:" + (new Json().serialize(response.result())));
       // System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    }
}
