import Socket from "../util/Socket";
import { idmEPs } from "../Config.json";

const { loginEP, registerEP } = idmEPs;

async function login(email, password) {
  const payLoad = {
    email: email,
    password: password.split("")
  };

  return await Socket.idmPOST(loginEP, payLoad);
}

async function register(email, password)
{
  const payLoad = {
    "email": email,
    "password": password.split("")
  }
  return await Socket.idmPOST(registerEP,payLoad);
}

export default {
  login,
  register
};
