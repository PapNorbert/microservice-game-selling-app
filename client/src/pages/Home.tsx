import { Container } from "react-bootstrap";
import SearchBar from "../layouts/SearchBar";

export default function Home(): JSX.Element {

  return (
    <>
      <SearchBar />
      <Container className="mt-5">
        <h1>Home</h1>
      </Container>
    </>
  )
}