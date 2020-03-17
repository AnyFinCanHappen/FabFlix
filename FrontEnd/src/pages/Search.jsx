import React, { Component } from "react";
import Movies from "../services/Movies.js";
import "../css/common.css";
import "../css/image.css";
import { Redirect } from "react-router-dom";

/*
  TO DO
  1) Finish Search feature
      a) add genre as search option
      b) add page implentation
  2) See movie details
  3) Add movie to cart
  4) Buy movie
  5) See order history
*/

const localStorage = require("local-storage");

class Search extends Component {
  state = {
    title_request:"",
    year_request:"",
    director_request:"",
    keyword_request:"",
    keyword: false,
    orderYear: false,
    orderTitle: false,
    orderRating: false,
    desc: false,
    asc: false,
    pageNumber: 0,
    limit: 10,
    genre: "None",
    movieList :[],
    movieFound:null,
    movieDetail:null,
    movieDetailFound: false
  };

  handleGenreChange = e => {
    const {value} = e.target;
    this.setState({genre:value});
  }

  handleLimitChange = e => {
    const {value} = e.target;
    this.setState({limit:value});
  }


  handleSubmit = e => {
    e.preventDefault();
    this.setState({pageNumber:0});
    this.executeSearch(0);
  };

  executeSearch(offset)
  {
    const noMoviesFound = 211;
    const {keyword_request,title_request, year_request, director_request, 
      orderRating, orderTitle, orderYear, desc, asc, genre, keyword,
      limit} = this.state;
    const queryParam = {limit: limit};
    if(title_request !== "" || year_request !== "" || director_request !== ""|| keyword_request !== "")
    {
      if(keyword)
      {
        console.log(keyword);
        if(offset > 0)
        {
          queryParam.offset = (offset)*limit;
        }
        console.log("here");
        Movies.browsePhrase(localStorage.get("email"), localStorage.get("session_id"),keyword_request)
        .then(response => {
          if(response.data.resultCode === noMoviesFound)
          {
            this.setState({movieFound:false});
          }
          else{
            this.setState({movieFound:true})
            this.setState({movieList: response.data.movies});
            console.log(JSON.stringify(this.state.movieList, null, 4));
          }
        })
        .catch(error => console.log(error));
      }
      else
      {
        //Search by
        if(title_request !== ""){
          queryParam.title = title_request;
        }
        else if(year_request){
          queryParam.year = year_request;
        }
        else if(director_request !== ""){
          queryParam.director = director_request;
        }
        //Order by
        if(orderYear){
          queryParam.orderby = "year";
        }
        else if(orderTitle){
          queryParam.orderby = "title";
        }
        else if(orderRating){
          queryParam.orderby = "rating";
        }
        //direction by
        if(desc){
          queryParam.direction = "DESC";
        }
        else if(asc){
          queryParam.direction = "ASC";
        }
        //genre
        if(genre !== "None"){
          queryParam.genre = genre;
        }
        if(offset > 0)
        {
          queryParam.offset = (offset)*limit;
        }
        console.log(queryParam);
        Movies.searchTitle(localStorage.get("email"), localStorage.get("session_id"),queryParam)
        .then(response => {
          if(response.data.resultCode === noMoviesFound)
          {
            this.setState({movieFound:false});
          }
          else{
            this.setState({movieFound:true})
            this.setState({movieList: response.data.movies});
            console.log(JSON.stringify(this.state.movieList, null, 4));
          }
        })
        .catch(error => console.log(error));
      }
    }
  }

  getMovieDetail = e =>
  {
    const noMoviesFound = 211;
    const{id} = e.target;
    Movies.getMovieDetail(localStorage.get("email"), localStorage.get("session_id"),id)
    .then(response => {
      if(response.data.resultCode === noMoviesFound)
      {
        this.setState({movieDetailFound:false});
      }
      else{
        this.setState({movieDetailFound:true});
        this.setState({movieDetail: response.data.movie});
        localStorage.set("movieDetail",response.data.movie);
        this.props.updateMovieDetail(response.data.movie);
        console.log(JSON.stringify(this.state.movieDetail, null, 4));
      }
    })
    .catch(error => console.log(error));
  }

  changePage = e =>{
    const {pageNumber} = this.state;
    const {id} = e.target;
    if(id === "nextButton")
    {
      this.setState({pageNumber:pageNumber + 1});
      this.executeSearch(this.state.pageNumber + 1);
    }
    else if(id === "prevButton")
    {
      this.setState({pageNumber:pageNumber - 1});
      this.executeSearch(this.state.pageNumber - 1);
    }
  };

  updateField = e => {
    const { id, value, name } = e.target;
    if(name === "orderRadio")
    {
      if(id === "orderYear")
      {
        this.setState({[id]: value});
        this.setState({orderTitle:false});
        this.setState({orderRating:false});
      }
      else if(id === "orderTitle")
      {
        this.setState({[id]: value});
        this.setState({orderYear:false});
        this.setState({orderRating:false});
      }
      else if(id === "orderRating")
      {
        this.setState({[id]: value});
        this.setState({orderYear:false});
        this.setState({orderTitle:false});
      }
    }
    else if(name === "directionRadio")
    {
        if(id === "desc")
        {
          this.setState({[id]: value});
          this.setState({asc:false});
        }
        else if(id === "asc")
        {
          this.setState({[id]: value});
          this.setState({desc: false});
        }
    }
    else{
      this.setState({ [id]: value });
    }
  };

  /*
          <Route 
          path = "/MovieDetail" 
          component={props => <MovieDetail movieDetail = {movieDetail} {...props} />}/>

  */

  render() {
    const {title_request, director_request, year_request,keyword_request} = this.state;
    if(this.state.movieDetailFound)
    {
      return <Redirect to = "/MovieDetail"/>
    }
    return (
      <div>
      
      <div className="form-box-search">
        <form onSubmit = {this.handleSubmit}>
        <label className="label">Title</label>
          <input
            className="input"
            type="title_request"
            id ="title_request"
            value={title_request}
            onChange={this.updateField}
          />
        <br></br>
        <label className="label">Director</label>
          <input
            className="input"
            type="director_request"
            id ="director_request"
            value={director_request}
            onChange={this.updateField}
          />
        <br></br>
        <label className="label">Year</label>
          <input
            className="input"
            type="year_request"
            id ="year_request"
            value={year_request}
            onChange={this.updateField}
          />
        
        <label className="label">Keyword</label>
          <input
            className="input"
            type="keyword_request"
            id ="keyword_request"
            value={keyword_request}
            onChange={this.updateField}
          />
        <br></br>
        
        <br></br>
        <div className = "right">
          Number of Results <br></br>
          <select value = {this.state.limit} onChange = {this.handleLimitChange} >
            <option value = "10">10</option>
            <option value = "25">25</option>
            <option value = "50">50</option>
            <option value = "100">100</option>
          </select>
        </div>
        <label htmlFor = "keyword"> No Keyword </label>
        <input type ="radio"
          id = "keyword"
          name = "radio"
          value = {false}
          onChange = {this.updateField}
        />
        <label htmlFor = "keyword"> Keyword </label>
          <input type ="radio"
            id = "keyword"
            name = "radio"
            value = {true}
            onChange = {this.updateField}
          />
        <div id = "ifKeyword">
        <br></br> 
          <p>Order By</p>
          <label htmlFor = "orderYear"> Year </label>
          <input type ="radio"
            id = "orderTitle"
            name = "orderRadio"
            value = {true}
            onChange = {this.updateField}
          />
          <label htmlFor = "orderTitle"> Title  </label>

          <input type ="radio"
            id = "orderRating"
            name = "orderRadio"
            value = {true}
            onChange = {this.updateField}
          />
          <label htmlFor = "orderRating"> Rating</label>

          <br></br> <br></br>
          <p>Direction</p>
          <input type ="radio"
            id = "desc"
            name = "directionRadio"
            value = {true}
            onChange = {this.updateField}
          />
          <label htmlFor = "desc"> Descending </label>

          <input type ="radio"
            id = "asc"
            name = "directionRadio"
            value = {true}
            onChange = {this.updateField}
          />
          <label htmlFor = "asc"> Ascending  </label>
          <br></br><br></br>
          Genre <br></br>
          <select value = {this.state.genre} onChange = {this.handleGenreChange}>
            <option value = "None">None</option>
            <option value = "Adventure">Adventure</option>
            <option value = "Fantasy">Fantasy</option>
            <option value = "Animation">Animation</option>
            <option value = "Drama">Drama</option>
            <option value = "Horror">Horror</option>
            <option value = "Action">Action</option>
            <option value = "Comedy">Comedy</option>
            <option value = "History">History</option>
            <option value = "Western">Western</option>
            <option value = "Thriller">Thriller</option>
            <option value = "Crime">Crime</option>
            <option value = "Documentary">Documentary</option>
            <option value = "Science Fiction">Science Fiction</option>
            <option value = "Mystery">Mystery</option>
            <option value = "Music">Music</option>
            <option value = "Romance">Romance</option>
            <option value = "Family">Family</option>
            <option value = "War">War</option>
            <option value = "TV Movie">TV Movie</option>
          </select>
        </div> 
        
        <button className="button"> Search</button> 
        <br></br>
        </form>
        </div>
        {this.state.movieFound && this.state.pageNumber !== 0 &&
          <button className = "buttonBottonLeft" 
            id = "prevButton" 
            onClick = {this.changePage}>
            Previous
          </button>
        }
        {this.state.movieFound &&
          <button className = "buttonBottonRight" 
            id = "nextButton"
            onClick = {this.changePage}>
            Next
          </button>
        } 
        <br></br><br></br><br></br><br></br>
        {this.state.movieFound &&
          <div id = "moviesLeft" className = "columnLeft">
            {
              this.state.movieList.map((movies, i) =>{
                if(i%2 === 0){
                  const posterURL = "http://image.tmdb.org/t/p/w185" + movies.poster_path;
                  return(
                    <div key = {i}>
                      <img src = {posterURL} alt = "new" id = {movies.movie_id} onClick = {this.getMovieDetail}></img> 
                      <p> {movies.title} </p>
                      
                      <br></br>
                    </div>
                  );
                }
                else{
                  return(<div key = {i}></div>);
                }
              })
            }
          </div> 
        }
        {this.state.movieFound &&
          <div id = "moviesRight" className = "columnRight">
            {
              this.state.movieList.map((movies, i) =>{
                if(i%2 !== 0){
                  const posterURL = "http://image.tmdb.org/t/p/w185" + movies.poster_path;
                  return(
                    <div key = {i}>
                      <img src = {posterURL} alt = "new" id = {movies.movie_id} onClick = {this.getMovieDetail}></img> 
                      <p> {movies.title} </p>
                      <br></br>
                    </div>
                  );
                }
                else{
                  return(<div key = {i}></div>);
                }
              })
            }
          </div>
        }
        {!this.state.movieFound && this.state.movieFound !== null &&
          <div>
            <button className = "buttonBottonLeft" 
            id = "prevButton" 
            onClick = {this.changePage}>
            Previous
            </button>
            <div className = 'no_movie_found_text'>
              No Movies Found
            </div>
          </div>
        }
      </div>
    );
  }
}

export default Search;


