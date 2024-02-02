import React from 'react';
import '../css/ReviewCard.css'


const ReviewCard = ({userName, content, date, rating} ) => {
   
   

    return (
        <div id="content">
            <div class="testimonial">
                <blockquote>
                    {content}
                   
                </blockquote>
                <div></div>
                <p>
                {userName} &mdash; {date}
                </p>
            </div>
        </div>
    );
};

export default ReviewCard;
