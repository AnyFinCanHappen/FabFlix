package edu.uci.ics.sidneyjt.service.idm.helper;

import edu.uci.ics.sidneyjt.service.idm.IDMService;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.security.Crypto;
import edu.uci.ics.sidneyjt.service.idm.security.Session;
import edu.uci.ics.sidneyjt.service.idm.security.Token;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBTool
{
    public static String buildLoginQuery(ArrayList<String> cols, String email)
    {

        String SELECT = "SELECT user_id";
        String FROM = " FROM user";
        String WHERE = " WHERE 1=1 && email LIKE '" + email + "';";

        for(String c:cols){
            SELECT += (", " + c);
        }

        return SELECT + FROM + WHERE;
    }
    public static boolean checkUniqueEmail(String email)
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
                ServiceLogger.LOGGER.info("Email is unique");
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
    public static boolean retrieveUserDB(String email, ArrayList<Integer> list)
    {
        try {
            String Query = "SELECT user_id, status " +
                    "FROM user " +
                    "WHERE 1=1 && email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();       //iterator
            ServiceLogger.LOGGER.info("Query succeeded.");

            if(!rs.isBeforeFirst())
            {
                ServiceLogger.LOGGER.info("Email not found.");
                return false;
            }
            else {
                ServiceLogger.LOGGER.info("Email found.");
                while(rs.next())
                {
                    Integer user_id = rs.getInt("user_id");
                    Integer status = rs.getInt("status");
                    list.add(status);
                }
                return true;
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve user records.");
            e.printStackTrace();
        }
        return false;
    }
    public static boolean getUserData(String email, UserStorage bin)
    {
        try {
            String Query = "SELECT * " +
                    "FROM user " +
                    "WHERE 1=1 && email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();       //iterator
            ServiceLogger.LOGGER.info("Query succeeded.");

            if(!rs.isBeforeFirst())
            {
                ServiceLogger.LOGGER.info("Email not found.");
                return false;
            }
            else {
                ServiceLogger.LOGGER.info("Email found.");
                while(rs.next())
                {
                    bin.setUser_id(rs.getInt("user_id"));
                    bin.setStatus(rs.getInt("status"));
                    bin.setPlevel(rs.getInt("plevel"));
                    bin.setSalt(rs.getString("salt"));
                    bin.setPword(rs.getString("pword"));
                }
                bin.setEmail(email);
                return true;
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve user records.");
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkPassword(String email, char[] pass)
    {
        try
        {
            ArrayList<String> col = new ArrayList<>();
            col.add("salt");
            col.add("pword");
            String Query = buildLoginQuery(col, email);
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();       //iterator
            ServiceLogger.LOGGER.info("Query succeeded.");
            if(!rs.isBeforeFirst())
            {
                ServiceLogger.LOGGER.info("Query empty.");
                return false;
            }
            else {
                if(rs.next())
                {
                    String saltString = rs.getString("salt");
                    String DBpassword = rs.getString("pword");
                    byte[] salt = Hex.decodeHex(saltString);
                    //salt requestModel password

                    String SaltedPass = Hex.encodeHexString(Crypto.SaltAndHash(pass, salt));
                    //compare if they are the same from database
                    //ServiceLogger.LOGGER.info(Hex.encodeHexString(salt));
                    //ServiceLogger.LOGGER.info(SaltedPass + "  DBP = " + DBpassword);
                    return DBpassword.equals(SaltedPass);
                }
                return false;
            }

        } catch (SQLException | DecoderException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static void updateUserStatus(String email, int status)
    {
        try
        {
            String Query = " UPDATE user " +
                            "SET status = " + status +
                            " WHERE email LIKE '" + email + "';";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying to update into DB: " + ps.toString());
            ps.executeUpdate();       //iterator
            ServiceLogger.LOGGER.info("Update succeeded.");
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Update failed.");
            e.printStackTrace();
        }
    }

    public static boolean getSessionData(String email, String session_id, SessionStorage store)
    {
        try
        {
            String Query = "SELECT status, time_created, last_used, expr_time "
                            +"FROM session "
                            +"WHERE 1=1 && email LIKE '" + email + "' && session_id Like '" + session_id + "';";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if(!rs.isBeforeFirst()) {
                ServiceLogger.LOGGER.info("Query is empty.");
                return false;
            }
            while(rs.next())
            {
                store.setStatus(rs.getInt("status"));
                store.setTimeCreated(rs.getTimestamp("time_created"));
                store.setLastUsed(rs.getTimestamp("last_used"));
                store.setExprTime(rs.getTimestamp("expr_time"));
            }
            store.setEmail(email);
            store.setSessionID(Token.rebuildToken(session_id));
            return true;
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getSessionData(String email, int status, SessionStorage store)
    {
        try
        {
            String Query = "SELECT status, time_created, last_used, expr_time, session_id "
                    +"FROM session "
                    +"WHERE 1=1 && email LIKE '" + email + "' && status = " + status + ";";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if(!rs.isBeforeFirst()) {
                ServiceLogger.LOGGER.info("Query is empty.");
                return false;
            }
            while(rs.next())
            {
                store.setStatus(rs.getInt("status"));
                store.setTimeCreated(rs.getTimestamp("time_created"));
                store.setLastUsed(rs.getTimestamp("last_used"));
                store.setExprTime(rs.getTimestamp("expr_time"));
                store.setSessionID(Token.rebuildToken(rs.getString("session_id")));
            }
            store.setEmail(email);

            return true;
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return false;
    }

    //update later???
    public static void updateSession(Session session, int status)
    {
        try
        {
            if(checkIfSessionExist(session))
            {
                String Query = "UPDATE session " +
                        "SET session_id = '" + session.getSessionID() + "', status = " + status + ", time_created = '" + session.getTimeCreated() +
                        "', last_used = '" + session.getLastUsed() + "', expr_time =  '" + session.getExprTime() + "'" +
                        "WHERE 1=1 && email LIKE '" + session.getEmail() + "';";
                PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
                ServiceLogger.LOGGER.info("Trying Update: " + ps.toString());
                ps.executeUpdate();       //iterator
                ServiceLogger.LOGGER.info("Update succeeded.");
                DBTool.updateUserStatus(session.getEmail(),status);
            }
            else {
                String Query = "INSERT INTO session (session_id, email, status, time_created, last_used, expr_time)"
                            + "VALUES ('" + session.getSessionID() + "', '" + session.getEmail() + "', " + status + ", '"
                            + session.getTimeCreated() + "', '" + session.getLastUsed() + "', '" + session.getExprTime() + "');";
                PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
                ServiceLogger.LOGGER.info("Trying Insert: " + ps.toString());
                ps.executeUpdate();       //iterator
                ServiceLogger.LOGGER.info("Insert succeeded.");
                DBTool.updateUserStatus(session.getEmail(),status);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static boolean checkIfSessionExist(Session session)
    {
        try
        {
            String Query = "SELECT * " +
                    "FROM session "+
                    "WHERE 1=1 && email LIKE ? && session_id LIKE ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ps.setString(1, session.getEmail());
            ps.setString(2, String.valueOf(session.getSessionID()));
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
    public static void insertSession(Session session, int status)
    {
        try {
            String Query = "INSERT INTO session (session_id, email, status, time_created, last_used, expr_time)"
                    + "VALUES ('" + session.getSessionID() + "', '" + session.getEmail() + "', " + status + ", '"
                    + session.getTimeCreated() + "', '" + session.getLastUsed() + "', '" + session.getExprTime() + "');";
            PreparedStatement ps = IDMService.getCon().prepareStatement(Query);
            ServiceLogger.LOGGER.info("Trying Insert: " + ps.toString());
            ps.executeUpdate();       //iterator
            ServiceLogger.LOGGER.info("Insert succeeded.");
            DBTool.updateUserStatus(session.getEmail(), status);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
