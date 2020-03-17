package edu.uci.ics.sidneyjt.service.gateway.query;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.gateway.threadpool.ClientRequest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query
{
    private static Integer modifyTable(String Query, Connection con) throws Exception
    {
        try {
            ServiceLogger.LOGGER.info(Query);
            PreparedStatement ps = con.prepareStatement(Query);
            Integer row = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query Succeeded.");
            return row;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: " + e.getMessage());
            throw e;

        }
        catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("System Error.");
            throw e;
        }
    }

    public static ResultSet makeResultSet(String Query, Connection con)
    {
        try {
            ServiceLogger.LOGGER.info(Query);
            PreparedStatement ps = con.prepareStatement(Query);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeeded.");
            return rs;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: " + e.getMessage());
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String insertQuery(ClientRequest client, String jsonText,Integer http_status)
    {

        jsonText = jsonText.replaceAll("'", "''");
        return "INSERT IGNORE INTO responses(transaction_id, email, session_id, response, http_status)\n" +
                "VALUES('" + client.getTransaction_id() + "', '" + client.getEmail() + "', '" + client.getSession_id() + "', '" + jsonText + "', " + http_status + ");";
    }

    private static String getResponseFromTableQuery(String transaction_id)
    {
        return "SELECT r.transaction_id, r.email, r.session_id, r.response, r.http_status\n" +
                "FROM responses AS r\n" +
                "WHERE 1=1 &&\n" +
                "      r.transaction_id = '" + transaction_id + "';";
    }

    private static String deleteResponseFromTableQuery(String transaction_id)
    {
        return "DELETE FROM responses \n" +
                "WHERE transaction_id = '" + transaction_id + "';";
    }

    public static void updateResponseTable(ClientRequest client, String jsonText,Integer http_status, Connection con) throws Exception
    {
        String query = insertQuery(client,jsonText,http_status);
        modifyTable(query, con);
    }


    public static Response.ResponseBuilder checkResponseTable(String transaction_id, Connection con) throws Exception
    {
        String query = getResponseFromTableQuery(transaction_id);
        ResultSet rs = makeResultSet(query, con);
        if(rs == null) {
             return Response.status(Status.NO_CONTENT);
        }
        else {
            return getData(rs);
        }
    }

    public static void deleteResponse(String transaction_id, Connection con) throws Exception
    {
        String query = deleteResponseFromTableQuery(transaction_id);
        modifyTable(query, con);
    }


    private static Response.ResponseBuilder getData(ResultSet rs) throws Exception
    {
        if(!rs.isBeforeFirst()){
             return null;
        }
        else {
            if (rs.next()) {
                //ServiceLogger.LOGGER.info(rs.getString("response"));
                return Response.status(rs.getInt("http_status")).entity(rs.getString("response"));
            }
        }
        return null;
    }


}
