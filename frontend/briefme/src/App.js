import './App.css';
import Left from './components/Left';
import Right from './components/Right';
import React from 'react';

function App() {
  return (
    <div className="vertical-container">
      <Left />
      <Right />
    </div>
  );
}

export default App;
