package edu.uci.ics.sidneyjt.service.billing.query.order;

import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.Item;
import edu.uci.ics.sidneyjt.service.billing.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceQuery
{
    private static String saleQuery(Item item)
    {
        return "INSERT INTO sale(email,movie_id, quantity, sale_date)\n" +
                "VALUES('" + item.getEmail() + "', '" + item.getMovie_id() + "', " + item.getQuantity() + ", CURRENT_DATE);";
    }

    private static String getSaleIdQuery(Item item)
    {
        return "SELECT s.sale_id\n" +
                "FROM sale AS s\n" +
                "WHERE 1=1 && s.email = '"+ item.getEmail() + "'&&\n" +
                "      s.movie_id = '" + item.getMovie_id() + "'&&\n" +
                "      s.quantity = " + item.getQuantity() + ";";
    }

    private static String transactionQuery(String token, Integer sale_id)
    {
        return "INSERT INTO transaction(sale_id, token)\n" +
                "VALUES(" + sale_id + ",'" + token + "');";
    }

    private static Integer getSaleId(ResultSet rs)
    {
        try
        {
            ArrayList<Integer> sale_idList = new ArrayList<>();
            while(rs.next())
            {
                sale_idList.add(rs.getInt("sale_id"));
            }
            Integer max = Integer.MIN_VALUE;
            for(Integer i : sale_idList)
            {
                if(i >= max)
                    max = i;
            }
            return max;
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL ERROR:" + e.getMessage());
        }
        return -1;
    }


    private static boolean insertSaleTransaction(Item[] items, String token)
    {
        String query;
        for(Item i : items)
        {
            query = saleQuery(i);
            Query.modifyTable(query);
            query = getSaleIdQuery(i);
            ResultSet rs = Query.makeResultSet(query);
            if(rs == null)
                return false;
            Integer ID = getSaleId(rs);
            if(ID == -1)
                return false;
            else{
                query = transactionQuery(token, ID);
                Query.modifyTable(query);
            }
        }
        return true;
    }
    public static boolean updateSaleTransactions(Item[] items, String token)
    {
        return insertSaleTransaction(items, token);
    }
}
