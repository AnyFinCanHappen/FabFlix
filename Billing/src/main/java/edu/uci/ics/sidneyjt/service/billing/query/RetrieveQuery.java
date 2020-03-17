package edu.uci.ics.sidneyjt.service.billing.query;

import edu.uci.ics.sidneyjt.service.billing.BillingService;
import edu.uci.ics.sidneyjt.service.billing.core.Util;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;
import edu.uci.ics.sidneyjt.service.billing.models.base.bill.BillingRequestModel;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RetrieveQuery
{
    private static String getMovieIdQuery(String email)
    {
        return "SELECT c.movie_id \n" +
                "FROM cart AS c \n" +
                "WHERE c.email = '" + email + "';";
    }

    private static String getMoviePricesQuantity(String email)
    {
        return "SELECT mp.unit_price, mp.discount, mp.movie_id, c.quantity \n" +
                "FROM movie_price AS mp\n" +
                "INNER JOIN cart AS c\n" +
                "ON c.email = '" + email + "' \n" +
                "WHERE mp.movie_id = c.movie_id;";
    }
    private static HashMap<String, CartDetails> getCartDetails(ResultSet rs)
    {
        try{
            HashMap<String, CartDetails> map = new HashMap<>();
            if(rs == null)
                return null;
            else
            {
                while(rs.next())
                {
                    CartDetails cartDetails = new CartDetails(rs.getFloat("discount"),
                            rs.getFloat("unit_price"),
                            rs.getInt("quantity"),
                            rs.getString("movie_id"));
                    map.put(rs.getString("movie_id"), cartDetails);
                }
                return map;
            }

        }catch (SQLException e){
            ServiceLogger.LOGGER.warning("SQL Error: " + e.getMessage());
            return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static String[] getListOfMovieId(ResultSet rs)
    {
        try{
            ArrayList<String> movieIdList = new ArrayList<>();
            if(rs == null)
                return null;
            else
            {
                while(rs.next())
                {
                    movieIdList.add(rs.getString("movie_id"));
                }
                String[] movieIdArray = new String[movieIdList.size()];
                movieIdArray = movieIdList.toArray(movieIdArray);
                return movieIdArray;

            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.warning("SQL Error: " + e.getMessage());
            return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static void updateResponse(RetrieveResponseModel responseModel, BillingRequestModel requestModel)
    {
        String movie_query = getMovieIdQuery(requestModel.getEmail());
        ResultSet rs = Query.makeResultSet(movie_query);
        String[] movieIdArray = getListOfMovieId(rs);
        if(movieIdArray == null) {
            responseModel.setResult(Result.FAILED_CART);
            return;
        }
        else if(movieIdArray.length == 0) {
            responseModel.setItems(null);
            return;
        }
        String servicePath = BillingService.getMoviesConfigs().getScheme() + BillingService.getMoviesConfigs().getHostName() + ":" +
                BillingService.getMoviesConfigs().getPort() + BillingService.getMoviesConfigs().getPath();
        String endpointPath = BillingService.getMoviesConfigs().getThumbnailPath();
        ThumbnailRequestClass requestClass = new ThumbnailRequestClass(movieIdArray);
        ThumbnailResponseClass thumbnailResponseClass = Util.checkUserPLevel(servicePath, endpointPath, ThumbnailResponseClass.class, requestClass);
        if(thumbnailResponseClass.getThumbnails().length == 0 || thumbnailResponseClass.getThumbnails() == null){
            responseModel.setItems(null);
        }
        else{
            String query = getMoviePricesQuantity(requestModel.getEmail());
            rs = Query.makeResultSet(query);
            HashMap<String, CartDetails> map = getCartDetails(rs);
            if(map == null) {
                responseModel.setItems(null);
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            }
            else {
                Thumbnail[] thumbnails = thumbnailResponseClass.getThumbnails();
                ArrayList<Item> itemList = new ArrayList<>();
                for (Thumbnail t : thumbnails)
                {
                    if (map.get(t.getMovie_id()) != null)
                    {
                        CartDetails details = map.get(t.getMovie_id());
                        Item item = new Item(requestModel.getEmail(), details.getUnit_price(), details.getDiscount(),
                                details.getQuantity(), t);
                        itemList.add(item);
                    }
                }
                Item[] items = new Item[itemList.size()];
                items = itemList.toArray(items);
                ServiceLogger.LOGGER.info("Size of items: " + items.length);
                responseModel.setItems(items);
            }
        }
    }
}
