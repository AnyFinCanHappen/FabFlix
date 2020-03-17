package edu.uci.ics.sidneyjt.service.movies.resources;

import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseRequestModel;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeRequestModel;
import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("search")
public class SearchPage
{

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers, @QueryParam("title") String title,
                        @QueryParam("year") Integer year, @QueryParam("director") String director,
                        @QueryParam("genre") String genre, @QueryParam("hidden") boolean hidden,
                        @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                        @QueryParam("orderby") String orderby, @QueryParam("direction") String direction)
    {

        String servicePath = Util.getServicePath();
        String endpointPath = Util.getEndpointPath();

        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        PrivilegeRequestModel requestModelP = new PrivilegeRequestModel(email, 4);
        PrivilegeResponseModel responseModelP = Util.checkUserPLevel(servicePath, endpointPath, PrivilegeResponseModel.class, requestModelP);
        Boolean isHidden = Util.setResultFromUserPLevel(responseModelP);
        ServiceLogger.LOGGER.info("isHidden: " + isHidden);
        if(isHidden == null)
        {
            responseModelP.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModelP.buildResponse(email, session_id, transaction_id);
        }

        SearchBrowseResponseModel responseModel = new SearchBrowseResponseModel();
        SearchBrowseRequestModel requestModel = new SearchBrowseRequestModel(title, year, director, genre, isHidden, limit, offset, orderby, direction);
        Query.executeSearchQuery(requestModel, responseModel);
        return responseModel.buildResponse(email, session_id, transaction_id);
    }
}
