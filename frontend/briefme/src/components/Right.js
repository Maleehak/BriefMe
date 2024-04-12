import React, { useState } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClipboard } from '@fortawesome/free-solid-svg-icons';

const Right = () => {
  const [inputValue, setInputValue] = useState('');
  const [outputValue, setOutputValue] = useState('');

  const handleChange = (event) => {
      // Handle changes in the input field
    setInputValue(event.target.value);
  };

  const handleClick = () => {
    console.log('Button clicked!');
    // Add your custom logic here
    setOutputValue(inputValue);
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(inputValue);
  };

  return (
    <div className="vertical-component-right">
      <input
        type="text"
        value={inputValue}
        onChange={handleChange}
        placeholder="Enter url here"
        className="url-input-textbox" 
      />
      <div style={{ display: "flex", width: "83%" }}>
        <button onClick={handleClick} className="submit-btn">
          Click Me
        </button>
      </div>
      <textarea
        type="text"
        value={outputValue}
        onChange={handleChange}
        className="summary-textbox" 
      />
      <FontAwesomeIcon
          icon={faClipboard}
          style={{
            position: 'absolute',
            right: '8%',
            top: '39%',
            transform: 'translateY(-50%)',
            cursor: 'pointer'
          }}
          onClick={handleCopy}
      />
    </div>
  );
}

export default Right;