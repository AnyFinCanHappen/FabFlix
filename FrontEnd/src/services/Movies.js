import Socket from "../util/Socket";
import { movieEPs } from "../Config.json";

const {searchEP, browseEP, movieDetailEP} = movieEPs;


async function searchTitle(email, session_id, param)
{
    const params = param;
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    console.log(JSON.stringify(params));
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    Socket.initBaseURL();
    const querystring = require('querystring');
    let queryString = querystring.stringify(params);
    console.log(queryString);
    return await Socket.GET(searchEP.concat("?").concat(queryString));
}

async function browsePhrase(email, session_id, keywords)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    Socket.initBaseURL();
    return await Socket.GET(browseEP.concat(keywords));
}

async function getMovieDetail(email, session_id, movie_id)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    Socket.initBaseURL();
    return await Socket.GET(movieDetailEP.concat(movie_id));
}


export default{
    searchTitle,
    browsePhrase,
    getMovieDetail
};
