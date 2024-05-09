import { Button, Card, Col, Row, Stack } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'

export default function GoToLoginCard() {
  const navigate = useNavigate();

  return (
    <Card className='mt-4'>
      <Stack direction='horizontal' gap={5}>
        Login to your CGS account or create a new account to contact this seller.
        <Button className='btn-orange-dark' onClick={() => { navigate('/login') }}>
          Log in / New account
        </Button>
      </Stack>
    </Card>
  )
}
