import React,{ useState, useEffect } from 'react';
import axios from 'axios';
import PropTypes from 'prop-types';
import '../css/BookCard.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { useAuth } from "../context/AuthContext";

const BookCard = ({ book,isWeeklyRecommendation }) => {
  const { volumeInfo,id } = book;
  const title = volumeInfo.title;
  const author = volumeInfo.authors ? volumeInfo.authors.join(', ') : 'Unknown Author';
  const imageUrl = volumeInfo.imageLinks?.thumbnail || '';
  const publishDate = volumeInfo.publishedDate || "Unkown";
  const [isFavorite, setIsFavorite] = useState(false);
  const { userInfo } = useAuth();

  const userId = userInfo ? userInfo.id : null;


  
  const handleDetailsClick = () => {
    
    window.location.href=(`/book/${id}`);
  };



  useEffect(() => {

    if (userInfo){
      const userId=userInfo.id
      const checkFavorite = async () => {
        try {
          const response = await axios.get(`http://localhost:8080/favorites/user/${userId}`);
          if (response.data.includes(id)) {
            setIsFavorite(true);
          }
        } catch (error) {
          console.error('Erro ao verificar favoritos:', error);
        }
      };
    

      checkFavorite();

    }
  }, [id, userId]);

  const toggleFavorite = async () => {
    if (userInfo){
      const userId=userInfo.id
    try {
      const url = `http://localhost:8080/favorites/${isFavorite ? 'remove' : 'add'}`;
      await axios.post(url, { userId, bookId: id });
      setIsFavorite(!isFavorite);
    } catch (error) {
      console.error(`Erro ao ${isFavorite ? 'remover' : 'adicionar'} favorito:`, error);
    }
  }
  };

  const shouldRender = !userInfo || userInfo.tipo !== "bibliotecario";

  return (
    <div className="book-card">
     <div className="bookcard-content">
        <img src={imageUrl} alt={title}/>
          <div className='book-info'>
            <h3 style={{"color":" #2c293d"}}>{title}</h3>
            <p style={{"color":" #2c293d"}}>Author: {author}</p>
            <p style={{"color":" #2c293d"}}>Publish Date: {publishDate}</p>
            </div>
      <div className="book-details">
        {!shouldRender ?(
          <a onClick={toggleFavorite}>
          <i className={`far fa-heart ${isFavorite ? 'fas fa-heart favorite' : 'far fa-heart'}`} style={{ color: isFavorite ? 'red' : 'black' }}></i>
        </a>
        ) : null}
        <button onClick={handleDetailsClick}>View Details</button>
        </div>
      </div>
    </div>
  );
};


BookCard.propTypes = {
  book: PropTypes.object.isRequired,
}

export default BookCard;