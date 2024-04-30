import { UserRoleTypes } from '../enums/UserRoleTypes'

export interface AuthData {
  logged_in: boolean;
  username: string | undefined;
  role: UserRoleTypes | undefined;
}