package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.BillingService;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query
{

    public static String checkMovieQuery(String movie_id)
    {
        return "SELECT m.movie_id \n" +
                "FROM movie_price AS m \n" +
                "WHERE 1=1 && m.movie_id = '" + movie_id + "';";
    }

    public static boolean checkMovieId(String movie_id){
        String query = Query.checkMovieQuery(movie_id);
        ResultSet rs = Query.makeResultSet(query);
        if(rs == null)
            return false;
        try {
            return rs.isBeforeFirst();
        }catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.warning("SQL Error: " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer modifyTable(String Query)
    {
        try {
            ServiceLogger.LOGGER.info(Query);
            PreparedStatement ps = BillingService.getCon().prepareStatement(Query);
            Integer row = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query Succeeded.");
            return row;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: " + e.getMessage());
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    public static ResultSet makeResultSet(String Query)
    {
        try {
            ServiceLogger.LOGGER.info(Query);
            PreparedStatement ps = BillingService.getCon().prepareStatement(Query);
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

}
