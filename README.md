

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#quick-demo">Quick Demo</a></li>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#installation">Features</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

## Quick Demo
<img src="https://github.com/Maleehak/BriefMe/blob/main/frontend/briefme/public/Screen%20Recording%202024-04-19%20at%208.07.21%20PM%20(1).gif">

<!-- ABOUT THE PROJECT -->
## About The Project
The sotware is designed to summarise the content of Youtube video within few seconds. This is a full stack open-source Youtube video summarizer built using Java and React.Js. 
This is the experimental project to utilize the existing/custom tools to extract the video in a few seconds. Backend application exposes two versions of APIs. Here's how both work:
* v1: 
    * How it works:
        1. Extract Audio from the Youtube video using FFmpeg/Yt-Dlp
        2. Convert Audio to Speech using Google Speech to Text API
        3. Summarize the extracted text using Custom Text Summarize that builds context using Similarity Matrix and Text Rank Algorithm
    * Advantage:
        - Accurate summary generation that does not relies on subtitles unlike other Youtube Video Summarizers in the market
    * Disadvantage:
        - Since downloading the audio takes time and resources, this task is computationally expensive and takes around 2-5 mins to summarize average size videos
* v2:
  * How it works:
        1. Extract Subtitles from the Youtube video using Yt-Dlp
        2. Summarize the extracted text using Google Vertex AI
    * Advantage:
        - Accurately summarizes the average size videos generation within 2-4s!
    * Disadvantage:
        - Since it relies on subtitles, if the video does not contains the subtitles, this method fails
     
<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Features
* Youtube video to audio converter using Yt-Dlp
* MP4 video to audio converter using FFmpeg
* Audio to text conversion using Google Speech to Text
* Custom Text Summarizer using Similarity Matrix and Text Rank Algorithm
* Youtube video subtitles extracting using Yt-Dlp
* Text Summarization using Google Vertex AI
* Containerized Java Backend application using Docker
* Fully CI/CD backend pipeline using Github Actions
* Containerized Backend is deployed to Google Clound Run
* React Frontend is deployed on Github Pages
* Secrets are managed using Github Action Secrets
* Google service account credentials are encrypted 

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

