package edu.uci.ics.sidneyjt.service.billing.query.order;

import edu.uci.ics.sidneyjt.service.billing.core.Util;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.order.retrieve.OrderItemModel;
import edu.uci.ics.sidneyjt.service.billing.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderRetrieveQuery
{
    private static String retrieveQuery(String email)
    {
        return "SELECT t.token\n" +
                "FROM transaction AS t\n" +
                "INNER JOIN sale AS s\n" +
                "ON s.email = '" + email + "'\n" +
                "WHERE 1=1 && s.sale_id = t.sale_id;";
    }
    private static ArrayList<String> getTokensFromResultSet(ResultSet rs)
    {
        try{
            ArrayList<String> tokenList = new ArrayList<>();
            while(rs.next())
            {
                tokenList.add(rs.getString("token"));
            }
            return tokenList;
        }catch (SQLException e){
            ServiceLogger.LOGGER.warning("SQL Error: " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<String> getTokens(String email)
    {
        String query = retrieveQuery(email);
        ResultSet rs = Query.makeResultSet(query);
        if(rs == null)
            return null;
        return getTokensFromResultSet(rs);
    }


    private static String itemsQuery(String token)
    {
        return "SELECT JSON_OBJECT(\n" +
                "    'email', s.email,\n" +
                "    'movie_id', s.movie_id,\n" +
                "    'quantity', s.quantity,\n" +
                "    'sale_date', s.sale_date,\n" +
                "    'unit_price', m.unit_price,\n" +
                "    'discount', m.discount\n" +
                "           ) AS Item\n" +
                "FROM sale AS s\n" +
                "INNER JOIN movie_price AS m\n" +
                "ON m.movie_id = s.movie_id\n" +
                "INNER JOIN transaction t\n" +
                "ON s.sale_id = t.sale_id\n" +
                "WHERE t.token = '" + token + "';";
    }
    private static OrderItemModel[] getItemsFromResultSet(ResultSet rs)
    {
        try{
            if(rs != null)
            {
                ArrayList<OrderItemModel> itemList = new ArrayList<>();
                while(rs.next())
                {
                    String jsonItem = rs.getString("Item");
                    OrderItemModel item = Util.modelMapper(jsonItem, OrderItemModel.class);
                    itemList.add(item);
                }
                OrderItemModel[] itemArray = new OrderItemModel[itemList.size()];
                itemArray = itemList.toArray(itemArray);
                return itemArray;
            }
            else{
                return null;
            }
        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL Error: " + e.getMessage());
        }
        return null;
    }

    public static OrderItemModel[] getItems(String token)
    {
        String query = itemsQuery(token);
        ResultSet rs = Query.makeResultSet(query);
        return getItemsFromResultSet(rs);
    }

}
