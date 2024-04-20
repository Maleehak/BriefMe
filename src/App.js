import './App.css';
import Left from './components/Left';
import Right from './components/Right';
import React, { useState, useEffect } from 'react';

function App() {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <div className="vertical-container">
      {windowWidth < 768 ? null : <Left />}
      <Right />
    </div>
  );
}

export default App;
