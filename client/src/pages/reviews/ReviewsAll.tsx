import { FormEvent, useContext, useEffect, useState } from "react";
import { keepPreviousData, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Alert, Button, Container, Form } from "react-bootstrap";
import { Navigate, useLocation, useNavigate, useParams } from "react-router-dom";

import { SearchContext } from "../../context/SearchContextProvider";
import {
  apiPrefix, sellerParamName,
  limitQuerryParamDefault, limitQuerryParamName, pageQuerryParamDefault, pageQuerryParamName,
} from '../../config/application.json';
import configuredAxios from "../../axios/configuredAxios";
import PaginationElement from "../../components/PaginationElement";
import Limit from "../../components/Limit";
import useAuth from "../../hooks/useAuth";
import { Reviews } from "../../interface/review/reviewsInterface";
import ReviewElement from "../../components/ReviewElement";
import { ReviewCreation } from "../../interface/review/reviewCreationInterface";
import { ErrorResponseData } from "../../interface/errorResponseInterface";
import { User } from "../../interface/UsersChattedWith";
import UserElement from "../../components/UserElement";

export default function ReviewsAll() {
  const { sellerId } = useParams();
  const [seller, setSeller] = useState<User | null>();
  const { auth } = useAuth();
  const [reviewsUrl, setReviewsUrl] = useState<string>(`/${apiPrefix}/reviews?${sellerParamName}=${sellerId}`);
  const { limit, page } = useContext(SearchContext);
  const location = useLocation();
  const navigate = useNavigate();
  const [insertReviewText, setInsertReviewText] = useState<string>('');
  const [insertReviewTextError, setInsertReviewTextError] = useState<string>('');
  const reviewsCreateUrl = `/${apiPrefix}/reviews`;
  const queryClient = useQueryClient();


  const { data: reviewsData, isError, error, isLoading } =
    useQuery<AxiosResponse<Reviews>, AxiosError>({
      queryKey: ["reviews", reviewsUrl],
      queryFn: queryFunction,
      retry: false,
      placeholderData: keepPreviousData, // keeps the last succesful fetch as well beside current 
    });

  const { mutate: mutatePost } = useMutation({
    mutationFn: postMutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });

  
  useEffect(() => {
    // get user details
    configuredAxios.get(`/${apiPrefix}/users/${sellerId}`)
      .then((response) => {
        setSeller(response?.data || null);
      })
      .catch(() => {
        setSeller(null);
      })
  }, [sellerId]);

  useEffect(() => {
    if (!sellerId) {
      return;
    }
    const queryParams = new URLSearchParams();
    queryParams.set(sellerParamName, sellerId);

    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, page);
    }

    setReviewsUrl(`/${apiPrefix}/reviews?${queryParams.toString()}`)
  }, [limit, page, sellerId
  ]);

  function queryFunction() {
    return configuredAxios.get(reviewsUrl);
  }

  function postMutationFunction(data: ReviewCreation) {
    return configuredAxios.post(reviewsCreateUrl, data);
  }

  function handleSubmitSucces() {
    if (error) {
      setInsertReviewTextError('');
    }
    setInsertReviewText('');
    queryClient.invalidateQueries({ queryKey: ['reviews'] });
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setInsertReviewTextError('Error connecting to the server')
    } else {
      setInsertReviewTextError(error.response?.data.errorMessage || 'Error updating saved status');
    }
  }


  function handleCreateReview(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (!sellerId) {
      return;
    }
    if (!auth.logged_in || !auth.userId) {
      navigate('/login');
      return;
    }
    if (insertReviewText === '' || insertReviewText === null) {
      setInsertReviewTextError('Cannot create a review without text.');
    } else {
      const postData: ReviewCreation = {
        reviewerId: auth.userId,
        reviewText: insertReviewText,
        sellerId: parseInt(sellerId)
      }
      mutatePost(postData);
    }
  }

  function clearReview() {
    setInsertReviewText('');
    setInsertReviewTextError('');
  }


  if( seller === null ) {
    return (
      <>
        <h2 className="error">User reviews not found!</h2>
        <h3 className="clickable text-center fst-italic text-decoration-underline" onClick={() => navigate('/announcements')}>Announcements</h3>
      </>
    ) 
  }

  if (isLoading) {
    return (
      <>
        <h2 className="text-center">Loading...</h2>
      </>
    )
  }

  if (isError) {
    if (error.response?.status === 401) {
      return (
        <Navigate to='/login' state={{ from: location }} replace />
      )
    }
    return (
      <>
        <h2 className="error">{error.message || 'Sorry, there was an error!'}</h2>
      </>
    )
  }


  return (
    <>
      <h1>Reviews</h1>
      {
        seller &&
        <UserElement user={seller} />
      }
      <Container>
        <h3>Found {reviewsData?.data.pagination.totalCount} results</h3>
        <Limit />
        {
          reviewsData?.data &&
            reviewsData.data.reviews.length > 0 ? (
            <Container>
              {reviewsData?.data.reviews.map(currentElement => (
                <ReviewElement review={currentElement} key={currentElement.reviewId} />
              ))}
              < PaginationElement totalPages={reviewsData?.data.pagination.totalPages} />

            </Container>
          )
            :
            <h3>No Reviews found!</h3>
        }
      </Container>
      {auth.logged_in &&
        <Form className='mt-3' onSubmit={handleCreateReview} >
          {/* form to add a review  */}
          <Form.Control as='textarea' rows={4} className='mb-3'
            placeholder='Write a review'
            value={insertReviewText} isInvalid={!!insertReviewTextError} autoComplete='off'
            onChange={e => { setInsertReviewText(e.target.value) }}
          />
          <Alert key='danger' variant='danger' show={insertReviewTextError !== ''}
            onClose={() => setInsertReviewTextError('')} dismissible >
            {insertReviewTextError}
          </Alert>
          <Button type='submit' variant='light' className='border border-2 mb-5'
            disabled={insertReviewText === '' || insertReviewText === undefined}>
            Create review
          </Button>
          <Button variant='light' key='clear' className='mx-2 border border-2 mb-5' onClick={clearReview}>
            Cancel
          </Button>
        </Form>

      }
    </>
  )
}
