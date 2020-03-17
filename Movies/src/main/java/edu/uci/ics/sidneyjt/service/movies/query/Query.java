package edu.uci.ics.sidneyjt.service.movies.query;
import edu.uci.ics.sidneyjt.service.movies.MoviesService;
import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.basic.GenreModel;
import edu.uci.ics.sidneyjt.service.movies.models.basic.PeopleModel;
import edu.uci.ics.sidneyjt.service.movies.models.get.GetMovieModel;
import edu.uci.ics.sidneyjt.service.movies.models.get.GetResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.search.BrowseRequestModel;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseRequestBase;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseRequestModel;
import edu.uci.ics.sidneyjt.service.movies.models.MovieModel;
import edu.uci.ics.sidneyjt.service.movies.models.search.SearchBrowseResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Query
{
    public static String select_from_movie()
    {
        String select = "\n" +
                "SELECT JSON_OBJECT( \n"  +
                " 'movie_id', m.movie_id, \n" +
                " 'title', title, \n" +
                " 'year', year, \n" +
                " 'director', p.name, \n" +
                " 'rating', rating, \n" +
                " 'backdrop_path', backdrop_path, \n"+
                " 'poster_path', poster_path, \n" +
                " 'hidden', hidden" +
                ") AS MovieModel \n" +
                "FROM movie AS m \n";

        return select;
    }


    public static String inner_join_movie(SearchBrowseRequestModel requestModel)
    {
        String inner = "";
        if(requestModel.getDirector() != null)
        {
            inner = "INNER JOIN person AS p \n" +
                    "ON p.name LIKE '%" + requestModel.getDirector() + "%' \n";
        }
        else
        {
            inner = "INNER JOIN person AS p \n" +
                    "ON p.person_id = m.director_id \n";
        }
        if(requestModel.getGenre() != null)
        {
            inner += "INNER JOIN genre AS g \n" +
                    "ON g.name LIKE '%" +requestModel.getGenre() + "%' \n" +
                    "INNER JOIN genre_in_movie AS gm \n" +
                    "ON gm.genre_id = g.genre_id \n";
        }
        return inner;
    }

    public static String inner_join_movie(BrowseRequestModel requestModel)
    {
        String inner = "";
        for(int i = 0; i < requestModel.getPhrase().length; i ++)
        {
            String genre = "g" + (i + 1);
            String genre_in_movie = "gm" + (i + 1);
            inner += "INNER JOIN keyword AS " + genre +  "\n" +
                    "ON " + genre + ".name = '" + requestModel.getPhrase()[i] + "' \n" +
                    "INNER JOIN keyword_in_movie AS " + genre_in_movie + " \n" +
                    "ON " + genre_in_movie + ".keyword_id = " + genre + ".keyword_id \n";
        }
        inner += "INNER JOIN person AS p \n" +
                "ON p.person_id = m.director_id \n";
        return inner;
    }

    public static String where_movie(SearchBrowseRequestModel requestModel)
    {
        String where = "WHERE 1=1 ";
        if(requestModel.getTitle()!= null)
        {
            if(where.equals("WHERE 1=1 "))
            {
                where += "&& title LIKE '%" + requestModel.getTitle() + "%' ";
            }
            else {
                where += "\n" +
                        "title LIKE '%" + requestModel.getTitle() + "%' ";
            }
        }
        if(requestModel.getYear() != null)
        {
            if(where.equals("WHERE 1=1 "))
            {
                where += "\n" +
                        "&& year = " + requestModel.getYear();
            }
            else {
                where += "\n" +
                        "&& year = " + requestModel.getYear();
            }
        }
        if(requestModel.getDirector() != null)
        {
            if(where.equals("WHERE 1=1 "))
            {
                where += "\n" +
                        "&& p.person_id = m.director_id ";
            }
            where += " \n" +
                    "&& p.person_id = m.director_id ";
        }
        if(requestModel.getGenre() != null)
        {
            if(where.equals("WHERE 1=1 "))
            {
                where += "\n" +
                        "&& gm.movie_id = m.movie_id ";
            }
            where += "\n" +
                    "&& gm.movie_id = m.movie_id ";
        }
        if(!requestModel.isHidden())        //only show non-hidden movies
        {
            if(where.equals("WHERE 1=1 "))
            {
                where += "\n" +
                        "&& \n hidden = " + requestModel.isHidden();

            }
            where += "&& \n hidden = " + requestModel.isHidden();
        }

        return where;
    }

    public static String where_movie(BrowseRequestModel requestModel)
    {
        String where = "WHERE 1=1 && ";
        for(int i = 0; i < requestModel.getPhrase().length; i ++)
        {
            String genre_in_movie = "gm" + (i + 1);
            if(i == requestModel.getPhrase().length - 1)
            {
                where += "\n" +
                        genre_in_movie + ".movie_id = m.movie_id";
            }
            else {
                where += "\n" +
                        genre_in_movie + ".movie_id = m.movie_id &&";
            }
        }
        return where;
    }

    public static String order_movie(SearchBrowseRequestBase requestModel)
    {
        String order = "";
        if(requestModel.getOrderby() == null)
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
        else
        {
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



    public static String limit_offset_movie(SearchBrowseRequestBase requestModel)
    {
        try {

            String limit = "";
            int limit_int = 10;
            if (requestModel.getLimit() == null) {
                limit = "LIMIT 10 ";
                limit_int = 10;
            } else {
                if(requestModel.getLimit() != 10 && requestModel.getLimit() != 25 && requestModel.getLimit() != 50 && requestModel.getLimit() != 100) {
                    limit = "LIMIT 10 ";
                    limit_int = 10;
                }
                else {
                    limit = "LIMIT " + requestModel.getLimit() + " ";
                    limit_int = requestModel.getLimit();
                }
            }
            if (requestModel.getOffset() == null) {
                limit += "OFFSET 0;";
            }
            else {
                if(requestModel.getOffset() % limit_int != 0 || requestModel.getOffset() <= 0)
                    limit += "OFFSET 0;";
                else
                    limit += "OFFSET " + requestModel.getOffset() + ";";
            }
            return limit;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public static String getPeopleFromMovieID(String movieID)
    {
        return "SELECT JSON_OBJECT( \n" +
                "'person_id', p.person_id, \n" +
                "'name', p.name) AS people \n" +
                "FROM person AS p \n" +
                "INNER JOIN person_in_movie AS pm \n" +
                "ON pm.movie_id = '" + movieID + "' \n" +
                "WHERE p.person_id = pm.person_id;";
    }

    public static String getGenreFromMovieID(String movieID)
    {
        return "SELECT JSON_OBJECT( \n" +
                "'genre_id', g.genre_id, \n" +
                "'name', g.name) AS genres \n" +
                "FROM genre AS g \n" +
                "INNER JOIN genre_in_movie AS gm \n" +
                "ON gm.movie_id = '" + movieID + "' \n" +
                "WHERE g.genre_id = gm.genre_id;";
    }

    public static String getMovieFromMovieID(String movieID)
    {
        return "SELECT JSON_OBJECT(\n" +
                "    'movie_id', m.movie_id,\n" +
                "    'title', title,\n" +
                "    'year', year,\n" +
                "    'director', p.name,\n" +
                "    'rating', rating,\n" +
                "    'backdrop_path', backdrop_path,\n" +
                "    'poster_path', poster_path,\n" +
                "    'num_votes', num_votes,\n" +
                "    'budget', budget,\n" +
                "    'revenue', revenue,\n" +
                "    'overview', overview,\n" +
                "    'backdrop_path', backdrop_path,\n" +
                "    'poster_path', poster_path, \n" +
                "    'hidden', hidden) AS movie\n" +
                "FROM movie AS m\n" +
                "INNER JOIN person AS p\n" +
                "ON p.person_id = m.director_id\n" +
                "WHERE\n" +
                "m.movie_id = '" + movieID + "';";
    }

    public static String construct_search_query(SearchBrowseRequestModel requestModel)
    {
        String Query = "";
        Query = select_from_movie();
        Query += inner_join_movie(requestModel);
        Query += where_movie(requestModel);
        Query += order_movie(requestModel);
        Query += limit_offset_movie(requestModel);
        return Query;
    }
    public static String construct_search_query(BrowseRequestModel requestModel)
    {
        String Query = "";
        Query = select_from_movie();
        Query += inner_join_movie(requestModel);
        Query += where_movie(requestModel);
        Query += order_movie(requestModel);
        Query += limit_offset_movie(requestModel);
        return Query;
    }

    public static ResultSet makeResultSet(String Query)
    {
        try {
            ServiceLogger.LOGGER.info(Query);
            PreparedStatement ps = MoviesService.getCon().prepareStatement(Query);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeeded.");
            return rs;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: " + e.getMessage());
            return null;
        }
    }

    public static void getResponse(SearchBrowseResponseModel responseModel, Boolean isHidden, ResultSet rs)
    {
        ArrayList<MovieModel> movieList = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                ServiceLogger.LOGGER.info(rs.getString("MovieModel"));
                if(rs.getString("MovieModel") == null)
               {
                   ServiceLogger.LOGGER.warning("No movies found.");
                   responseModel.setResult(Result.NO_MOVIE_FOUND);
               }
               else {
                    MovieModel movie = Util.modelMapper(rs.getString("MovieModel"), MovieModel.class);
                    if(isHidden)
                     movie.setHidden(null);
                    movieList.add(movie);
                }
            }
            if(movieList.isEmpty())
            {
                ServiceLogger.LOGGER.warning("No movies found.");
             responseModel.setResult(Result.NO_MOVIE_FOUND);
            }
            else
            {
                MovieModel[] movies = new MovieModel[movieList.size()];
                movies = movieList.toArray(movies);
                ServiceLogger.LOGGER.info("Movie(s) found, # of movies: " + movies.length);
                responseModel.setMovies(movies);
            }
        }catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movies.");
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    public static GenreModel[] updateGenreModelList(ResultSet rs) throws Exception
    {
        ArrayList<GenreModel> genreList = new ArrayList<>();
        while(rs.next())
        {
            if(rs.getString("genres") == null)
            {
                ServiceLogger.LOGGER.warning("No genres found.");
                throw new Exception("No genres found.");
            }
            else
            {
                ServiceLogger.LOGGER.info(rs.getString("genres"));
                GenreModel genre = Util.modelMapper(rs.getString("genres"), GenreModel.class);
                genreList.add(genre);
            }
        }
        if(genreList.isEmpty())
        {
            throw new Exception("No genres found.");
        }
        else
        {
            GenreModel[] genreArray = new GenreModel[genreList.size()];
            genreArray = genreList.toArray(genreArray);
            ServiceLogger.LOGGER.info("Genres(s) found, # of genres: " + genreArray.length);
            return genreArray;
        }
    }

    public static PeopleModel[] updatePeopleModelList(ResultSet rs)throws Exception
    {
        ArrayList<PeopleModel> peopleList = new ArrayList<>();
        while(rs.next())
        {
            if(rs.getString("people") == null)
            {
                ServiceLogger.LOGGER.warning("No people found.");
                throw new Exception("No people found.");
            }
            else
            {
                PeopleModel person = Util.modelMapper(rs.getString("people"), PeopleModel.class);
                peopleList.add(person);
            }
        }
        if(peopleList.isEmpty())
        {
            throw new Exception("No people found.");
        }
        else
        {
            PeopleModel[] peopleArray = new PeopleModel[peopleList.size()];
            peopleArray = peopleList.toArray(peopleArray);
            ServiceLogger.LOGGER.info("People found, # of genres: " + peopleArray.length);
            return peopleArray;
        }
    }

    public static GetMovieModel updateGetMovieModel(ResultSet rs) throws Exception
    {
        if(rs.next())
        {
            if(rs.getString("movie") == null)
            {
                ServiceLogger.LOGGER.warning("No movies found.");
                return null;
            }
            else
            {
                GetMovieModel movieModel = Util.modelMapper(rs.getString("movie"), GetMovieModel.class);
                return movieModel;
            }
        }
        else {
            ServiceLogger.LOGGER.warning("No movies found.");
            return null;
        }
    }

    //for search page
    public static void executeSearchQuery(SearchBrowseRequestModel requestModel, SearchBrowseResponseModel responseModel)
    {
        String Query = construct_search_query(requestModel);
        ResultSet rs = makeResultSet(Query);
        getResponse(responseModel,requestModel.isHidden(),rs);
    }

    //for browse page
    public static void executeSearchQuery(BrowseRequestModel requestModel, SearchBrowseResponseModel responseModel)
    {
        String Query = construct_search_query(requestModel);
        ResultSet rs = makeResultSet(Query);
        getResponse(responseModel,requestModel.isHidden(),rs);
    }

    public static GenreModel[] getGenresFromQuery(String movieID) throws Exception
    {
        String Query = getGenreFromMovieID(movieID);
        ResultSet rs = makeResultSet(Query);
        return updateGenreModelList(rs);
    }

    public static PeopleModel[] getPeopleFromQuery(String movieID) throws Exception
    {
        String Query = getPeopleFromMovieID(movieID);
        ResultSet rs = makeResultSet(Query);
        return updatePeopleModelList(rs);
    }

    public static GetMovieModel getMovieFromQuery(String movieID)throws Exception
    {
        String Query = getMovieFromMovieID(movieID);
        ResultSet rs = makeResultSet(Query);
        return updateGetMovieModel(rs);
    }


    //for get page, called in GetPage.java
    public static void updateGetResponseModel(GetResponseModel responseModel, String movieID, Boolean isHidden)
    {
        try
        {
            GenreModel[] genreModels = getGenresFromQuery(movieID);
            PeopleModel[] peopleModels = getPeopleFromQuery(movieID);
            GetMovieModel movieModel = getMovieFromQuery(movieID);
            movieModel.setGenres(genreModels);
            movieModel.setPeople(peopleModels);
            if(isHidden)
                movieModel.setHidden(null);
            responseModel.setMovies(movieModel);
        }
        catch (Exception e)
        {
            if(e.getMessage().equals("No movies found.") || e.getMessage().equals("No people found.") || e.getMessage().equals("No genres found."))
                responseModel.setResult(Result.NO_MOVIE_FOUND);
            else {
                ServiceLogger.LOGGER.warning("Internal Error.");
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
