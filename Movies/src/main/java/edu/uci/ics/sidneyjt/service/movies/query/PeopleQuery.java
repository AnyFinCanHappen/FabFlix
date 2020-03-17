package edu.uci.ics.sidneyjt.service.movies.query;

import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleRequestQuery;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseResponseModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleQuery
{
    public static String makeQuery(String name)
    {
        return "SELECT JSON_OBJECT(\n" +
                "    'movie_id', m.movie_id,\n" +
                "    'title', title,\n" +
                "    'year', year,\n" +
                "    'director', p1.name,\n" +
                "    'rating', rating,\n" +
                "    'backdrop_path', backdrop_path,\n" +
                "    'poster_path', poster_path,\n" +
                "    'hidden', hidden\n" +
                "           ) AS MovieModel \n" +
                "FROM movie AS m\n" +
                "INNER JOIN person AS p1\n" +
                "ON p1.person_id = m.director_id\n" +
                "INNER JOIN person AS p2\n" +
                "ON p2.name = '" + name + "' \n" +
                "INNER JOIN person_in_movie AS pm\n" +
                "ON p2.person_id = pm.person_id\n" +
                "WHERE\n" +
                "m.movie_id = pm.movie_id\n";
    }

    public static String checkPersonNameQuery(String name)
    {
        return "SELECT p.name\n" +
                "FROM person AS p\n" +
                "WHERE p.name LIKE '%" + name + "%' ;";
    }

    public static void checkResultSet(ResultSet rs) throws SQLException
    {
        if(!rs.isBeforeFirst())
        {
            ServiceLogger.LOGGER.info("WTF");
            throw new SQLException("No person found.");
        }
    }

    public static String order_movies(PeopleRequestQuery requestModel)
    {
        String order = "";
        if(requestModel.getOrderby() == null)
        {
          ServiceLogger.LOGGER.info("Null");
            order = "\n" +
                    "ORDER BY m.title ";
            if (requestModel.getDirection() == null)
            {
                order += "ASC, ";
            }
            else
            {
                order += requestModel.getDirection() + ", ";
            }
            order += "m.rating DESC ";
        }
        else
        {
          ServiceLogger.LOGGER.info("Order by: " + requestModel.getOrderby());
            if(requestModel.getOrderby().equals("title") || requestModel.getOrderby().equals("rating") || requestModel.getOrderby().equals("year")) {
                order = "\n" +
                        "ORDER BY m." + requestModel.getOrderby() + " ";
                if (requestModel.getDirection() == null)
                {
                    order += "ASC, ";
                }
                else
                {
                    order += requestModel.getDirection() + ", ";
                }
                if(requestModel.getOrderby().equals("title"))
                    order += "m.rating DESC ";
                else if(requestModel.getOrderby().equals("rating"))
                    order += "m.title DESC ";
                else
                    order += "m.rating DESC ";
            }
            else
            {
                order = "\n" +
                        "ORDER BY m.title ";
                if (requestModel.getDirection() == null)
                {
                    order += "ASC, ";
                }
                else
                {
                    order += requestModel.getDirection() + ", ";
                }
                order += "m.rating DESC ";
            }
        }
        return order;
    }

    public static void checkPeople(PeopleRequestQuery requestModel, ResponseModel responseModel)
    {
        String check = checkPersonNameQuery(requestModel.getName());
        try
        {
            ResultSet rs = Query.makeResultSet(check);
            if(!rs.isBeforeFirst())
            {
                ServiceLogger.LOGGER.warning("No Person Found.");
                responseModel.setResult(Result.NO_PERSON_FOUND);
            }
            else
            {
                ServiceLogger.LOGGER.info("People Found.");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Error: " + e.getMessage());
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
    }

    public static void updatePeopleResponseModel(PeopleRequestQuery requestModel, SearchBrowseResponseModel responseModel)
    {
        checkPeople(requestModel, responseModel);
        if(responseModel.getResult() != null) {
            if(responseModel.getResult().getResultCode() == Result.NO_PERSON_FOUND.getResultCode() || responseModel.getResult().getResultCode() == Result.INTERNAL_SERVER_ERROR.getResultCode())
                return;
        }
        String query = makeQuery(requestModel.getName()) + order_movies(requestModel) + Query.limit_offset_movie(requestModel);
        ResultSet rs = Query.makeResultSet(query);
        Query.getResponse(responseModel, requestModel.isHidden(), rs);
    }
}
