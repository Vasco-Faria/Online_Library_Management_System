import React , { useState, useEffect }  from "react";
import FavoriteBookCard from './FavoriteBookCard';
import '../css/favorites.css';
import { useAuth } from '../context/AuthContext';
import axios from "axios";


const Favorites = () => {
    const { userInfo } = useAuth();
    const [favorites, setFavorites] = useState([]); 
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (userInfo) {
            console.log(userInfo.id);
            const fetchFavorites = async () => {
                try {
                    setIsLoading(true);
                    const response = await axios.get(`http://localhost:8080/favorites/user/${userInfo.id}`);
                    console.log(response.data);
                    setFavorites(response.data);
                } catch (error) {
                    console.error('Erro ao buscar favoritos:', error);
                } finally {
                    setIsLoading(false);
                }
            };
            fetchFavorites();
        }
    }, [userInfo]);

    const removeFavorite = (bookId) => {
        setFavorites(favorites.filter(id => id !== bookId));
    };


    const renderFavorites = () => {
        if (isLoading) {
            return <div style={{'color':'white'}}>Carregando...</div>;
        }

        if (favorites.length === 0) {
            return <div style={{'color':'white'}}>No favorites books at the moment.</div>;
        }

        return favorites.map(bookId => (
            <FavoriteBookCard key={bookId} bookId={bookId} onRemoveFavorite={removeFavorite} />
        ));
    };
return (

    <div className="favorites-page">
            <h2>Favorites Books</h2>
            <div className="favorites-grid">
                {renderFavorites()}
            </div>
        </div>
    
    
    );

}
export default Favorites;