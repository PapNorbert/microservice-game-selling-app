import { createContext } from 'react';
import { ChildrenProps } from '../interface/childrenPropsInterface';

import {
  limitQuerryParamDefault, pageQuerryParamDefault,
  productNameParamDefault, consoleTypeParamDefault,
  productNameParamName, consoleTypeParamName,
  pageQuerryParamName, limitQuerryParamName
} from '../config/application.json'
import { useSearchParamsState } from '../hooks/useSearchParamsState';

interface ContextData {
  selectedConsole: string;
  setSelectedConsole: (newState: string) => void;
  productName: string;
  setProductName: (newState: string) => void;
  limit: string;
  setLimit: (newState: string) => void;
  page: string;
  setPage: (newState: string) => void;
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
  const [selectedConsole, setSelectedConsole] =
    useSearchParamsState(consoleTypeParamName, consoleTypeParamDefault);
  const [productName, setProductName] =
    useSearchParamsState(productNameParamName, productNameParamDefault);
  const [limit, setLimit] =
    useSearchParamsState(limitQuerryParamName, limitQuerryParamDefault);
  const [page, setPage] =
    useSearchParamsState(pageQuerryParamName, pageQuerryParamDefault);



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
