import { User } from "../UsersChattedWith";
import { Pagination } from "../paginationInterface";

export interface Reviews {
  reviews: Review[];
  pagination: Pagination
}

export interface Review {
  reviewId: number;
  reviewText: string;
  creationDate: string;
  reviewer: User
}
