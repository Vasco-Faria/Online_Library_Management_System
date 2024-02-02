import React, { useEffect, useState } from "react";
import '../css/FavoriteBookCard.css';
import PropTypes from 'prop-types';
import axios from 'axios';
import { useAuth } from "../context/AuthContext";


const FavoriteBookCard = ({ bookId, onRemoveFavorite}) => {

    const { userInfo } = useAuth();
    const userId=userInfo.id;
    const [bookDetails, setBookDetails] = useState(null);
      
    useEffect(() => {
        const fetchBookDetails = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/books/details/${bookId}`);
            setBookDetails(response.data);
        } catch (error) {
            console.error('Erro ao buscar detalhes do livro:', error);
        }
        };
    
        if (bookId) {
            fetchBookDetails();
        }

    }, [bookId]);


    const handleRemoveFavorite = async () => {
        try {
          await axios.post(`http://localhost:8080/favorites/remove`, { userId: userId, bookId });
          onRemoveFavorite(bookId);
        } catch (error) {
          console.error(`Erro ao remover favorito:`, error);
        }
      };

    const handleViewDetails = () => {
        window.location.href = `/book/${bookId}`;
    };
    
    if (!bookDetails) {
        return <div>Loading...</div>; 
    }
    return (
        <div className="favorite-book-card">
            <div className="book-image">
                <img src={bookDetails.volumeInfo.imageLinks?.thumbnail || ''} alt={bookDetails.volumeInfo.title}/>
            </div>
            <div className="book-info">
                <h3>{bookDetails.volumeInfo.title}</h3>
            </div>
            <div className="book-actions">
                <button  style={{'backgroundColor':'#6eff3e'}} onClick={() => handleViewDetails()}>View Details</button>
                <button style={{'backgroundColor':'red'}} onClick={() => handleRemoveFavorite()}>Remove</button>
            </div>
        </div>
    );
};


export default FavoriteBookCard;