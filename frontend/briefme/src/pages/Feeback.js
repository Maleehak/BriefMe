import React, { useState } from 'react';

const Feedback = () => {
  const [feedback, setFeedback] = useState('');
  const [rating, setRating] = useState(0);

  const emojis = ['', '', '', '', ''];

  const handleFeedbackChange = (event) => {
    setFeedback(event.target.value);
  };

  const handleRatingClick = (index) => {
    setRating(index + 1);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    // Submit feedback data here (e.g., send to server)
    console.log('Feedback:', feedback);
    console.log('Rating:', rating);

    setFeedback('');
    setRating(0);
  };

  return (
    <div className="feedback-container">
      <h2>Leave Feedback</h2>
      <form onSubmit={handleSubmit}>
        <textarea
          value={feedback}
          onChange={handleFeedbackChange}
          placeholder="Enter your feedback"
        />
        <div className="rating">
          {emojis.map((emoji, index) => (
            <span key={index} onClick={() => handleRatingClick(index)}>
              {emoji} {rating === index + 1 ? ' (selected)' : ''}
            </span>
          ))}
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default Feedback;
