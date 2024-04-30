import { jwtDecode } from 'jwt-decode';

import { AuthData } from '../interface/authDataInterface';
import { JwtTokenPayloadInterface } from '../interface/jwtTokenPayloadInterface';
import payloadRoleToUserRole from './payloadRoleToUserRole';

export default function decodeJwtAccesToken(accesToken: string): AuthData {
  // returns auth information
  let jwtPayload: JwtTokenPayloadInterface = {
    sub: undefined,
    role: ''
  }
  let correct = false;
  if (accesToken) {
    jwtPayload = jwtDecode<JwtTokenPayloadInterface>(accesToken);
    if (jwtPayload.sub && jwtPayload.role && jwtPayload.iat && jwtPayload.exp) {
      correct = true;
    }
  }
  return {
    username: jwtPayload.sub,
    logged_in: correct,
    role: payloadRoleToUserRole(jwtPayload.role)
  }
}
