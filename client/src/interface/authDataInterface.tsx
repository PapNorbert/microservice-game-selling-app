export interface AuthData {
  logged_in: boolean;
  username: string | undefined
  [key: string]: string | boolean | undefined
}