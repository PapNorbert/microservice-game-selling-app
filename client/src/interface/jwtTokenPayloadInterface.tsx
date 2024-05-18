import { JwtPayload } from "jwt-decode";

export interface JwtTokenPayloadInterface extends JwtPayload {
  role: string | undefined,
  userId: number | undefined
}