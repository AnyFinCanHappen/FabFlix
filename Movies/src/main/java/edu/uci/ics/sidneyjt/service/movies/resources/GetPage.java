package edu.uci.ics.sidneyjt.service.movies.resources;

import edu.uci.ics.sidneyjt.service.movies.MoviesService;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.get.GetResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeRequestModel;
import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("get")
public class GetPage
{
    @Path("{movie_id: .*}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers, @PathParam("movie_id") String movie_id)
    {
        String servicePath = Util.getServicePath();
        String endpointPath = Util.getEndpointPath();

        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        PrivilegeRequestModel requestModelP = new PrivilegeRequestModel(email, 4);
        PrivilegeResponseModel responseModelP = Util.checkUserPLevel(servicePath, endpointPath, PrivilegeResponseModel.class, requestModelP);
        Boolean isHidden = Util.setResultFromUserPLevel(responseModelP);
        if(isHidden == null)
        {
            responseModelP.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModelP.buildResponse(email, session_id, transaction_id);
        }
        GetResponseModel responseModel = new GetResponseModel();
        Query.updateGetResponseModel(responseModel, movie_id, isHidden);
        return responseModel.buildResponse(email, session_id, transaction_id);
    }

}
