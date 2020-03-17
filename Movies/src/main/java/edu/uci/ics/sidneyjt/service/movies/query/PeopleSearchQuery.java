package edu.uci.ics.sidneyjt.service.movies.query;

import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.basic.PeopleModel;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleSearchModel;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleSearchRequestQuery;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleSearchResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseRequestBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PeopleSearchQuery
{
    public static String select_where()
    {
        return "SELECT DISTINCT JSON_OBJECT(\n" +
                "    'person_id', p.person_id,\n" +
                "    'name', p.name,\n" +
                "    'birthday', p.birthday,\n" +
                "    'popularity', p.popularity,\n" +
                "    'profile_path', p.profile_path\n" +
                "    ) AS people \n" +
                "FROM person AS p\n";
    }

    public static String innerjoin(PeopleSearchRequestQuery requestModel)
    {
        String inner = "";
        if(requestModel.getName() != null)
        {
            inner += "INNER JOIN person AS p1\n" +
                    "ON p1.name LIKE '%" +  requestModel.getName() + "%' \n";
        }
        if(requestModel.getBirthday() != null)
        {
            inner += "INNER JOIN person AS p2\n" +
                    "ON p2.birthday = date '" + requestModel.getBirthday() + "' \n";
        }
        if(requestModel.getMovie_title() != null)
        {
            inner += "INNER JOIN movie AS m1\n" +
                    "ON m1.title LIKE '%" + requestModel.getMovie_title() + "%' \n" +
                    "INNER JOIN person_in_movie AS pm\n" +
                    "ON pm.movie_id = m1.movie_id \n";
        }
        return inner;
    }

    public static String where(PeopleSearchRequestQuery requestModel)
    {
        String where = "WHERE 1=1 \n";
        if(requestModel.getName() != null)
        {
            where += "&& p1.person_id = p.person_id \n";
        }
        if(requestModel.getBirthday() != null)
        {
            where += "&& p2.person_id = p.person_id  \n";
        }
        if(requestModel.getMovie_title() != null)
        {
            where += "&& pm.person_id = p.person_id \n";
        }

        return where;
    }

    public static String order_movie(PeopleSearchRequestQuery requestModel)
    {
        String order = "";
        if(requestModel.getOrderby() == null)
        {
            order = "\n" +
                    "ORDER BY p.name ";
            if (requestModel.getDirection() == null)
            {
                order += "ASC, ";
            }
            else
            {
                order += requestModel.getDirection() + ", ";
            }
            order += "p.popularity DESC ";
        }
        else
        {
            if(requestModel.getOrderby().equals("name") || requestModel.getOrderby().equals("birthday") || requestModel.getOrderby().equals("popularity")) {
                order = "\n" +
                        "ORDER BY p." + requestModel.getOrderby() + " ";
                if (requestModel.getDirection() == null)
                {
                    order += "ASC, ";
                }
                else
                {
                    order += requestModel.getDirection() + ", ";
                }
                if(requestModel.getOrderby().equals("name"))
                    order += "p.popularity DESC ";
                else if(requestModel.getOrderby().equals("birthday"))
                    order += "p.popularity DESC ";
                else
                    order += "p.name DESC ";
            }
            else
            {
                order = "\n" +
                        "ORDER BY p.name ";
                if (requestModel.getDirection() == null)
                {
                    order += "ASC, ";
                }
                else
                {
                    order += requestModel.getDirection() + ", ";
                }
                order += "p.popularity DESC ";
            }
        }
        return order;
    }


    //test later???
    public static <T> T[] getPeopleGeneric(ResultSet rs, Class<T> className) throws SQLException
    {
        ArrayList<T> peopleList = new ArrayList<>();
        while(rs.next())
        {
            if(rs.getString("people") == null)
            {
                ServiceLogger.LOGGER.warning("No people found.");
                continue;
            }
            else
            {
                T person = Util.modelMapper(rs.getString("people"), className);
                peopleList.add(person);
            }
        }
        if(peopleList.isEmpty())
        {
            ServiceLogger.LOGGER.warning("No people found.");
            throw new SQLException("No people found.");
        }
        else
        {
            T[] peopleArray = (T[])new Object[peopleList.size()];
            peopleArray = peopleList.toArray(peopleArray);
            ServiceLogger.LOGGER.info("People found, # of genres: " + peopleArray.length);
            return peopleArray;
        }
    }

    public static PeopleSearchModel[] getPeople(ResultSet rs) throws SQLException
    {
        ArrayList<PeopleSearchModel> peopleList = new ArrayList<>();
        while(rs.next())
        {
            if(rs.getString("people") == null)
            {
                ServiceLogger.LOGGER.warning("Null found in Result Set.");
                continue;
            }
            else
            {
                ServiceLogger.LOGGER.info(rs.getString("people"));
                PeopleSearchModel person = Util.modelMapper(rs.getString("people"), PeopleSearchModel.class);
                peopleList.add(person);
            }
        }
        if(peopleList.isEmpty())
        {
            ServiceLogger.LOGGER.warning("No people found.");
            throw new SQLException("No people found.");
        }
        else
        {
            PeopleSearchModel[] peopleArray = new PeopleSearchModel[peopleList.size()];
            peopleArray = peopleList.toArray(peopleArray);
            ServiceLogger.LOGGER.info("People found, # of people: " + peopleArray.length);
            return peopleArray;
        }
    }

    public static void getResponse(PeopleSearchResponseModel responseModel, ResultSet rs)
    {
        try {
            PeopleSearchModel[] people = getPeople(rs);
            responseModel.setPeople(people);
            responseModel.setResult(Result.PERSON_FOUND);
        }catch (SQLException e)
        {
            if(e.getMessage().equals("No people found."))
                responseModel.setResult(Result.NO_PERSON_FOUND);
        }
    }

    public static void updatePeopleSearchResponseModel(PeopleSearchRequestQuery requestModel, PeopleSearchResponseModel responseModel)
    {
        String query = select_where() + innerjoin(requestModel) + where(requestModel) + order_movie(requestModel) + Query.limit_offset_movie(requestModel);
        ResultSet rs = Query.makeResultSet(query);
        getResponse(responseModel, rs);
    }
}
