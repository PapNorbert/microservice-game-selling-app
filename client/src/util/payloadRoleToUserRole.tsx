import { UserRoleTypes } from "../enums/UserRoleTypes";

export default function payloadRoleToUserRole(userRoleName : string | undefined) : UserRoleTypes | undefined {
  if (userRoleName === 'USER') {
    return UserRoleTypes.USER
  }
  if (userRoleName === 'ADMIN') {
    return UserRoleTypes.ADMIN
  }
  return undefined;
}