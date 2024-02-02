import React, { useState, useEffect, useRef } from 'react';
import BezierEasing from 'bezier-easing';
import axios from 'axios';

const AnimatedInput = ({userId, bookId,onReviewAdded }) => {
  const [focused, setFocused] = useState(false);
  const pathRef = useRef(null);
  const placeholderRef = useRef(null);
  const inputRef = useRef(null);
  const [reviewContent, setReviewContent] = useState('');

  const startAnimation = () => {
    if (inputRef.current && inputRef.current.value.length === 0) {
      setFocused(true);
      placeholderRef.current.classList.add('expand');
    }
  };

  const endAnimation = () => {
    if (inputRef.current && inputRef.current.value.length === 0) {
      setFocused(false);
      placeholderRef.current.classList.remove('expand');
    }
  };

  const animate = (pct) => {
    const newPct = 1 - 2 * Math.abs(0.5 - pct);
    const newBump = newPct * 25;
    pathRef.current.setAttribute('d', `m 0,30 c 16.920055,0 8.8823519,-${newBump} 30,-${newBump} 21.117648,0 13.001352,${newBump} 30,${newBump} h 260`);
  };

  useEffect(() => {
    let start = null;
    const duration = 600;
    const easing = BezierEasing(0.4, 0.0, 0.2, 1);

    const step = (timestamp) => {
      if (!start) start = timestamp;
      const progress = (timestamp - start);
      let pct = progress / duration;
      if (pct > 1) pct = 1;
      animate(pct);
      if (progress < duration) {
        window.requestAnimationFrame(step);
      } else {
        start = null;
      }
    };

    if (focused) {
      window.requestAnimationFrame(step);
    }
  }, [focused]);

  const handleSubmit = async () => {
    const reviewDTO = {
      userId,
      bookId,
      content: reviewContent
    };
    
    try {
      const response = await axios.post('http://localhost:8080/api/reviews/add', reviewDTO);
      if (response.status === 200) {
        alert('Review adicionada com sucesso!');
        setReviewContent('');
        onReviewAdded(response.data);
      }
    } catch (error) {
      console.error('Erro ao enviar review:', error);
    }
  };

  return (
    <div className="review-container">
      <div ref={placeholderRef} className="review-placeholder">Your Comment</div>
      <svg viewBox="0 0 500 100" width="500" height="100">
        <path ref={pathRef} stroke="#000" strokeWidth="2" fill="none" />
      </svg>
      <input ref={inputRef} type="text" value={reviewContent} onChange={(e) => setReviewContent(e.target.value)} onFocus={startAnimation} onBlur={endAnimation} />
      <button className="add-review-button" onClick={handleSubmit}>Submit Review</button>
    </div>
  );
};

export default AnimatedInput;