import { jwtDecode, JwtPayload } from 'jwt-decode';

import { AuthData } from '../interface/authDataInterface';

export default function decodeJwtAccesToken(accesToken: string): AuthData {
  // returns auth information
  let jwtPayload: JwtPayload = {
    sub: undefined
  }
  let correct = false;
  if (accesToken) {
    jwtPayload = jwtDecode<JwtPayload>(accesToken);
    if (jwtPayload.sub && jwtPayload.iat && jwtPayload.exp) {
      correct = true;
    }
  }
  return {
    username: jwtPayload.sub,
    logged_in: correct
  }
}
