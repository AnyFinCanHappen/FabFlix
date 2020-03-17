package edu.uci.ics.sidneyjt.service.movies.resources;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.search.BrowseRequestModel;
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

@Path("browse")
public class BrowsePage
{
    @Path("{phrase: .*}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers, @PathParam("phrase") String phrase,
                        @QueryParam("hidden") boolean hidden,
                        @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                        @QueryParam("orderby") String orderby, @QueryParam("direction") String direction)
    {
        String servicePath = Util.getServicePath();
        String endpointPath = Util.getEndpointPath();

        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        ServiceLogger.LOGGER.info("Email: " + email);
        PrivilegeRequestModel requestModelP = new PrivilegeRequestModel(email, 4);
        PrivilegeResponseModel responseModelP = Util.checkUserPLevel(servicePath, endpointPath, PrivilegeResponseModel.class, requestModelP);
        Boolean isHidden = Util.setResultFromUserPLevel(responseModelP);
        if(isHidden == null)
        {
            responseModelP.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModelP.buildResponse(email, session_id, transaction_id);
        }
        SearchBrowseResponseModel responseModel = new SearchBrowseResponseModel();
        String[] phraseList = makePhraseList(phrase);
        BrowseRequestModel requestModel = new BrowseRequestModel(phraseList, isHidden, limit, offset, orderby, direction);
        Query.executeSearchQuery(requestModel, responseModel);
        ServiceLogger.LOGGER.info(responseModel.getMessage());
        return responseModel.buildResponse(email, session_id, transaction_id);
    }

    public String[] makePhraseList(String phrase)
    {
        String[] phraseList = phrase.split(",");
        return phraseList;
    }

}
