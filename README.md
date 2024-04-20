

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#quick-demo">Quick Demo</a></li>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="built-with">Built With</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li>
          <a href="#installation-guide">Installation guide</a>
          <ul><li><a href="#backend">Backend</a></li></ul>
          <ul><li><a href="#frontend">Frontend</a></li></ul>
        </li>
      </ul>
    </li> 
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

## Quick Demo
<img src="https://github.com/Maleehak/BriefMe/blob/main/frontend/briefme/public/Screen%20Recording%202024-04-19%20at%208.07.21%20PM%20(1).gif">

<!-- ABOUT THE PROJECT -->
## About The Project
The sotware is designed to summarise the content of Youtube video within few seconds. This is a full stack open-source Youtube video summarizer built using Springboot and React.Js. 
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

### Built With
<img height="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png">  <img height="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png"><img height="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png"> <img height="50" src="https://user-images.githubusercontent.com/25181517/183897015-94a058a6-b86e-4e42-a37f-bf92061753e5.png"> <img height="50" src="https://user-images.githubusercontent.com/25181517/183911547-990692bc-8411-4878-99a0-43506cdb69cf.png"> <img height="50" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png"> <img height="50" src="https://user-images.githubusercontent.com/25181517/183868728-b2e11072-00a5-47e2-8a4e-4ebbb2b8c554.png"> 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started
_Below is an example of how you can setup the project locally._

### Installation guide

#### Backend
1. Install and setup Java 11 on your system
2. For using the `yt-dlb` package, we need to have Python and Ffmpeg. Use the below command to install these dependencies:
   ```sh
   apt-get install -y python3 wget curl ffmpeg
   ```
4. Run the below command to install `yt-dlb` or use the [offical guide](https://github.com/yt-dlp/yt-dlp?tab=readme-ov-file#installation) for installation :
   ```sh
   wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -O /usr/local/bin/yt-dlp
   ```
5. Clone the repo
   ```sh
   git clone https://github.com/Maleehak/BriefMe.git
   ```
6. Create a Google Service account using [this](https://cloud.google.com/iam/docs/service-accounts-create) guide and grant it the following permissions to use Google Vertex AI or use official g [Speech to text](https://cloud.google.com/speech-to-text/docs/transcribe-client-libraries#client-libraries-usage-java) and [Vertex AI](https://cloud.google.com/vertex-ai/docs/start/cloud-environment) to learn how to create and use local credentials:
   1. Vertex AI user
   2. Vertex AI Admin
   3. Editor
8. Enable [Google Speech to Text](https://cloud.google.com/speech-to-text?hl=en) for Speech to text conversion and [Vertex AI](https://cloud.google.com/vertex-ai/docs) for summary generation
9. Run the below command to start up the backend application:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
   
#### Frontend
1. Install NPM packages
   ```sh
   npm install
   ```
 2. Point the application to local backend server to call the APIs  

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

