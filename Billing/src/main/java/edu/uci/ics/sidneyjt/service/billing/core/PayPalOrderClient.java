package edu.uci.ics.sidneyjt.service.billing.core;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PayPalOrderClient
{
    private final String clientId = "AfcTqkC-tlHdIcZfoRJf3bmhAIpvGgrj_n_1Rz2QOKG8Ock8Jn3eYGwARwYueE3CLU7hgSffIbVMBKbO";
    private final String clientSecret = "EC_soyHt91Eit_RHleCyM5RCygAsSXO3uJxZY1mdNSb8N6gXNinwJbk7TMLxm7hsIyPiA5Brawf-RuEy";

    //setup paypal envrionment
    public PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    //Create client for environment
    public PayPalHttpClient client = new PayPalHttpClient(environment);
}
