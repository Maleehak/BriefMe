import React, { useState } from 'react';

const SelectBox = ({ onSelect }) => {
  const [selectedValue, setSelectedValue] = useState(1);

  const handleChange = (event) => {
    const value = parseInt(event.target.value, 10);
    setSelectedValue(value);
    onSelect(value);
  };

  return (
    <select value={selectedValue} onChange={handleChange} className='selection-box'>
      {[...Array(6)].map((_, index) => (
        <option key={index + 5} value={index + 5}>{index + 5}</option>
      ))}
    </select>
  );
};

export default SelectBox;
