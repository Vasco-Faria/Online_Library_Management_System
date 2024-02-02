import React,{ useState, useEffect  } from 'react';
import axios from 'axios';
import PropTypes from 'prop-types';
import '../css/BookRecommendationCard.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { useAuth } from "../context/AuthContext";

const BookRecommendationCard = ({ book,isWeeklyRecommendation }) => {
  const { volumeInfo,id } = book;
  const title = volumeInfo.title;
  const author = volumeInfo.authors ? volumeInfo.authors.join(', ') : 'Unknown Author';
  const imageUrl = volumeInfo.imageLinks?.thumbnail || ''; 
  const [isFavorite, setIsFavorite] = useState(false);
  const { userInfo } = useAuth();

  const userId = userInfo ? userInfo.id : null;
  
  useEffect(() => {
    if (userInfo){
      const userId = userInfo ? userInfo.id : null;
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
      const userId = userInfo ? userInfo.id : null;
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
  
  const handleDetailsClick = () => {
    
    window.location.href=(`/book/${id}`);
  };

  return (
    <div className={`bookrec-card ${isWeeklyRecommendation ? 'weekly-recommendation' : ''}`}>
      <div className="card-content">
        <img src={imageUrl} alt={title} />
        <h3 style={{"color":" #2c293d"}}>{title}</h3>
        <p>Author: {author}</p>
        { !shouldRender  ?(
        <a onClick={toggleFavorite}>
          <i className={`fas fa-heart ${isFavorite ? 'fas fa-heart favorite' : 'far fa-heart'}`}style={{ color: isFavorite ? 'red' : 'black' }}></i>
        </a>
        ):null}
      </div>
      <div className="card-actions">
        <button onClick={handleDetailsClick}>View Details</button>
      </div>
    </div>
  );
};

BookRecommendationCard.propTypes = {
  book: PropTypes.object.isRequired,
}

export default BookRecommendationCard;