import Socket from "../util/Socket";
import { billingEPs } from "../Config.json";

const {insertEP, retrieveEP, updateEP, deleteEP, placeEP, completeEP, orderRetrieveEP} = billingEPs;

async function insert(email, session_id, payload)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    return await Socket.billingPOST(insertEP,payload);
}

async function retrieve(email, session_id, payload)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    return await Socket.billingPOST(retrieveEP,payload);
}

async function update(email, session_id, payload)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    console.log(payload);
    return await Socket.billingPOST(updateEP,payload);
}

async function deleteCart(email, session_id, payload)
{
    const defaultOptions = {
        email: email,
        session_id: session_id,
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    console.log(payload);
    return await Socket.billingPOST(deleteEP,payload);
}

async function placeOrder(email, session_id, payload)
{
        const defaultOptions = {
            email: email,
            session_id: session_id
        };
        Socket.updateHeaders(JSON.stringify(defaultOptions));
        console.log(payload);
        return await Socket.billingPOST(placeEP,payload);
}

async function completeOrder(payload)
{
    const querystring = require('querystring');
    let queryString = querystring.stringify(payload);
    console.log(queryString);
    return await Socket.billingGet(completeEP.concat("?").concat(queryString));
}

async function getOrder(email, session_id, payload)
{
    const defaultOptions = {
        email: email,
        session_id: session_id
    };
    Socket.updateHeaders(JSON.stringify(defaultOptions));
    console.log(payload);
    return await Socket.billingPOST(orderRetrieveEP,payload);
}

export default{
    insert,
    retrieve,
    update,
    deleteCart,
    placeOrder,
    completeOrder,
    getOrder
}