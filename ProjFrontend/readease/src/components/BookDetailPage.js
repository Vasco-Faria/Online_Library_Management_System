import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import '../css/BookDetailPage.css'
import "./Loading";
import Loading from './Loading';
import BookButton from './BookButton';
import { useAuth } from "../context/AuthContext";
import axios from 'axios';
import ConfirmationModal from './ConfirmationModal';
import ReviewCard from './ReviewCard'
import AnimatedInput from './AnimatedInput';



const BookDetailPage = () => {
  const { id } = useParams();
  const [book, setBook] = useState();
  const [loading, setLoading] = useState(true);
  const { userInfo } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [isFavorite, setIsFavorite] = useState(false);
  const [isAvailable, setIsAvailable] = useState(false);
  const [canAddReview, setCanAddReview] = useState(false);
  const [reviews, setReviews] = useState([]);
  const [reviewAdded, setReviewAdded] = useState(false);



  useEffect(() => {
    const fetchBookById = async () => {
      try {
        const response = await fetch(`http://localhost:8080/books/details/${id}`);
        const data = await response.json();
        setBook(data);
        setIsAvailable(data.isAvailable);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching book details:', error);
        setLoading(false);
      }
    };

    if (userInfo) {
      checkFavorite();
    }

    fetchBookById();
    checkUserReservation();
    fetchReviews();
  
  }, [id, userInfo,reviewAdded]);


  const fetchReviews = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/reviews/byBook/${id}`);
      if (response.status === 200) {
        console.log(reviews);
        setReviews(response.data);
      }
    } catch (error) {
      console.error('Erro ao buscar reviews:', error);
    }
  };
  const checkFavorite = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/favorites/user/${userInfo.id}`);
      if (response.data.includes(id)) {
        setIsFavorite(true);
      }
    } catch (error) {
      console.error('Erro ao verificar favoritos:', error);
    }
  };
  

  const checkUserReservation = async () => {
    if (!userInfo) return;
  
    try {
      const response = await axios.get(`http://localhost:8080/api/users/userReservations/${userInfo.id}/book/${id}`);
      if (response.status === 200) {
        setCanAddReview(true);
      }
    } catch (error) {
      console.error('Erro ao verificar reservas:', error);
    }
  };
  


  if (loading) {
    return <Loading/>;
  }

  if (!book || book.error) {
    return <div>Error loading book details</div>;
  }

  function downloadRandomPdf() {
    if (!userInfo) {
      console.error('Usuário não está autenticado');
      return;
    }
  
    fetch('http://localhost:8080/books/pdf/download', {
        method: 'GET',
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const a = document.createElement('a');
        a.href = url;
        a.download = 'random.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    })
    .catch(error => console.error('Error:', error));
}

const handleDownloadClick = (event) => {
  event.preventDefault();
  downloadRandomPdf();
};


const handleReserveEbook = async () => {
  if (!userInfo) {
    console.error('Usuário não está autenticado');
    return;
  }

  const requestBody = {
    ebookId: book.id,
    userId: userInfo.id,
  };

  

  try {
    const response = await fetch('http://localhost:8080/EbookReservations/reserve', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody)
    });

    if (response.ok) {
      alert("Reserva de eBook atualizada com sucesso!");
    } else if (response.status === 409) { 
      setShowModal(true); 
    }
  } catch (error) {
    console.error('Erro ao reservar eBook:', error);
  }
};

const handleReserveEbook2 = async () => {

  const requestBody = {
      userId: userInfo.id,
      ebookId: book.id
  };

  try {
      const response = await axios.post('http://localhost:8080/EbookReservations/changeAndReserveEbook', requestBody);

      if (response.status === 200) {
          alert("Reserva de eBook atualizada com sucesso!");
          setShowModal(false);
         
      }
  } catch (error) {
      console.error("Erro ao reservar eBook:", error);
      }
  }

const handleReserveBook = async () => {
  if (!userInfo) {
    console.error('Usuário não está autenticado');
    return;
  }

  const reservationRequest = {
    bookId: book.id,
    userId: userInfo.id, 
  };

  try {
    const response = await axios.post('http://localhost:8080/BookReservations/reserve', reservationRequest);
      alert(response.data, "tem 2 dias para o levantar na biblioteca");
      setIsAvailable(false);
    

  } catch (error) {
    console.error('Error doing the reserve', error); 
    alert('Error doing the reserve');
  }
};

const handleFavorite = async () => {
  const url = `http://localhost:8080/favorites/${isFavorite ? 'remove' : 'add'}`;
  try {
    await axios.post(url, { userId: userInfo.id, bookId: id });
    setIsFavorite(!isFavorite);
  } catch (error) {
    console.error(`Erro ao ${isFavorite ? 'remover' : 'adicionar'} favorito:`, error);
  }
};

const addNewReview = (newReview) => {
  setReviews(prevReviews => [...prevReviews, newReview]);
};

const isUser = userInfo?.tipo === "normaluser";




  return (
    <div className='book-detail-page'>
        <div className='detail-left'>
            <div className='details-1'>
              <h2 style={{ "color":"white"}}>{book.volumeInfo.title}</h2>
              <p>Author: {book.volumeInfo.authors && book.volumeInfo.authors.join(', ')}</p>
              <p>Published Date: {book.volumeInfo.publishedDate}</p>
              <p>Publisher: {book.volumeInfo.publisher}</p>
              <p>Categories: </p>
              {book.volumeInfo.categories ? book.volumeInfo.categories.map((category, index) => <span key={index}>{
                index === book.volumeInfo.categories.length - 1 ? category : category + ', '
              }</span>) : ''}
              <p>Page Count: {book.volumeInfo.pageCount}</p>
              <p>Language: {book.volumeInfo.language}</p>
              
            </div>
            <div className='details-2'>
              <p>Description:</p>
              <div className='book-description' dangerouslySetInnerHTML={{ __html: book.volumeInfo.description }} />
            </div>

        </div>
        <div className='detail-right'>
            <div className='book-image'>
              <img src={book.volumeInfo.imageLinks?.thumbnail || ''} alt={book.volumeInfo.title}/>
            </div>
            {userInfo && isUser ? (
            <div className='book-buttons'>
              <div className='buttons1'>
              <BookButton color={isFavorite ? 'red' : 'white'} onClick={handleFavorite}>
                {isFavorite ? 'Remove Favorite' : 'Add Favorite'}
              </BookButton>
              <BookButton color='#1e9bff' onClick={handleDownloadClick} >PDF</BookButton>
              </div>
              <div className='buttons2'>
                <BookButton color='#ff1867' onClick={handleReserveEbook}>Ebook</BookButton>
                {isAvailable ? (
                  <BookButton color="#6eff3e" onClick={handleReserveBook} >Reserve Book</BookButton>
                ) : (
                  <BookButton color="black">Not Available</BookButton>
                )}
              </div>
            </div>
            ) : userInfo && !isUser ? (
              <div className='book-librarian-pdf'>
                    <BookButton color='#1e9bff' onClick={handleDownloadClick}>PDF</BookButton>
              </div>
              ) : null
            } 

          {userInfo && isUser ? (
            <div className='tell-availability'><a><p>Tell me when is Available  <i className="fa-solid fa-bell"></i> (Put the book as favorite)</p></a></div>
          ):null}
            </div>
        <div className='book-reviews'>
              <h2 style={{"paddingBottom":"2rem"}}>What our users are saying</h2>
              {canAddReview && (
                <div className='review-review-container'>
                <AnimatedInput
                userId={userInfo.id}
                bookId={id}
                onReviewAdded={addNewReview}
                />

                </div>
              )}
              {reviews.length > 0 ? (
                <div className='review'>
                  {reviews.map(review => (
                    <ReviewCard 
                    key={review.id}
                    userName={review.user?.nome || 'Usuário Anônimo'} 
                    content={review.content}
                    date={review.date ? new Date(review.date).toLocaleString() : 'Unknown date'}
                    rating={review.rating}
                />
                  ))}
                </div>
              ) : (
                <p style={{"textAlign":"center","color":"white"}}>No reviews available for this book.</p>
              )}            
        </div>
        {showModal && <ConfirmationModal  text="User already has a reserved ebook. Do you want to change the reservation?"onConfirm={handleReserveEbook2} onCancel={() => setShowModal(false)} />}
    </div>
  );
};


export default BookDetailPage;