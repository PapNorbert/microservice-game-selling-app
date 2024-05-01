import { useSearchParams } from "react-router-dom";

export function useSearchParamsState(searchParamName: string, defaultValue: string) {

  const [searchParams, setSearchParams] = useSearchParams();
  const acquiredSearchParam = searchParams.get(searchParamName);
  const searchParamsState = acquiredSearchParam ?? defaultValue;

  function setSearchParamsState(newState: string) {
    setSearchParams((searchParams) => {
      searchParams.set(searchParamName, newState);
      return searchParams;
    });
  };

  return [searchParamsState, setSearchParamsState];
}