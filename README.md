# Sphinx4-HTTP-server

A simple HTTP server for the Sphinx4 speech-to-text java library. Requires the program FFMPEG with verison 1.1 or higher. The executable should be in /usr/bin. FFMPEG can be downloaded here: https://ffmpeg.org/download.html or get the latest version by:
```
$ git clone https://git.ffmpeg.org/ffmpeg.git ffmpeg
```

You can run the server by executing the following commands:
```
$ mvn compile
$ mvn exec:java -Dexec.mainClass="server.HttpServer"
```

Alternatively, run the scripts in the scripts folder:
```
$ ./scripts/compile.sh
$ ./scripts/run.sh <optional-port-number>
```
The default port is 8081. 

When sending a POST request, the url should tail "/recognize". The server accepts requests with a content type "audio/xxx". 

An example of a request would look like the following:
```
$ curl -X POST --data-binary @filename.webm -H "Content-Type: audio/webm" http://localhost:8081/recognize
```

After retrieving a request, the server will convert the given audio file to the right format for Sphinx4 before it will do speech recognition. It will then send back the hypothesis gotten from sphinx4 in the following json format:

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
Every word uttered has a timestamp relative to the start of the audio file. If the word is filler, e.g a sigh, the filler value will be true. The session-id is not very relevant because it's not used yet. 
