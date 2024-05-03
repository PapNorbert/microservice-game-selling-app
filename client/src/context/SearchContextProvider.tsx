import { createContext, useRef } from 'react';
import { ChildrenProps } from '../interface/childrenPropsInterface';

import {
  limitQuerryParamDefault, pageQuerryParamDefault, productNameParamDefault, consoleTypeParamDefault,
  productNameParamName, consoleTypeParamName, pageQuerryParamName, limitQuerryParamName,
  productTypeParamDefault, productTypeParamName, transportPaidParamDefault, transportPaidParamName,
  priceMaxParamName, priceMinParamName
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
  transportPaid: string;
  setTransportPaid: (newState: string) => void;
  productType: string;
  setProductType: (newState: string) => void;
  priceMin: string;
  setPriceMin: (newState: string) => void;
  priceMax: string;
  setPriceMax: (newState: string) => void;
  priceMinRef?: React.RefObject<HTMLInputElement>;
  priceMaxRef?: React.RefObject<HTMLInputElement>
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
  transportPaid: limitQuerryParamDefault,
  setTransportPaid: () => { },
  productType: pageQuerryParamDefault,
  setProductType: () => { },
  priceMin: '',
  setPriceMin: () => { },
  priceMax: '',
  setPriceMax: () => { },
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
  const [transportPaid, setTransportPaid] =
    useSearchParamsState(transportPaidParamName, transportPaidParamDefault);
  const [productType, setProductType] =
    useSearchParamsState(productTypeParamName, productTypeParamDefault);
  const [priceMin, setPriceMin] =
    useSearchParamsState(priceMinParamName, '');
  const [priceMax, setPriceMax] =
    useSearchParamsState(priceMaxParamName, '');
  const priceMinRef = useRef<HTMLInputElement>(null);
  const priceMaxRef = useRef<HTMLInputElement>(null);


  const value = {
    selectedConsole, setSelectedConsole,
    productName, setProductName,
    limit, setLimit,
    page, setPage,
    transportPaid, setTransportPaid,
    productType, setProductType,
    priceMinRef, priceMaxRef,
    priceMin, setPriceMin,
    priceMax, setPriceMax
  }

  return (
    <SearchContext.Provider value={value}>
      {children}
    </SearchContext.Provider>
  );
}
