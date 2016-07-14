package server;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import util.TimeStrings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by nik on 13/07/16.
 */
public class RequestHandler extends AbstractHandler
{
    private static final String ACCEPTED_TARGET = "/recognize";
    private static final String DIRECTORY = "data/";

    private FileManager fileManager;
    private AudioTranscriber transcriber;

    public RequestHandler()
    {
        fileManager = FileManager.getInstance();
        transcriber = new AudioTranscriber();
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        //check if the address was "http://<ip>:<port>/recognize"
        if (!ACCEPTED_TARGET.equals(target))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("URL needs to tail " + ACCEPTED_TARGET);
            return;
        }
        //check if request method is POST
        if (!HttpMethod.POST.asString().equals(baseRequest.getMethod()))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("HTTP request should be POST" +
                    "and include an audio file");
            return;
        }
        //check if content type is an audio file
        if(!baseRequest.getContentType().contains("audio/"))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("HTTP request should have content type" +
                    " \"audio/xxxx\"");
        }
        //log
        System.out.println("New incoming message on port 8081");
        System.out.println(target);
        System.out.println(baseRequest);
        System.out.println(request.toString());
        System.out.println(response);
        System.out.println("Content type of request:" +
                baseRequest.getContentType());

        //extract file
        File audioFile = writeAudioFile(request.getInputStream(),
                baseRequest.getContentType());

        //convert file to .wav
        File convertedFile;
        try
        {
            convertedFile = AudioConverter.convertToWAV(audioFile,
                    fileManager.getNewConvertedFilePath(".wav"));
        }
        catch (InterruptedException | ConversionFailedException | IOException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to execute request due to " +
                    "failure in converting the audio file");
            return;
        }

        //get the transcript
        try
        {
            String hypothesis = transcriber.transcribeAudioFile(convertedFile);
            System.out.println(hypothesis);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(hypothesis);
            baseRequest.setHandled(true);
        }
        catch (IOException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to execute request due to" +
                    "an error in transcribing the audio file");
        }

    }

    private File writeAudioFile(InputStream stream, String contentType)
            throws IOException
    {
        try
        {
            File audioFile = fileManager.getNewIncomingFile(contentType);
            FileOutputStream outputStream = new FileOutputStream(audioFile);
            int bit;
            while( (bit = stream.read()) != -1 )
            {
                outputStream.write(bit);
            }
            stream.close();
            outputStream.close();
            return audioFile;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}
