import { useContext } from 'react';
import { Form, Stack } from 'react-bootstrap'

import { SearchContext } from '../context/SearchContextProvider';
import useSetMultipleSearchParams from '../hooks/useSetMultipleSearchParams';
import { limitQuerryParamName, pageQuerryParamDefault, pageQuerryParamName } from '../config/application.json';


export default function Limit() {
  const { limit } = useContext(SearchContext);
  const [setMultipleSearchParams] = useSetMultipleSearchParams();

  function handleLimitChangeTo(newLimit: string) {
    // set new page an limit values
    if (!setMultipleSearchParams(
      [pageQuerryParamName, limitQuerryParamName],
      [pageQuerryParamDefault, newLimit])
    ) {
      console.log('Error changing limit');
    }
  }

  return (
    <Stack className="mb-5" direction='horizontal' gap={3}>
      <Form.Text as='h4' >
        Results per page:
      </Form.Text>
      <Form.Select value={limit} onChange={e => handleLimitChangeTo(e.target.value)} className='limit-select'>
        <option value='5'>5</option>
        <option value='10'>10</option>
        <option value='15'>15</option>
        <option value='30'>30</option>
      </Form.Select>
    </Stack>
  )
}
