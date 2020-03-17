package edu.uci.ics.sidneyjt.service.idm.resources;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.idm.IDMService;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.models.login.*;
import edu.uci.ics.sidneyjt.service.idm.models.register.RegisterResponseModel;
import edu.uci.ics.sidneyjt.service.idm.security.Crypto;
import edu.uci.ics.sidneyjt.service.idm.helper.*;
import edu.uci.ics.sidneyjt.service.idm.security.Session;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("login")
public class LoginPage
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers, String jsonText)
    {
        LoginRequestModel requestModel;
        LoginResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, LoginRequestModel.class);
        } catch (IOException e) {
            int resultCode;
            e.printStackTrace();
            if (e instanceof JsonParseException) {
                resultCode = -3;
                responseModel = new LoginResponseModel(resultCode, "JSON Parse Exception",null);
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else if (e instanceof JsonMappingException) {
                resultCode = -2;
                responseModel = new LoginResponseModel(resultCode, "JSON Mapping Exception",null);
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else {
                resultCode = -1;
                responseModel = new LoginResponseModel(resultCode, "Internal Server Error",null);
                ServiceLogger.LOGGER.severe("Internal Server Error");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }
        ServiceLogger.LOGGER.info("Received post request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        try
        {
            if(InfoChecker.validatePasswordLength(requestModel.getPassword()))
            {
                responseModel = new LoginResponseModel(-12, "Password has invalid length.",null);
                ServiceLogger.LOGGER.warning("Password has invalid length");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            if(!InfoChecker.validateEmail(requestModel.getEmail()))
            {
                if(InfoChecker.validateEmailLength(requestModel.getEmail()))
                {
                    responseModel = new LoginResponseModel(-10, " Email address has invalid length.",null);
                    ServiceLogger.LOGGER.warning(" Email address has invalid length.");
                } else {
                    responseModel = new LoginResponseModel(-11, "Email address has invalid format.",null);
                    ServiceLogger.LOGGER.warning("Email address has invalid format.");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            //email and password have correct syntax, now look up in DB
            ArrayList<Integer> statusList = new ArrayList<>();
            if(DBTool.retrieveUserDB(requestModel.getEmail(), statusList))
            {
                Integer status = statusList.get(0);
                ServiceLogger.LOGGER.info("Status: " + status);
                if(DBTool.checkPassword(requestModel.getEmail(),requestModel.getPassword()))
                {
                    Session session = Session.createSession(requestModel.getEmail());
                    if(checkIfSessionExist(requestModel.getEmail()))
                    {
                        SessionStorage storage = new SessionStorage();
                        if(DBTool.getSessionData(requestModel.getEmail(), 1, storage))
                        {
                            session = Session.rebuildSession(storage.getEmail(), storage.getSessionID(), storage.getTimeCreated(), storage.getLastUsed(), storage.getExprTime());
                        }
                    }
                    //if user status is active then revoke
                    if(status == 1)
                    {
                        updateSession(session, 1);
                    }
                    //create session if it doesnt exist or update to active
                    else
                    {
                        updateSession(session,status);
                    }
                    responseModel = new LoginResponseModel(120, "User logged in successfully.", String.valueOf(session.getSessionID()));
                    ServiceLogger.LOGGER.info("User logged in successfully.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
                else
                {
                    responseModel = new LoginResponseModel(11, "Passwords do not match.", null);
                    ServiceLogger.LOGGER.warning("Passwords do not match.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }

            }
            else
            {
                responseModel = new LoginResponseModel(14, "User not found.", null);
                ServiceLogger.LOGGER.warning("User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            int resultCode = -1;
            responseModel = new LoginResponseModel(resultCode, "Internal Server Error",null);
            ServiceLogger.LOGGER.severe("Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
    private void updateSession(Session session, int status)
    {
        try
        {
            if(status == 1)
            {
                session.update();
                String Query = "UPDATE session " +
                        "SET status = " + 4 + ", time_created = '" + session.getTimeCreated() +
                        "', last_used = '" + session.getLastUsed() + "', expr_time =  '" + session.getExprTime() + "'" +
                        "WHERE 1=1 && email LIKE '" + session.getEmail() + "' &&  session_id LIKE '" + session.getSessionID() + "';";
                PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
                ServiceLogger.LOGGER.info("Trying Update: " + ps.toString());
                ps.executeUpdate();       //iterator
                ServiceLogger.LOGGER.info("Update succeeded.");
                Session newSession = Session.createSession(session.getEmail());
                DBTool.updateUserStatus(session.getEmail(),1);
                DBTool.insertSession(newSession, 1);
            }
            else
            {
                if(checkIfSessionExist(session.getEmail()))
                {
                    session.update();
                    /*
                    String Query = "UPDATE session " +
                            "SET session_id = '" + session.getSessionID() + "', status = " + 1 + ", time_created = '" + session.getTimeCreated() +
                            "', last_used = '" + session.getLastUsed() + "', expr_time =  '" + session.getExprTime() + "'" +
                            "WHERE 1=1 && email LIKE '" + session.getEmail() + "';";
                     */
                    String Query = "UPDATE session " +
                            "SET status = " + 4 + ", time_created = '" + session.getTimeCreated() +
                            "', last_used = '" + session.getLastUsed() + "', expr_time =  '" + session.getExprTime() + "'" +
                            "WHERE 1=1 && email LIKE '" + session.getEmail() + "' &&  session_id LIKE '" + session.getSessionID() + "';";
                    PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
                    ServiceLogger.LOGGER.info("Trying Update: " + ps.toString());
                    ps.executeUpdate();       //iterator
                    ServiceLogger.LOGGER.info("Update succeeded.");
                }
                else  //Is this needed???
                {
                    String Query = "INSERT INTO session (session_id, email, status, time_created, last_used, expr_time)"
                            + "VALUES ('" + session.getSessionID() + "', '" + session.getEmail() + "', " + 1 + ", '"
                            + session.getTimeCreated() + "', '" + session.getLastUsed() + "', '" + session.getExprTime() + "');";
                    PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
                    ServiceLogger.LOGGER.info("Trying Insert: " + ps.toString());
                    ps.executeUpdate();       //iterator
                    ServiceLogger.LOGGER.info("Insert succeeded.");
                }
                DBTool.updateUserStatus(session.getEmail(),1);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkIfSessionExist(String email)
    {
        try
        {
            String Query = "SELECT session_id " +
                            "FROM session "+
                            "WHERE 1=1 && email LIKE ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded (checkIfSessionExist).");
            if(!rs.isBeforeFirst())
                return false;
            else
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
