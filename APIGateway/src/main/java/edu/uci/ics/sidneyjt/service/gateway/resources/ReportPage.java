package edu.uci.ics.sidneyjt.service.gateway.resources;

import edu.uci.ics.sidneyjt.service.gateway.GatewayService;
import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.gateway.models.Result;
import edu.uci.ics.sidneyjt.service.gateway.models.SessionResponseModel;
import edu.uci.ics.sidneyjt.service.gateway.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;


@Path("report")
public class ReportPage
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@Context HttpHeaders headers)
    {
        try
        {
            /*
            SessionResponseModel sessionResponseModel = GatewayPage.checkSession(headers.getHeaderString("email"), headers.getHeaderString("session_id"));
            if(sessionResponseModel.getResultCode() != Result.SESSION_ACTIVE.getResultCode()) {
                ServiceLogger.LOGGER.info("Session: " + headers.getHeaderString("session_id") + " is invalid.");
                return Response.status(Response.Status.OK).entity(sessionResponseModel).build();
            }
             */
            ServiceLogger.LOGGER.info("Report endpoint: " + headers.getHeaderString("transaction_id"));
            if(headers.getHeaderString("transaction_id") != null) {
                Connection con = GatewayService.getConnectionPoolManager().requestCon();
                Response.ResponseBuilder build = Query.checkResponseTable(headers.getHeaderString("transaction_id"), con);
                GatewayService.getConnectionPoolManager().releaseCon(con);
                if(build != null) {
                    con = GatewayService.getConnectionPoolManager().requestCon();
                    Query.deleteResponse(headers.getHeaderString("transaction_id"), con);
                    GatewayService.getConnectionPoolManager().releaseCon(con);
                    ServiceLogger.LOGGER.info("SENDING RESPONSE BACK FROM REPORT!!");
                    ServiceLogger.LOGGER.info(build.build().getEntity().toString());
                    build.header("transaction_id", headers.getHeaderString("transaction_id"));
                    return build.build();
                }
            }
            Response.ResponseBuilder build = GatewayPage.builder(headers.getHeaderString("email"), headers.getHeaderString("session_id"), headers.getHeaderString("transaction_id"));
            return build.build();

        }catch (Exception e)
        {
            e.printStackTrace();
            Response.ResponseBuilder build = GatewayPage.builder(headers.getHeaderString("email"), headers.getHeaderString("session_id"), headers.getHeaderString("transaction_id"));
            return build.build();
        }
    }
}
