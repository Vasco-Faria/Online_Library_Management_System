import React, { useState, useEffect } from 'react';
import "../css/SearchBooks.css";
import BookRecommendationCard  from './BookRecommendationCard';
import BookCard from './BookCard';


function BooksSearch() {

    const [query, setQuery] = useState('');
    const [author, setAuthor] = useState('');
    const [searchType, setSearchType] = useState('title');
    const [weeklyRecommendations, setWeeklyRecommendations] = useState([]);
    const [books,setBooks]=useState([])

  
    const handleSearch = async () => {
      try {

        if (!query && !author) {
          return;
        }
        const queryParams = new URLSearchParams({
          query: searchType === 'title' ? query : '',
          author: searchType === 'author' ? author : '',
        });
        const response = await fetch(`http://localhost:8080/books/search?${queryParams}`);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }


        const data = await response.json();
        setBooks(data.items)

      } catch (error) {
        console.error('Error fetching books:', error);
      }
    };

    const fetchWeeklyRecommendations = async () => {
      try {
        const response = await fetch('http://localhost:8080/books/weekly-recommendations');
  
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
  
        const data = await response.json();
        setWeeklyRecommendations(data);
      } catch (error) {
        console.error('Error fetching weekly recommendations:', error);
      }
    };
  

    useEffect(() => {
      fetchWeeklyRecommendations();
    }, []);


    const handleKeyPress = (e) => {
      if (e.key === 'Enter') {
        handleSearch();
      }
    };


    return (
      <div className='searchBooks-page'>
          <div className='searchBooks-left'>
          <h2>Search your Books</h2>
          <div className='search-container'>
            <input
              className='search-input'
              type="text"
              placeholder={`Search by ${searchType === 'title' ? 'title' : 'author'}`}
              value={searchType === 'title' ? query : author}
              onChange={(e) => {
                if (searchType === 'title') {
                  setQuery(e.target.value);
                } else {
                  setAuthor(e.target.value);
                }
              }}
              onKeyPress={handleKeyPress}
            />
            <div className='search-opitons'>
              <select className="custom-select" value={searchType} onChange={(e) => setSearchType(e.target.value)}>
                <option value="title">Title</option>
                <option value="author">Author</option>
              </select>
              <button   onClick={handleSearch} className="searchbutton"><i className="animation"></i><i className="fa fa-search"></i> Search<i className="animation"></i></button>
            </div>
          </div>
          <div className="books-container">
            {books.length > 0 ? (
              books.map((book) => (
                <BookCard key={book.id} book={book} />
              ))
            ) : (
              <div className='books-none'>
                <p className='p-search'>Search for your favorite book!</p>
              </div>
            )}
          </div>
        </div>
        <div className='searchBooks-right'>
          <h2>Weekly Recommendations</h2>
          <div className="recommendations-container">
            {weeklyRecommendations.map((book) => (
              <BookRecommendationCard key={book.id} book={book} isWeeklyRecommendation={true} />
            ))}
          </div>
        </div>
      </div>
    );
}

export default BooksSearch;
