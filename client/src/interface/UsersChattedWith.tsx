import { Pagination } from './paginationInterface';

export interface UsersChattedWith {
  users: User[];
  pagination: Pagination
}

export interface User {
  userId: number;
  firstName: string;
  lastName: string;
  registrationDate: string;
  username: string;
}
