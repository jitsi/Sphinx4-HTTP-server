# Sphinx4-HTTP-server

A simple HTTP server for the Sphinx4 speech-to-text java library. Requires the program avconv in /usr/bin/ to convert audio files

you can run the server by executing the following commands:
```
$ mvn compile
$ mvn exec:java -Dexec.mainClass="server.HttpServer"
```

Default port is 8081. The ip should trail "/recognize". It accepts HTTP POST requests with a content type "audio/x". 

An example of a request would look like the following:
```
curl -X POST --data-binary @filename.webm -H "Content-Type: audio/webm" http://localhost:8081/recognize
```

After retrieving a request, the server will convert the given audio file to the right format for Sphinx4 before it will do speech recognition. It will then send back the hypothesis gotten from sphinx4.
