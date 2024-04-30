import { UserRoleTypes } from '../enums/UserRoleTypes';
import useAuth from '../hooks/useAuth';
import { Navigate, Outlet, useLocation } from 'react-router-dom';

interface RequireAuthParameter {
  allowedRoles?: (UserRoleTypes | undefined)[];
}

export default function RequireAuth({ allowedRoles = [UserRoleTypes.USER, UserRoleTypes.ADMIN] }: RequireAuthParameter) {
  const { auth } = useAuth();
  const location = useLocation();

  return (
    allowedRoles?.includes(auth?.role)
      ? <Outlet />
      : auth?.logged_in
        ? <Navigate to='/unauthorized' state={{ from: location }} replace />
        : <Navigate to='/login' state={{ from: location }} replace />
  )

}
