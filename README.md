# Moosic
A lightweight [Java + Spring](https://spring.io/) HTTP audio streaming application that allows you to play from a single source but queue from multiple.
Hosted via Localhost, users connect to the embedded HTTP server at [http://localhost:8080](http://localhost:8080) and are presented with a webpage for queueing tracks from a number of HTTP sources and a dashboard for monitoring/control. 

### Installation

#### Prerequisites
- Windows 10/11 [Untested on Linux/MacOS]
- [Java 16](https://jdk.java.net/16/)

#### Instructions
1) Download JAR file from this repository.
2) Open terminal in the directory the JAR is stored and run <code>java -jar Moosic.jar</code>
3) Moosic should automatically open a browser window once the application has launched.

### Support
[LavaPlayer](https://github.com/sedmelluq/lavaplayer) the audio player library Moosic is based upon supports a number of HTTP sources, there are the ones that have been enabled:
- YouTube
- SoundCloud
- Bandcamp
- Vimeo
- Twitch streams

Most HTTP audio source are also supported but your mileage may vary.

### Display
Here are some screenshots of how Moosic currently looks, I will try to keep these screenshots accurate to the current state of the project.

![landing](resources/landing.png)
![dashboard](resources/dashboard.png)



