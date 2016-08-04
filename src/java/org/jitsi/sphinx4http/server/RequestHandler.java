/*
 * Sphinx4 HTTP server
 *
 * Copyright @ 2016 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.sphinx4http.server;


import org.jitsi.sphinx4http.exceptions.*;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jitsi.sphinx4http.util.*;
import org.jitsi.sphinx4http.util.json.*;

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
     * The logger for this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(RequestHandler.class);

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
     * Name of the json value holding the session id
     */
    private static final String JSON_SESSION_ID = "session-id";

    /**
     *  name of the json value holding the speech-to-text result
     */
    private static final String JSON_RESULT = "result";

    /**
     * The class managing creation of directories and names for the audio files
     * which are needed for handling the speech-to-text service
     */
    private FileManager fileManager;

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
        //log the request
        //log
        logger.info("New incoming request:\n" +
                        "target: {}\n" +
                        "method: {}\n" +
                        "Content-Type: {}\n" +
                        "Content-Length: {}\n" +
                        "Time: {}\n" +
                        "session-id: {}\n",
                target, baseRequest.getMethod(), baseRequest.getContentType(),
                baseRequest.getContentLength(), baseRequest.getHeader("Date"),
                baseRequest.getParameter("session-id"));

        //check if the address was "http://<ip>:<port>/recognize"
        if (!target.startsWith(ACCEPTED_TARGET))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("URL needs to tail " + ACCEPTED_TARGET);
            baseRequest.setHandled(true);
            logger.info("denied request because target was " + target);
            return;
        }
        //check if request method is POST
        if (!HttpMethod.POST.asString().equals(baseRequest.getMethod()))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("HTTP request should be POST" +
                    "and include an audio file");
            baseRequest.setHandled(true);
            logger.info("denied request because METHOD was not post");
            return;
        }
        //check if content type is an audio file
        if(!baseRequest.getContentType().contains("audio/"))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("HTTP request should have content type" +
                    " \"audio/xxxx\"");
            baseRequest.setHandled(true);
            return;
        }

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
            baseRequest.setHandled(true);
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
            baseRequest.setHandled(true);
            return;
        }

        //check for session
        Session session;
        String sessionID;
        if((sessionID = request.getParameter(SESSION_PARAMETER)) == null)
        {
            //make a new session
            session = sessionManager.createNewSession();
            logger.info("Created new session with id: {} ", session.getId());
        }
        else
        {
            logger.info("handling session with id: {}");
            session = sessionManager.getSession(sessionID);
            if(session == null)
            {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/plain");
                response.getWriter().println("invalid session id");
                fileManager.disposeFiles(audioFile);
                baseRequest.setHandled(true);
                return;
            }
        }

        //get the speech-to-text
        JSONArray speechToTextResult;
        try
        {
            logger.info("Started audio transcription for id: {}",
                    session.getId());
            speechToTextResult = session.transcribe(convertedFile);
        }
        catch (IOException e)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().println("Failed to execute request due to" +
                    "an error in transcribing the audio file");
            baseRequest.setHandled(true);
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

        logger.info("Successfully handled request with id: {}",
                session.getId());
        logger.debug("Result of request with id {}:\n{}", session.getId(),
                result.toString());
    }

    /**
     * Writes the audio file given in the HTTP request to file
     * @param inputStream the InputStream of the audio file object
     * @param contentType the content type of the HTTP request. Needed to give
     *                    the written file the correct file extension
     * @return The file object if the audio file in hte HTTP request,
     * written to disk
     * @throws IOException when reading from the InputStream goes wrong
     */
    private File writeAudioFile(InputStream inputStream, String contentType)
            throws IOException, InvalidDirectoryException
    {
        String content = contentType.split("/")[0];
        File audioFile = fileManager.getNewFile(FileManager.INCOMING_DIR,
                content);
        try(FileOutputStream outputStream = new FileOutputStream(audioFile))
        {
            byte[] buffer = new byte[2048];
            while(inputStream.read(buffer) != -1)
            {
                outputStream.write(buffer);
            }
            inputStream.close();
            return audioFile;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

}
