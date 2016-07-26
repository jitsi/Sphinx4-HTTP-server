package server;

import com.sun.org.apache.xpath.internal.SourceTree;
import exceptions.InvalidDirectoryException;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import exceptions.OperationFailedException;
import util.FileManager;
import util.SessionManager;
import util.json.JSONObject;
import util.json.JSONPair;;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

/**
 * This class does most of the work. It accepts incoming HTTP requests,
 * see if their content is correct, converts the given audio file to the
 * correct format, predicts the speech in said audio file, and sends the text
 * back to the requester
 */
public class RequestHandler extends AbstractHandler
{
    /**
     * Keyword in the http url specifying that an audio transcription
     * is being requested
     */
    private static final String ACCEPTED_TARGET = "/recognize";

    /**
     * Keyword in the http url specifying the session id
     */
    private static final String SESSION_PARAMETER = "session-id";

    /**
     * Characters splitting different keywords in the HTPP url;
     */
    private static final char[] URL_SEPARATORS = {'/', '?'};

    /**
     * Name of the json value holding the session id
     */
    private static final String JSON_SESSION_ID = "session-id";

    /**
     *  name of tje json value holding the speech-to-text result
     */
    private static final String JSON_RESULT = "result";

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
     * The class managing the sessions for every request
     */
    private SessionManager sessionManager;

    /**
     * Creates an object being able to handle incoming HTTP POST requests
     * with audio files wanting to be transcribed
     * @throws IOException when the creation of the AudioTranscriber goes wrong
     */
    public RequestHandler() throws IOException
    {
        fileManager = FileManager.getInstance();
        transcriber = new AudioTranscriber();
        sessionManager = new SessionManager();
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
        if (!target.startsWith(ACCEPTED_TARGET))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("URL needs to tail " + ACCEPTED_TARGET);
            return;
        }
        //check if request method is POST
        if (!HttpMethod.POST.asString().equals(baseRequest.getMethod()))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("HTTP request should be POST" +
                    "and include an audio file");
            return;
        }
        //check if content type is an audio file
        if(!baseRequest.getContentType().contains("audio/"))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
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
        System.out.println("session-id" +
                request.getParameter(SESSION_PARAMETER));

        //extract file
        File audioFile;
        try
        {
            audioFile= writeAudioFile(request.getInputStream(),
                    baseRequest.getContentType());
        }
        catch (IOException | InvalidDirectoryException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().println("Failed to execute request due to " +
                    "failure in writing the audio file");
            return;
        }

        //convert file to .wav
        File convertedFile;
        try
        {
            convertedFile = AudioFileManipulator.convertToWAV(audioFile,
                    fileManager.getNewFile(FileManager.CONVERTED_DIR, ".wav")
            .getAbsolutePath());
            //delete the original audio file immediately as it's not needed
            fileManager.disposeFiles(audioFile);
        }
        catch (OperationFailedException | InvalidDirectoryException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().println("Failed to execute request due to " +
                    "failure in converting the audio file");
            fileManager.disposeFiles(audioFile);
            return;
        }

        //check for session
        Session session;
        String sessionID;
        if((sessionID = request.getParameter(SESSION_PARAMETER)) == null)
        {
            //make a new session
            session = sessionManager.createNewSession();
            System.out.println("Created new session with id: "
                    + session.getId());
        }
        else
        {
            System.out.println("handling session with id: " + sessionID);
            session = sessionManager.getSession(sessionID);
            if(session == null)
            {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/plain");
                response.getWriter().println("invalid session id");
                fileManager.disposeFiles(audioFile);
                return;
            }
        }

        //get the speech-to-text
        JSONObject speechToTextResult;
        try
        {
            System.out.println("Started audio transcription");
            speechToTextResult = session.transcribe(convertedFile);
        }
        catch (IOException | OperationFailedException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().println("Failed to execute request due to" +
                    "an error in transcribing the audio file");
            return;
        }

        //create the returning json result with the session id
        JSONObject result = new JSONObject();
        result.addPair(new JSONPair(JSON_SESSION_ID, session.getId()));
        result.addPair(new JSONPair(JSON_RESULT, speechToTextResult));

        //return the json result
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
        baseRequest.setHandled(true);
        System.out.println("handled the request");
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
            throws IOException, InvalidDirectoryException
    {
        try
        {
            String content = contentType.split("/")[0];
            File audioFile = fileManager.getNewFile(FileManager.INCOMING_DIR,
                    content);
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
        catch (IOException | InvalidDirectoryException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

}
