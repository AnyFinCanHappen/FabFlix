import Axios from "axios";

import Config from "../Config.json";

const localStorage = require("local-storage");

const { baseUrl, pollLimit, gatewayEPs, idmURL,billingURL } = Config;

const HTTPMethod = Object.freeze({
  GET: "GET",
  POST: "POST",
  DELETE: "DELETE"
});

function initSocket() {
  const { common } = Axios.defaults.headers;
  common["session_id"] = localStorage.get("session_id");
  Axios.defaults.baseURL = baseUrl;
}

function initBaseURL(){
  Axios.defaults.baseURL = baseUrl;
}

async function GET(path) {
  console.log(path);
  return await sendHTTP(HTTPMethod.GET, path);
}

async function POST(path, data) {
  return await sendHTTP(HTTPMethod.POST, path, data);
}

async function DELETE(path) {
  return await sendHTTP(HTTPMethod.DELETE, path);
}

async function sendHTTP(method, path, data) {
  let response;

  switch (method) {
    case HTTPMethod.GET:
      response = await Axios.get(path);
      break;
    case HTTPMethod.POST:
      response = await Axios.post(path, data);
      break;
    case HTTPMethod.DELETE:
      response = await Axios.delete(path);
      break;
    default:
    // Should never reach here
  }
  /************************************************
        TODO Do error checking on response 
  ************************************************/
 if(response.status !== 204){
  console.log(response);
   return response;
 }
 console.log(response);

  return await getReport(response);
}


async function idmGET(path) {
  return await sendIdmHTTP(HTTPMethod.GET, path);
}

async function idmPOST(path, data) {
  return await sendIdmHTTP(HTTPMethod.POST, path, data);
}

async function idmDELETE(path) {
  return await sendIdmHTTP(HTTPMethod.DELETE, path);
}

async function sendIdmHTTP(method, path, data) {
  let response;
  Axios.defaults.baseURL = idmURL;
  switch (method) 
  {
    case HTTPMethod.GET:
      response = await Axios.get(path);
      break;
    case HTTPMethod.POST:
      response = await Axios.post(path, data);
      break;
    case HTTPMethod.DELETE:
      response = await Axios.delete(path);
      break;
    default:
  }
  Axios.defaults.baseURL = baseUrl;
  return response;
}



async function getReport(response) {
  const axiosConfig = {
    headers: { transaction_id: response.headers["transaction_id"] }
  };
  console.log(axiosConfig);
  return await pollForReport(axiosConfig);
}

async function pollForReport(axiosConfig) {
  let noContent = 204;

  for (let i = 0; i < pollLimit; i++) {
    const response = await Axios.get(gatewayEPs.reportEP, axiosConfig);
    console.log(response);
    if (response.status !== noContent) {
      console.log(response);
      /************************************************
            TODO More Robust checking for response  
      ************************************************/
      return response;
    } else await timeOut();
  }

  return undefined;
}

async function timeOut() {
  return new Promise(resolve => {
    let pollingLimit = 100;
    setTimeout(() => resolve(), pollingLimit);
  });
}

async function timeOut2() {
  return new Promise(resolve => {
    let pollingLimit = 1000;
    setTimeout(() => resolve(), pollingLimit);
  });
}

function updateHeaders(defaultOptions){
  const {common}= Axios.defaults.headers;
  const headers = JSON.parse(defaultOptions);
  common["email"] = headers["email"];
  common["session_id"] = headers["session_id"];
}



async function billingGet(path) {
  return await sendBillingHTTP(HTTPMethod.GET, path);
}

async function billingPOST(path, data) {
  return await sendBillingHTTP(HTTPMethod.POST, path, data);
}

async function billingDELETE(path) {
  return await sendBillingHTTP(HTTPMethod.DELETE, path);
}

async function sendBillingHTTP(method, path, data) {
  let response;
  Axios.defaults.baseURL = billingURL;
  switch (method) 
  {
    case HTTPMethod.GET:
      response = await Axios.get(path);
      console.log(response);      
      if(response.status !== 3420)
      {
        for (let i = 0; i < pollLimit; i++) 
        {
          response = await Axios.get(path);
          console.log(response);
          if (response.status === 3420) {
            return response;
          } else await timeOut2();
        }
      }
      break;
    case HTTPMethod.POST:
      response = await Axios.post(path, data);
      break;
    case HTTPMethod.DELETE:
      response = await Axios.delete(path);
      break;
    default:
  }
  Axios.defaults.baseURL = baseUrl;
  return response;
}

export default {
  initSocket,
  initBaseURL,
  GET,
  POST,
  DELETE,
  updateHeaders,
  idmGET,
  idmPOST,
  idmDELETE,
  billingPOST,
  billingGet,
  billingDELETE
};
