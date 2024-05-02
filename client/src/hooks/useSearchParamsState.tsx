import { useSearchParams } from "react-router-dom";

export function useSearchParamsState(searchParamName: string, defaultValue: string): [string, (newState: string) => void] {

  const [searchParams, setSearchParams] = useSearchParams();
  const acquiredSearchParam = searchParams.get(searchParamName);
  const searchParamsState: string = acquiredSearchParam ?? defaultValue;

  function setSearchParamsState(newState: string) {
    setSearchParams((currentSearchParams) => {
      const newSearchParams = new URLSearchParams(currentSearchParams);
      newSearchParams.set(searchParamName, newState);
      return newSearchParams;
    });
  };

  return [searchParamsState, setSearchParamsState];
}