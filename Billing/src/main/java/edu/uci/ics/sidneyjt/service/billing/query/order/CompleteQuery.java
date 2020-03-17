package edu.uci.ics.sidneyjt.service.billing.query.order;

import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompleteQuery
{
    private static String checkTokenQuery(String token)
    {
        return "SELECT t.sale_id\n" +
                "FROM transaction AS t\n" +
                "WHERE t.token = '" + token + "';";
    }

    private static String updateTransactionCaptureId(String token, String capture_id)
    {
        return "UPDATE transaction\n" +
                "SET transaction.capture_id = '" + capture_id + "'\n" +
                "WHERE transaction.token = '" + token + "';";
    }
    private static String getEmailQuery(String token)
    {
        return "SELECT s.email\n" +
                "FROM sale AS s\n" +
                "INNER JOIN transaction t\n" +
                "ON s.sale_id = t.sale_id\n" +
                "WHERE t.token = '" + token + "';";
    }

    private static String getEmailFromResultSet(ResultSet rs)
    {
        try
        {
            if(rs == null)
                return null;
            else{
                if(rs.next()){
                    return rs.getString("email");
                }
            }
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL Error:" + e.getMessage());
        } catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("System Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static boolean checkResultSetIfNotEmpty(ResultSet rs)
    {
        try
        {
            if(rs == null)
                return false;
            else{
                return rs.isBeforeFirst();
            }
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL Error:" + e.getMessage());
        } catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("System Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkToken(String token)
    {
        String query = checkTokenQuery(token);
        ResultSet rs = Query.makeResultSet(query);
        return checkResultSetIfNotEmpty(rs);
    }



    public static boolean updateTransactionTable(String token,String capture_id)
    {
        String query = updateTransactionCaptureId(token, capture_id);
        Integer result = Query.modifyTable(query);
        return result != -1 && result != -2;
    }

    public static String getEmail(String token)
    {
        String query = getEmailQuery(token);
        ResultSet rs = Query.makeResultSet(query);
        if(checkResultSetIfNotEmpty(rs))
        {
            return getEmailFromResultSet(rs);
        }
        return null;
    }




}
