import { Card } from "react-bootstrap";

import { Order } from "../../interface/OrdersInterface"
import { dateFormatOptions } from "../../util/dateOptions";
import { convertConsoleTypeName } from "../../util/consoleTypeNameConversion";
import RowWithKeyValue from "../RowWithKeyValue";

interface PropType {
  order: Order;
}

export default function OrderDetailedElement({ order }: PropType) {
  return (
    <>
      <h1>Details of order nr. {order.orderId}</h1>
      <Card>
        <RowWithKeyValue keyString="Order date:" valueString={new Date(order.orderDate).toLocaleDateString('ro-RO', dateFormatOptions)} />

        <RowWithKeyValue keyString="Order address:" valueString={order.orderAddress} />
        <RowWithKeyValue keyString="Total price:" valueString={order.price.toString()} />
        <RowWithKeyValue keyString="Product price price:" valueString={order.announcement.price.toString()} />
        <RowWithKeyValue keyString="Product name:" valueString={order.announcement.soldGameDiscName} />
        <RowWithKeyValue keyString="Console:" valueString={convertConsoleTypeName(order.announcement.soldGameDiscType) || 'Unknown'} />
        <RowWithKeyValue keyString="Condition:" valueString={order.announcement.newDisc ? 'New' : 'Used'} />
      </Card>
    </>
  )
}
