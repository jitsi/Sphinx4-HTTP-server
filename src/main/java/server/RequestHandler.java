package server;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import exceptions.OperationFailedException;
import util.FileManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * This class does most of the work. It accepts incoming HTTP requests,
 * see if their content is correct, converts the given audio file to the
 * correct format, predicts the speech in said audio file, and sends the text
 * back to the requester
 */
public class RequestHandler extends AbstractHandler
{
    /**
     * The required trailing string in the url
     */
    private static final String ACCEPTED_TARGET = "/recognize";

    /**
     * The class managing creation of directories and names for the audio files
     * which are needed for handling the speech-to-text service
     */
    private FileManager fileManager;

    /**
     * The class implementing the Sphinx4 speech-to-text library
     */
    private AudioTranscriber transcriber;

    /**
     * Creates an object being able to handle incoming HTTP POST requests
     * with audio files wanting to be transcribed
     * @throws IOException when the creation of the AudioTranscriber goes wrong
     */
    public RequestHandler() throws IOException
    {
        fileManager = FileManager.getInstance();
        transcriber = new AudioTranscriber();
    }

    /**
     * Handles incoming HTTP post requests. Checks for validity of the request
     * by checking if has the right url, if it's a POST request and if it
     * has an audio file as content type
     *
     * It than stores the audio file to disk, converts it and transcribed the
     * audio before sending the text string back
     *
     * @param target the target of the request - should be equal to
     *               ACCEPTED_TARGET
     * @param baseRequest the original unwrapped requets object
     * @param request The request either as the Request object or a wrapper of
     *                that request.
     * @param response The response as the Response object or a wrapper of
     *                 that request.
     *
     * @throws IOException when writing to the response object goes wrong
     */
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException
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
        File audioFile;
        try
        {
            audioFile= writeAudioFile(request.getInputStream(),
                    baseRequest.getContentType());
        }
        catch (IOException e)
        {

            return;
        }

        //convert file to .wav
        File convertedFile;
        try
        {
            convertedFile = AudioFileManipulator.convertToWAV(audioFile,
                    fileManager.getNewConvertedFilePath(".wav"));
        }
        catch (OperationFailedException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to execute request due to " +
                    "failure in converting the audio file");
            fileManager.disposeFiles(audioFile);
            return;
        }

        //get the transcript
        try
        {
            String hypothesis = transcriber.transcribeAudioFile(convertedFile);
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

        //clean up the created audio files
        fileManager.disposeFiles(audioFile, convertedFile);
    }

    /**
     * Writes the audio file given in the HTTP request to file
     * @param stream the InputStream of the audio file object
     * @param contentType the content type of the HTTP request. Needed to give
     *                    the written file the correct file extension
     * @return The file object if the audio file in hte HTTP request,
     * written to disk
     * @throws IOException when reading from the InputStream goes wrong
     */
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
