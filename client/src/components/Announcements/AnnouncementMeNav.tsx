import { Nav } from 'react-bootstrap'

interface PropType {
  sold: boolean;
  setSold: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function AnnouncementMeNav({sold, setSold} : PropType) {

  return (
    <Nav variant='tabs' defaultActiveKey='false' className='tabs-nav'
        activeKey={sold.toString()}
        onSelect={(eventKey) => { setSold(eventKey === 'true') }}>
        <Nav.Item>
          <Nav.Link eventKey='false' >
            Active announcements
          </Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey='true' >
            Sold announcements
          </Nav.Link>
        </Nav.Item>
      </Nav>
  )
}
