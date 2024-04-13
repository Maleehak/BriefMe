import React, { useState } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClipboard } from '@fortawesome/free-solid-svg-icons';
import LinearProgress from '@mui/material/LinearProgress';
import Box from '@mui/material/Box';
import axios from 'axios';

const Right = () => {
  const [inputValue, setInputValue] = useState('');
  const [summary, setSummary] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
      // Handle changes in the input field
    setInputValue(event.target.value);
  };

  const handleClick = () => {
    console.log('Button clicked!');
    fetchSummary()
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(summary);
  };

  const fetchSummary = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/get-summary', {
        params: {
          video: inputValue,
          lines: 6
        },
      }); 
      setSummary(response.data);
    } catch (error) {
      setSummary("Unable to process request at the momemt. " +error);
    } finally {
      setLoading(false);
    }
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
      {loading && 
        <Box sx={{ width: '82%', color: 'orange' }}>
          <LinearProgress color="inherit" />
        </Box>
      }
      <textarea
        type="text"
        value={summary}
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