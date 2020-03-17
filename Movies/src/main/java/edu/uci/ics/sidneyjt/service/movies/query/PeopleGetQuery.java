package edu.uci.ics.sidneyjt.service.movies.query;

import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleGetModel;
import edu.uci.ics.sidneyjt.service.movies.models.people.PeopleGetResponseModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleGetQuery
{
    public static String wholeQuery(String person_id)
    {
        return "SELECT json_object(\n" +
                "    'person_id', p.person_id,\n" +
                "    'name', p.name,\n" +
                "    'gender', g.gender_name,\n" +
                "    'birthday', p.birthday,\n" +
                "    'biography', p.biography,\n" +
                "    'birthplace', p.birthplace,\n" +
                "    'popularity', p.popularity,\n" +
                "    'profile_path', p.profile_path\n" +
                "    ) AS person\n" +
                "FROM person p\n" +
                "INNER JOIN gender g\n" +
                "ON g.gender_id = p.gender_id\n" +
                "WHERE p.person_id = '" + person_id + "' ;";
    }

    public static void getPerson(PeopleGetResponseModel responseModel, ResultSet rs)
    {
        try
        {
            if (rs.next()) {
                PeopleGetModel person  = Util.modelMapper(rs.getString("person"), PeopleGetModel.class);
                responseModel.setPerson(person);
                responseModel.setResult(Result.PERSON_FOUND);
            } else {
                responseModel.setResult(Result.NO_PERSON_FOUND);
            }
        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL Error:" + e.getMessage());
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
    }

    public static void updatePeopleGetResponseModel(PeopleGetResponseModel responseModel, String person_id)
    {
        String query = wholeQuery(person_id);
        ResultSet rs = Query.makeResultSet(query);
        getPerson(responseModel, rs);
    }

}
