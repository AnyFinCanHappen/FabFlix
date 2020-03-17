package edu.uci.ics.sidneyjt.service.movies.query;

import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.thumbnail.Thumbnail;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThumbnailQuery
{
    public static String constructBasicMovieQuery(String movieID)
    {
        return "SELECT JSON_OBJECT(\n" +
                "    'movie_id', m.movie_id,\n" +
                "    'title', m.title,\n" +
                "    'backdrop_path', m.backdrop_path,\n" +
                "    'poster_path', m.poster_path\n" +
                "    )   AS thumbnail\n" +
                "FROM movie AS m\n" +
                "WHERE m.movie_id = '" + movieID + "' ;";
    }

    public static Thumbnail updateThumbnailJson(String movieID)
    {
        String query = constructBasicMovieQuery(movieID);
        ResultSet rs = Query.makeResultSet(query);
        try
        {
            if(rs == null)
                return null;
            if(rs.next())
            {
                return Util.modelMapper(rs.getString("thumbnail"), Thumbnail.class);
            }
            else
            {
                ServiceLogger.LOGGER.warning("No movie found, could not retrieve thumbnail.");
                return null;
            }

        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL error. Could not retrieve thumbnail.");
        }
        return null;
    }
}
