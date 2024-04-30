import React, { useState } from "react";
import LinearProgress from '@mui/material/LinearProgress';
import Box from '@mui/material/Box';
import axios from 'axios';
import SelectBox from './SelectBox';

const Right = () => {
  const [inputUrl, setInputUrl] = useState('');
  const [summary, setSummary] = useState('');
  const [loading, setLoading] = useState(false);
  const [numberOfLines, setNumberOfLines] = useState(4);
  const [isValidURL, setisValidURL] = useState(false);

  const handleChange = (event) => {
    // Handle changes in the input field
    let url = event.target.value;
    setInputUrl(url);
    // Handles input validation state
    setisValidURL(isValidYoutubeUrl(url));
  };

  const isValidYoutubeUrl = (url) => {
    // Regular expression for matching YouTube URLs
    const youtubeRegex = /^(https?:\/\/)?(www\.)?(youtube\.com\/(?:[^/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
    return youtubeRegex.test(url);
  };

  const handleClick = () => {
    console.log("Button clicked!");
    if (isValidURL) {
      fetchSummary();
    }
  };

  const fetchSummary = async () => {
    setLoading(true);
    try {
      const response = await axios.get('https://brief-me-backend-app-26vtalgjia-el.a.run.app/api/get-summary/v2', {
        params: {
          video: inputUrl,
          lines: numberOfLines
        },
      });
      setSummary(response.data);
    } catch (error) {
      setSummary("Unable to process request at the momemt. " +error);
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = (value) => {
    setNumberOfLines(value);
  };

  return (
    <div className="vertical-component-right">
      <input type="text" value={inputUrl} onChange={handleChange} placeholder="Enter url here" className="url-input-textbox" />
      {!isValidURL && inputUrl && <p style={{ color: "red" }}>Please enter a valid YouTube URL</p>}
      <div className="selection-container">
        <p className="selection-text">No. of lines: </p>
        <SelectBox onSelect={handleSelect} />
      </div>
      <div style={{ display: "flex", width: "83%" }}>
        <button onClick={handleClick} className="submit-btn">
          Summarize
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
      </div>
  );
}

export default Right;