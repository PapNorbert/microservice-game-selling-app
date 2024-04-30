import { UserRoleTypes } from "../enums/UserRoleTypes";

export default function payloadRoleToUserRole(userRoleName : String) : UserRoleTypes | undefined {
  if (userRoleName === 'USER') {
    return UserRoleTypes.USER
  }
  if (userRoleName === 'ADMIN') {
    return UserRoleTypes.ADMIN
  }
  return undefined;
}