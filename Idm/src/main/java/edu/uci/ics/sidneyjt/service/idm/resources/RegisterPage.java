package edu.uci.ics.sidneyjt.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.idm.IDMService;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.models.register.*;
import edu.uci.ics.sidneyjt.service.idm.security.Crypto;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("register")
public class RegisterPage
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers, String jsonText)
    {
        RegisterRequestModel requestModel;
        RegisterResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);
        } catch (IOException e) {
            int resultCode;
            e.printStackTrace();
            if (e instanceof JsonParseException) {
                resultCode = -3;
                responseModel = new RegisterResponseModel(resultCode, "JSON Parse Exception");
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else if (e instanceof JsonMappingException) {
                resultCode = -2;
                responseModel = new RegisterResponseModel(resultCode, "JSON Mapping Exception");
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else {
                resultCode = -1;
                responseModel = new RegisterResponseModel(resultCode, "Internal Server Error");
                ServiceLogger.LOGGER.severe("Internal Server Error");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }
        ServiceLogger.LOGGER.info("Received post request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);


        try {
            boolean validEmail = validateEmail(requestModel.getEmail());
            boolean validPassword = validatePassword(requestModel.getPassword());
            //if email is valid, check if it is in database
            if (validEmail) {
                ServiceLogger.LOGGER.info("Valid Email.");
                if (!retrieveUserDB(requestModel.getEmail()))   //if yes, then not valid
                {
                    responseModel = new RegisterResponseModel(16, "Email already in use.");
                    ServiceLogger.LOGGER.warning("Email already in use.");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
            } else //check what requirements it did not meet
            {
                if (validateEmailLength(requestModel.getEmail())) {
                    responseModel = new RegisterResponseModel(-10, " Email address has invalid length.");
                    ServiceLogger.LOGGER.warning(" Email address has invalid length.");
                } else {
                    responseModel = new RegisterResponseModel(-11, "Email address has invalid format.");
                    ServiceLogger.LOGGER.warning("Email address has invalid format.");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            //if password is not valid, check what requirements it did not meet
            if (!validPassword) {
                if (validatePasswordLength(requestModel.getPassword())) {
                    if (requestModel.getPassword() == null || requestModel.getPassword().length == 0) {
                        responseModel = new RegisterResponseModel(-12, "Password has invalid length.");
                        ServiceLogger.LOGGER.warning("Password has invalid length");
                        return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                    } else {
                        responseModel = new RegisterResponseModel(12, "Password does not meet length requirements.");
                        ServiceLogger.LOGGER.info("Password does not meet length requirements.");
                        return Response.status(Response.Status.OK).entity(responseModel).build();
                    }
                } else {
                    responseModel = new RegisterResponseModel(13, "Password does not meet character requirements.");
                    ServiceLogger.LOGGER.info("Password does not meet character requirements");
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                }
            }
            ServiceLogger.LOGGER.info("Password meets requirements");
            //email and password are valid, input into database
            byte[] salt = Crypto.genSalt();
            byte[] hashedPassword = Crypto.SaltAndHash(requestModel.getPassword(), salt);
            String encodedSalt = Hex.encodeHexString(salt);
            String encodedPassword = Hex.encodeHexString(hashedPassword);
            ServiceLogger.LOGGER.info("Successfully encoded password");
            if (insertUser(requestModel.getEmail(), encodedPassword, encodedSalt)) {
                responseModel = new RegisterResponseModel(110, "User registered successfully.");
                ServiceLogger.LOGGER.info("User registered successfully.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            } else {
                int resultCode = -1;
                responseModel = new RegisterResponseModel(resultCode, "Internal Server Error");
                ServiceLogger.LOGGER.severe("Internal Server Error");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            int resultCode = -1;
            responseModel = new RegisterResponseModel(resultCode, "Internal Server Error");
            ServiceLogger.LOGGER.severe("Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
    private boolean insertUser(String email, String encodedPassword, String salt)
    {
        try
        {
            String Query = "INSERT INTO user(email, salt, pword, plevel, status) " +
                    "VALUES('" + email + "', '" + salt + "', '" + encodedPassword +"', 5, 2)";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying to insert into DB: " + ps.toString());
            ps.executeUpdate();       //iterator
            ServiceLogger.LOGGER.info("Insert succeeded.");
            return true;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Insert failed.");
            e.printStackTrace();
        }
        return false;
    }
    private boolean retrieveUserDB(String email)
    {
        try {
            String Query = "SELECT user_id " +
                    "FROM user " +
                    "WHERE 1=1 && email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();       //iterator
            ServiceLogger.LOGGER.info("Query succeeded.");

            if(!rs.isBeforeFirst())
            {
                ServiceLogger.LOGGER.info("Email is unique.");
                return true;
            }
            else {
                while(rs.next())
                {
                    Integer user_id = rs.getInt("user_id");
                    ServiceLogger.LOGGER.info("Email is not unique.");
                }
                return false;
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve user records.");
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkAlphanumeric(String s)
    {
        return s.matches("^[a-zA-Z0-9]*$");
    }
    private boolean validateEmailLength(String email)
    {
        return email == null || email.length() >= 50 || email.length() == 0;
    }
    private boolean validateEmail(String email)
    {
        //if email contains any space characters
        if(validateEmailLength(email))
            return false;
        for(char c: email.toCharArray())
            if(c == ' ')
                return false;

        //check if email has one '@' and has characters before and after
        //also to check if there are no '.' before '@'
        String[] split = email.split("@");
        if(split.length != 2)
            return false;
        if(split[0].equals("") || split[1].equals(""))
            return false;
        //check if it is alphanumeric
        if(!checkAlphanumeric(split[0]))
            return false;
        if(split[1].charAt(0) == '.')
        {
            return false;
        }
        for(char c: split[0].toCharArray())
            if(c == '.')
                return false;

        //check if email has correct extension
        if(email.charAt(email.length() - 1) == '.')
            return false;
        else {
            String domain = split[1];
            String[] SplitDomain = domain.split(".");
            for(String s: SplitDomain)
                if(!checkAlphanumeric(s))
                    return false;

            //check if no two '.' next to each other
            for(int i = 0; i < domain.length(); i ++)
                if(domain.charAt(i) == '.')
                    if(domain.charAt(i + 1) == '.')
                        return false;
        }
        return true;
    }
    private boolean validatePasswordLength(char[] password)
    {
        if(password == null)
            return true;
        return password.length < 7 || password.length >= 16;
    }
    private boolean validatePassword(char[] charList)
    {
        if(validatePasswordLength(charList))
            return false;
        String password = String.valueOf(charList);
        if(!checkAlphanumeric(password))
            return false;
        boolean upper = false, lower = false, number = false;
        for(char c: password.toCharArray())
        {
            if(Character.isUpperCase(c))
                upper = true;
            if(Character.isLowerCase(c))
                lower = true;
            if(Character.isDigit(c))
                number = true;
        }
        return upper && lower && number;
    }
}
