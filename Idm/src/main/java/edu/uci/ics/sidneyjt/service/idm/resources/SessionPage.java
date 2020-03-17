package edu.uci.ics.sidneyjt.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.models.session.*;
import edu.uci.ics.sidneyjt.service.idm.helper.*;
import edu.uci.ics.sidneyjt.service.idm.security.Session;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;

@Path("session")
public class SessionPage
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers, String jsonText)
    {
        SessionRequestModel requestModel;
        SessionResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
        } catch (IOException e) {
            int resultCode;
            e.printStackTrace();
            if (e instanceof JsonParseException) {
                resultCode = -3;
                responseModel = new SessionResponseModel(resultCode, "JSON Parse Exception",null);
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else if (e instanceof JsonMappingException) {
                resultCode = -2;
                responseModel = new SessionResponseModel(resultCode, "JSON Mapping Exception",null);
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else {
                resultCode = -1;
                responseModel = new SessionResponseModel(resultCode, "Internal Server Error",null);
                ServiceLogger.LOGGER.severe("Internal Server Error");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }
        ServiceLogger.LOGGER.info("Received post request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        try
        {
            //validate email first
            if(!InfoChecker.validateEmail(requestModel.getEmail()))
            {
                if(InfoChecker.validateEmailLength(requestModel.getEmail()))
                {
                    responseModel = new SessionResponseModel(-10, " Email address has invalid length.",null);
                    ServiceLogger.LOGGER.warning(" Email address has invalid length.");
                } else {
                    responseModel = new SessionResponseModel(-11, "Email address has invalid format.",null);
                    ServiceLogger.LOGGER.warning("Email address has invalid format.");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            //validate session id
            if(!InfoChecker.validateTokenLength(requestModel.getSession_id()))
            {
                responseModel = new SessionResponseModel(-13, "Token has invalid length.",null);
                ServiceLogger.LOGGER.warning("Token has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            //email and session_id have correct syntax, now check if in DB
            ArrayList<Integer> statusList = new ArrayList<>();
            if(DBTool.retrieveUserDB(requestModel.getEmail(), statusList))   //also get status of user
            {
                 Integer status = statusList.get(0);
                 //get session data from DB
                SessionStorage storage = new SessionStorage();
                if(DBTool.getSessionData(requestModel.getEmail(), requestModel.getSession_id(), storage))
                {

                    Session session = Session.rebuildSession(storage.getEmail(), storage.getSessionID(), storage.getTimeCreated(),
                                                            storage.getLastUsed(), storage.getExprTime());

                    if(!session.checkExpired())
                    {
                        //update information
                        //DBTool.updateUserStatus(session.getEmail(), 3);
                        session.update();
                        DBTool.updateSession(session,3);
                        responseModel = new SessionResponseModel(131, "Session is expired.", null);
                        ServiceLogger.LOGGER.info("Session is expired.");
                        return Response.status(Response.Status.OK).entity(responseModel).build();
                    }
                    if(!session.checkTimeout())
                    {
                        session.update();
                        DBTool.updateSession(session,4);
                        responseModel = new SessionResponseModel(133, "Session is revoked.", null);
                        ServiceLogger.LOGGER.info("Session is revoked.");
                        return Response.status(Response.Status.OK).entity(responseModel).build();
                    }
                    if(!session.checkIfNew())
                    {
                        session.update();
                        DBTool.updateSession(session,4);
                        Session newSesssion = Session.createSession(requestModel.getEmail());
                        DBTool.updateSession(newSesssion,1);
                        responseModel = new SessionResponseModel(133, "Session is revoked.", null);
                        ServiceLogger.LOGGER.info("Session is revoked.");
                        return Response.status(Response.Status.OK).entity(responseModel).build();
                    }
                    if(storage.getStatus() == 1)
                    {
                        return changeSessionStatus(session, 1);
                    }
                    else if(storage.getStatus() == 2)
                        return changeSessionStatus(session, 2);
                    else if(storage.getStatus() == 3)
                        return changeSessionStatus(session,3);
                    else if(storage.getStatus() == 4) {
                        return changeSessionStatus(session, 4);
                    }
                    else
                        return changeSessionStatus(session, 5);
                }
                else
                {
                    //session not found
                    responseModel = new SessionResponseModel(134, "Session not found.", null);
                    ServiceLogger.LOGGER.warning("Session not found.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
            }
            else
            {
                //email doesnt exist in db.
                responseModel = new SessionResponseModel(14, "User not found.", null);
                ServiceLogger.LOGGER.warning("User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        responseModel = new SessionResponseModel(-1, "Internal Server Error",null);
        ServiceLogger.LOGGER.severe("Internal Server Error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();

    }

    private Response changeSessionStatus(Session session, Integer status)
    {
        session.update();
        DBTool.updateSession(session,status);
        if(status == 1)
        {
            SessionResponseModel responseModel = new SessionResponseModel(130, "Session is active.", String.valueOf(session.getSessionID()));
            ServiceLogger.LOGGER.info("Session is active.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else if(status == 2)
        {
            SessionResponseModel responseModel = new SessionResponseModel(132, "Session is closed.", null);
            ServiceLogger.LOGGER.info("Session is closed.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else if(status == 3)
        {
            SessionResponseModel responseModel = new SessionResponseModel(131, "Session is expired.", null);
            ServiceLogger.LOGGER.info("Session is expired.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else if(status == 4)
        {
            SessionResponseModel responseModel = new SessionResponseModel(133, "Session is revoked.", null);
            ServiceLogger.LOGGER.info("Session is revoked.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else
        {
            SessionResponseModel responseModel = new SessionResponseModel(-1, "Internal Server Error",null);
            ServiceLogger.LOGGER.severe("Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
    private void printSessionStuff(SessionStorage storage)
    {
        ServiceLogger.LOGGER.info("POggers");
        ServiceLogger.LOGGER.info("Email: " + storage.getEmail());
        ServiceLogger.LOGGER.info("session_id: " + storage.getSessionID());
        ServiceLogger.LOGGER.info("status: " + storage.getStatus());
        ServiceLogger.LOGGER.info("time_created: " + storage.getTimeCreated());
        ServiceLogger.LOGGER.info("last_used: " + storage.getLastUsed());
        ServiceLogger.LOGGER.info("expr_time: " + storage.getExprTime());
    }
}
