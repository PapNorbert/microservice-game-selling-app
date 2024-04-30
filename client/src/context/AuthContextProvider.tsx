import { createContext, useEffect, useState } from 'react';

import decodeJwtAccesToken from '../util/decodeJwt';
import { ChildrenProps } from '../interface/childrenPropsInterface';
import { AuthData } from '../interface/authDataInterface';
import configuredAxios from '../axios/configuredAxios';
import { apiPrefix } from '../config/application.json';


interface ContextData {
  auth: AuthData;
  setAuth: React.Dispatch<React.SetStateAction<AuthData>>;
  loginExpired: boolean;
  setLoginExpired: React.Dispatch<React.SetStateAction<boolean>>;
}

const initialAuthState: AuthData = {
  logged_in: false,
  username: undefined,
  role: undefined
}

const initialContextData: ContextData = {
  auth: initialAuthState,
  setAuth: () => { },
  loginExpired: false,
  setLoginExpired: () => { },
}

export const AuthContext = createContext<ContextData>(initialContextData);


export default function AuthContextProvider({ children }: ChildrenProps) {

  const [loading, setLoading] = useState<boolean>(true);
  const [auth, setAuth] = useState<AuthData>(initialAuthState);
  const [loginExpired, setLoginExpired] = useState<boolean>(false);

  const value = {
    auth,
    setAuth,
    loginExpired,
    setLoginExpired
  }

  useEffect(() => {
    configuredAxios.get(`/${apiPrefix}/refresh`)
      .then((response) => {
        setAuth(decodeJwtAccesToken(response?.data?.token || null))
        setLoading(false);
      })
      // in case of error user is not logged in
      .catch(() => {
        setLoading(false);
      })
  }, []);

  return (!loading &&
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}
