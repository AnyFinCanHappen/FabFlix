package edu.uci.ics.sidneyjt.service.idm.resources;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.models.login.LoginResponseModel;
import edu.uci.ics.sidneyjt.service.idm.models.privilege.*;
import edu.uci.ics.sidneyjt.service.idm.helper.*;
import edu.uci.ics.sidneyjt.service.idm.security.Session;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;

@Path("privilege")
public class PrivilegePage
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers, String jsonText)
    {
        PrivilegeRequestModel requestModel;
        PrivilegeResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
        } catch (IOException e) {
            int resultCode;
            e.printStackTrace();
            if (e instanceof JsonParseException) {
                resultCode = -3;
                responseModel = new PrivilegeResponseModel(resultCode, "JSON Parse Exception");
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else if (e instanceof JsonMappingException) {
                resultCode = -2;
                responseModel = new PrivilegeResponseModel(resultCode, "JSON Mapping Exception");
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else {
                resultCode = -1;
                responseModel = new PrivilegeResponseModel(resultCode, "Internal Server Error");
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
                    responseModel = new PrivilegeResponseModel(-10, "Email address has invalid length.");
                    ServiceLogger.LOGGER.warning(" Email address has invalid length.");
                } else {
                    responseModel = new PrivilegeResponseModel(-11, "Email address has invalid format.");
                    ServiceLogger.LOGGER.warning("Email address has invalid format.");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            int privilegeRequest = requestModel.getPlevel();
            if(privilegeRequest > 5 || privilegeRequest < 1)
            {
                responseModel = new PrivilegeResponseModel(-14, "Privilege level out of valid range.");
                ServiceLogger.LOGGER.warning("Privilege level out of valid range.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            UserStorage storage = new UserStorage();
            if(DBTool.getUserData(requestModel.getEmail(), storage))
            {
                if(storage.getPlevel() <= privilegeRequest)
                {
                    responseModel = new PrivilegeResponseModel(140, "User has sufficient privilege level.");
                    ServiceLogger.LOGGER.info("User has sufficient privilege level.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
                else
                {
                    responseModel = new PrivilegeResponseModel(141, "User has insufficient privilege level.");
                    ServiceLogger.LOGGER.info("User has insufficient privilege level.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
            }
            else
            {
                responseModel = new PrivilegeResponseModel(14, "User not found.");
                ServiceLogger.LOGGER.warning("User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
        responseModel = new PrivilegeResponseModel(-1, "Internal Server Error");
        ServiceLogger.LOGGER.severe("Internal Server Error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();

    }


}
