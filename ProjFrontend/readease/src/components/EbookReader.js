import React, { useEffect, useRef, useState } from 'react';
import ePub from 'epubjs'; 
import '../css/EbookReader.css';
import { useAuth } from "../context/AuthContext";

const EbookReader = () => {
    const { userInfo } = useAuth();
    const viewerRef = useRef(null);
    const [bookUrl, setBookUrl] = useState(null);
    const [rendition, setRendition] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [reservation, setReservation] = useState(null);
    const [bookInfo, setBookInfo] = useState(null);
   

    const epubFileName = 'test.epub'; 
    const epubUrl = `/epubs/${epubFileName}`;


    const handleRemoveEbook = async () => {
        if (!userInfo || !reservation) {
          console.error('Usuário não está autenticado ou não possui reserva ativa');
          return;
        }
      
        try {
          const response = await fetch(`http://localhost:8080/EbookReservations/finish/${userInfo.id}`, {
            method: 'POST'
          });
      
          if (response.ok) {
            console.log("Ebook reservation marked as finished");
            window.location.href = '/booksearch';
          } else {
            throw new Error('Falha ao finalizar a reserva do eBook');
          }
        } catch (error) {
          console.error('Erro:', error);
        }
      };
 
    
    const goToBookDetails = () => {
        window.location.href=`/book/${reservation?.ebookId}`;
    };

    const fetchBookInfo = (ebookId) => {
        fetch(`http://localhost:8080/books/details/${ebookId}`)
            .then(response => response.json())
            .then(data => {
                console.log("Dados do livro recebidos:", data);
                setBookInfo(data);
            })
            .catch(error => console.error('Erro ao buscar informações do livro:', error));
    };

    const fetchReservationDetails = () => {
        if (userInfo && userInfo.id) {
            setIsLoading(true);
            fetch(`http://localhost:8080/EbookReservations/user/${userInfo.id}`)
                .then(response => response.json())
                .then(data => {
                    setReservation(data[0]);
                    checkEpubExistsAndLoad();
                })
                .catch(error => {
                    console.error('Error fetching eBook reservation:', error);
                    setError(error);
                })
                .finally(() => setIsLoading(false));
        }
    };

    const checkEpubExistsAndLoad = async () => {
        try {
            const response = await fetch(epubUrl, { method: 'HEAD' });
            if (!response.ok) {
                fetchEbook(`http://localhost:8080/EbookReservations/epubs`);
            } else {
                setBookUrl(epubUrl);
            }
        } catch (error) {
            console.error('Erro ao verificar o eBook:', error);
            setError(error);
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === 'ArrowRight') {
            goToNextPage();
        } else if (event.key === 'ArrowLeft') {
            goToPrevPage();
        }
    };

    useEffect(() => {
        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, [rendition]);

    const fetchEbook = (url) => {
        setIsLoading(true);
        fetch(url)
            .then(response => response.blob())
            .then(blob => {
                setBookUrl(URL.createObjectURL(blob));
            })
            .catch(error => {
               
                setError(error);
            })
            .finally(() => setIsLoading(false));
    };

    useEffect(() => {
       
        fetchReservationDetails();

    }, [userInfo]);
    
    useEffect(() => {
        if (reservation) {
            fetchBookInfo(reservation.ebookId);
        }
    }, [reservation]);
    
    useEffect(() => {
        if (bookUrl) {
           
            const book = ePub(bookUrl);
            const newRendition = book.renderTo(viewerRef.current, { width: '100%', height: '100%' });
            newRendition.display().then(() => {
              
            }).catch(error => {
               
            });
            setRendition(newRendition);
            return () => URL.revokeObjectURL(bookUrl);
        }
    }, [bookUrl]);

    if (!reservation) {
        return <div className='ebook-reader-page'><h2 className='ebook-title'>Nenhum eBook reservado.</h2></div>;
    }

    const goToNextPage = () => {
        if (rendition && rendition.next) {
            rendition.next();
        }
    };
    
    const goToPrevPage = () => {
        if (rendition && rendition.prev) {
            rendition.prev();
        }
    };


    if (isLoading) return <div>Loading eBook...</div>;
    if (error) return <div>Error loading eBook: {error.message}</div>;

    return (
        <div className='ebook-reader-page'>
            <h2 className='ebook-title'> Title: {bookInfo?.volumeInfo?.title}</h2>
            <div className='ebook-div'>
            <div ref={viewerRef} className='ebook-viewer'></div>
            {rendition ? (
                <>
                    <button className="navigation-button prev" onClick={goToPrevPage}>&larr;</button>
                    <button className="navigation-button next" onClick={goToNextPage}>&rarr;</button>
                </>
            ) : (
                <div>Loading or no books available</div>
            )}
            </div>
            <div className='ebook-footer'>
                <button  className="extend" onClick={goToBookDetails}>Book Details</button>
                <button onClick={handleRemoveEbook}>Delete Ebook</button>
            </div>
        </div>
    );
};

export default EbookReader;
