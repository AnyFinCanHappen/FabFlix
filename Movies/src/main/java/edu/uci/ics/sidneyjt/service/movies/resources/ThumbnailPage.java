package edu.uci.ics.sidneyjt.service.movies.resources;


import edu.uci.ics.sidneyjt.service.movies.core.Util;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import edu.uci.ics.sidneyjt.service.movies.models.thumbnail.Thumbnail;
import edu.uci.ics.sidneyjt.service.movies.models.thumbnail.ThumbnailRequestClass;
import edu.uci.ics.sidneyjt.service.movies.models.thumbnail.ThumbnailResponseClass;
import edu.uci.ics.sidneyjt.service.movies.query.ThumbnailQuery;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("thumbnail")
public class ThumbnailPage
{
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response Post(@Context HttpHeaders headers, String jsonString)
    {
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");
        ServiceLogger.LOGGER.info("email:" + email);
        ThumbnailResponseClass responseModel = new ThumbnailResponseClass();
        ThumbnailRequestClass requestModel = Util.modelMapper(jsonString, ThumbnailRequestClass.class, responseModel);
        //ThumbnailRequestClass requestModel = Util.modelMapper(jsonString, ThumbnailRequestClass.class);
        if(requestModel == null)
            return responseModel.buildResponse();

        ArrayList<Thumbnail> thumbnailList = new ArrayList<>();
        for(int i = 0; i < requestModel.getMovie_ids().length; i ++)
        {
            Thumbnail thumbnail = ThumbnailQuery.updateThumbnailJson(requestModel.getMovie_ids()[i]);
            if(thumbnail != null)
                thumbnailList.add(thumbnail);
        }
        if(thumbnailList.isEmpty()) {
            responseModel.setResult(Result.NO_MOVIE_FOUND);
            return responseModel.buildResponse(email, session_id, transaction_id);
        }
        Thumbnail[] thumbnails = new Thumbnail[thumbnailList.size()];
        thumbnails = thumbnailList.toArray(thumbnails);
        responseModel.setThumbnails(thumbnails);
        return responseModel.buildResponse(email, session_id, transaction_id);
    }
}
