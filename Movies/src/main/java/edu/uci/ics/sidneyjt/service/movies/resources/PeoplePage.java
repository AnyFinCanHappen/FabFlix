package edu.uci.ics.sidneyjt.service.movies.resources;

import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeRequestModel;
import edu.uci.ics.sidneyjt.service.movies.models.login.PrivilegeResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.people.*;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseResponseModel;
import edu.uci.ics.sidneyjt.service.movies.query.PeopleGetQuery;
import edu.uci.ics.sidneyjt.service.movies.query.PeopleQuery;
import edu.uci.ics.sidneyjt.service.movies.query.PeopleSearchQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("people")
public class PeoplePage
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response people(@Context HttpHeaders headers, @QueryParam("name") String name,
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
        ServiceLogger.LOGGER.info("isHidden: " + isHidden);
        if(isHidden == null)
        {
            responseModelP.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModelP.buildResponse(email, session_id, transaction_id);
        }

        SearchBrowseResponseModel responseModel = new SearchBrowseResponseModel();
        PeopleRequestQuery requestModel = new PeopleRequestQuery(name, limit, offset, orderby, direction, isHidden);
        PeopleQuery.updatePeopleResponseModel(requestModel, responseModel);
        return  responseModel.buildResponse(email, session_id, transaction_id);
    }

    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@Context HttpHeaders headers, @QueryParam("name") String name,
                           @QueryParam("birthday") String birthday, @QueryParam("movie_title") String movie_title,
                           @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                           @QueryParam("orderby") String orderby, @QueryParam("direction") String direction)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        PeopleSearchResponseModel responseModel = new PeopleSearchResponseModel();
        try {
            PeopleSearchRequestQuery requestModel = new PeopleSearchRequestQuery(name, birthday, movie_title, limit, offset, orderby, direction, false);
            PeopleSearchQuery.updatePeopleSearchResponseModel(requestModel, responseModel);
            return responseModel.buildResponse(email, session_id, transaction_id);
        }catch (Exception e)
        {
            ServiceLogger.LOGGER.info("Error in people/search page: " + e.getMessage());
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModel.buildResponse(email, session_id, transaction_id);
        }

    }

    @Path("get/{person_id: .*}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers, @PathParam("person_id") String person_id)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        PeopleGetResponseModel responseModel = new PeopleGetResponseModel();
        try
        {
            PeopleGetQuery.updatePeopleGetResponseModel(responseModel, person_id);
            return responseModel.buildResponse(email, session_id, transaction_id);
        } catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("Error in person/get: ");
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModel.buildResponse(email, session_id, transaction_id);
        }
    }

}
