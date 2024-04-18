import React from "react";

const Left = () => {

  const handleExternalLinkClick = (event) => {
    event.preventDefault();
    window.open(event.target.href, '_blank');
  };
  
  return (
    <div className="vertical-component-left">
      <div className="title">
        Brief Me
      </div>
      <div className="description">
        Just enter the url of any Youtube video and get summary within few seconds.
      </div>
      <img src="image.png" alt="Left Section" className="image"/>

      <footer class="left-footer">
        <a href="https://github.com/Maleehak/BriefMe" onClick={handleExternalLinkClick}>
           View on Github
        </a>
        <div class="divider">|</div>
        <a href="https://www.linkedin.com/in/maleehak/" onClick={handleExternalLinkClick}>
           About the Author
        </a>
      
      </footer>
    </div>
  );
}

export default Left;