import { createContext, useState } from 'react';
import { ChildrenProps } from '../interface/childrenPropsInterface';

import {
  limitQuerryParamDefault, pageQuerryParamDefault,
  productNameParamDefault, consoleTypeParamDefault
} from '../config/application.json'

interface ContextData {
  selectedConsole: string;
  setSelectedConsole: React.Dispatch<React.SetStateAction<string>>;
  productName: string;
  setProductName: React.Dispatch<React.SetStateAction<string>>;
  limit: string;
  setLimit: React.Dispatch<React.SetStateAction<string>>;
  page: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
}



const initialContextData: ContextData = {
  selectedConsole: consoleTypeParamDefault,
  setSelectedConsole: () => { },
  productName: productNameParamDefault,
  setProductName: () => { },
  limit: limitQuerryParamDefault,
  setLimit: () => { },
  page: pageQuerryParamDefault,
  setPage: () => { },
}


export const SearchContext = createContext<ContextData>(initialContextData);

export default function SearchContextProvider({ children }: ChildrenProps) {
  const [selectedConsole, setSelectedConsole] = useState<string>(consoleTypeParamDefault);
  const [productName, setProductName] = useState<string>(productNameParamDefault);
  const [limit, setLimit] = useState<string>(limitQuerryParamDefault);
  const [page, setPage] = useState<string>(pageQuerryParamDefault);



  const value = {
    selectedConsole,
    setSelectedConsole,
    productName,
    setProductName,
    limit,
    setLimit,
    page,
    setPage
  }

  return (
    <SearchContext.Provider value={value}>
      {children}
    </SearchContext.Provider>
  );
}
