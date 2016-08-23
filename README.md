# Sphinx4-HTTP-server

A simple speech-to-text HTTP server. It used Jetty as the server component and Sphinx4 as the speech-to-text library.

## Requirements
This project is build using Maven. Maven can be installed using:
```
$ apt-get install maven
```
It also requires the program FFMPEG with verison 1.1 or higher. It expects FFMPEG to be located in /usr/bin, but this can be configured. 
FFMPEG can be downloaded at: https://ffmpeg.org/download.html 

It is also possible to get the latest version by:
```
$ git clone https://git.ffmpeg.org/ffmpeg.git ffmpeg
```
## Running the server

You can run the server by executing the following commands in the root folder:
```
$ mvn compile
$ mvn exec:java -Dexec.mainClass="server.HttpServer"
```

Alternatively, run the scripts in the scripts folder:
```
$ ./scripts/compile.sh
$ ./scripts/run.sh <optional-port-number>
```
## Configuration

It is possible to configure the server in src/main/resources/sphinx4http.properties.

The config file has the following values:
```
# The port the server should use. 
port=8081

# The absolute path to a ffmpeg executable
ffmpeg_path=/usr/bin/ffmpeg

# The absolute path to the directory to write files to
#data_folder_path=/edit/to/an/absolute/path/

# To make responses be chunked
chunked_response=true
```
The ```port``` value will be overwritten when it's specified in the running script

Leave ```data_folder_path``` commented out unless the default behaviour is not desired.

If ```chunked_response``` is set to true, the server will respond to requests using a chunked HTTP response format, preventing time outs

## Usage

The server will listen for POST requests on the specified port. The post request should include the audio file which has to be trabscribed. When sending a POST request, the url of the server should tail "/recognize". The server accepts requests with a content type "audio/xxx". 

An example of a request would look like the following:
```
$ curl -X POST --data-binary @filename.webm -H "Content-Type: audio/webm" http://localhost:8081/recognize
```
After retrieving a request, the server will convert the given audio file to the right format for Sphinx4 before it will do speech recognition. It will then send back the transcription gotten from sphinx4 in JSON. The structue of the JSON is dependend on the configurution. 

A non-chunked reply will look like the following:
```
  {
  
    "session-id":"TpOeSN0gVM00OFHnSCHol9ESpaWNN5aF",
    "result":[
        {
            "word":"hello",
            "start":0,
            "end":390,
            "filler":false
        },
        {
            "word":"<sil>",
            "start":400,
            "end":420,
            "filler":true
        },
        {
            "word":"world",
            "start":430,
            "end":830,
            "filler":false
        }
    ]

}
```

If the server is set to use a chunked reply, however, it will look like this:
```
  {
    "objecets":[
        {
            "session-id":"TpOeSN0gVM00OFHnSCHol9ESpaWNN5aF",
        },
        {
            "word":"hello",
            "start":0,
            "end":390,
            "filler":false
        },
        {
            "word":"<sil>",
            "start":400,
            "end":420,
            "filler":true
        },
        {
            "word":"world",
            "start":430,
            "end":830,
            "filler":false
        }
    ]
}
```
The JSON array will hold objects with every word uttered in the given audio file. It will also include a timestamp relative to the start of the audio file. If the word is filler, e.g a sigh, the filler value will be true. The session-id is can be used to give multiple audio files back to back for more specifig use, but this feature is not yet implemented.
