
import { Col, Container, Row } from "react-bootstrap"
import { AnnouncementDetailedResponse } from "../../../interface/Announcements/announcementDetailedInterface"
import ConsumerRightsCard from "./ConsumerRightsCard"
import AnnouncementDetailedCard from "./AnnouncementDetailedCard"
import OrderCard from "./OrderCard"
import SellerCard from "./SellerCard"
import useAuth from "../../../hooks/useAuth"
import GoToLoginCard from "./GoToLoginCard"
import AnnouncementOwnCard from "./AnnouncementOwnCard"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementDetailedLong({ announcement }: PropType) {
  const { auth } = useAuth();

  return (
    <Container>
      <Row>
        {
          (auth.logged_in && auth.userId === announcement.sellerId) ?
            // own announcement
            <AnnouncementOwnCard announcement={announcement} />
            :
            // not own announcement
            <>
              <Col>
                <AnnouncementDetailedCard announcement={announcement} />
                {!auth.logged_in &&
                  <GoToLoginCard />
                }
                <SellerCard sellerId={announcement.sellerId} sellerUsername={announcement.sellerUsername}
                  sellerRegistrationDate={announcement.sellerRegistrationDate} />

              </Col>
              <Col lg={{ span: 4 }}>
                <OrderCard productPrice={announcement.price} productName={announcement.soldGameDisc.name}/>
                <ConsumerRightsCard />
              </Col>
            </>
        }
      </Row>
    </Container>
  )
}
