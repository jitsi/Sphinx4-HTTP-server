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

import org.jitsi.sphinx4http.exceptions.ServerConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Class handling the configuration file sphinx4http.properties
 * It currently supports 3 configurable options for the server:
 * 1. which port to use
 * 2. where the executable for ffmpeg is located in the system
 * 3. folder wherein the server stores and processes the incoming files
 */
public class ServerConfiguration
{
    /**
     * Identifier for the port in the config file
     */
    public final static String PORT_PROPERTY = "port";

    /**
     * Identifier for the ffmpeg path in the config file
     */
    public final static String FFMPEG_PATH_PROPERTY = "ffmpeg_path";

    /**
     * Identifier for the data folder path in the config file
     */
    public final static String DATA_FOLDER_PATH_PROPERTY = "data_folder_path";

    /**
     * Identifier for the returning method of the server in the config file
     */
    public final static String CHUNKED_HTTP_RESPONSE = "CHUCKED_RESPONSE";

    /**
     * Name of the config file
     */
    private static final String CONFIG_FILE_NAME = "sphinx4http.properties";

    /**
     * Path to the config file
     */
    private static final String CONFIG_FILE_PATH =
            "src/main/resources/" + CONFIG_FILE_NAME;

    /**
     * Logger for this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ServerConfiguration.class);

    /**
     * Default value for the port
     */
    private int port = 8081;

    /**
     * Default value for the ffmpeg path
     */
    private String ffmpegPath = "/usr/bin/ffmpeg";

    /**
     * Default value for the data folder path
     */
    private String dataFolderPath = "data/";

    /**
     * Default value for the chunked response returning policy
     */
    private boolean chunkedResponse = false;

    /**
     * Singleton instance of the configuration class
     */
    private static ServerConfiguration config = new ServerConfiguration();

    /**
     * Stores the properties found in the file
     */
    private Properties properties;


    /**
     * Construct an instance of the configuration.
     */
    private ServerConfiguration()
    {
        //get the config file
        File configFile = findConfigFile();
        if(configFile == null)
        {
            logger.warn("Could not find {}", CONFIG_FILE_NAME);
            return;
        }

        //load the Properties class
        this.properties = new Properties();
        try (FileInputStream in = new FileInputStream(configFile))
        {
            properties.load(in);
        }
        catch (IOException e)
        {
            logger.info("Couldn't load {}", CONFIG_FILE_NAME);
        }

        //load properties
        loadProperties();
    }

    /**
     * Find the config file with name "sphinx4http.properties". It will first
     * try to use the getResource() method and if that fales,
     * try the location /src/main/resources/sphinx4http.properties"
     * @return the file containing the configuration, or null if not found
     */
    private File findConfigFile()
    {
        //first try resources
        URL url = getClass().getResource(CONFIG_FILE_NAME);

        //if url is null, try the absolute path in the /src
        if(url == null)
        {
            File file = new File(CONFIG_FILE_PATH);
            if(file.exists())
            {
                return file;
            }
        }
        else
        {
            try
            {
                return Paths.get(url.toURI()).toFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //if it cannot be found, return null
        return null;
    }

    /**
     * Return the singleton object of this configuration class
     * @return the configuration instance
     */
    public static ServerConfiguration getInstance()
    {
        return config;
    }

    /**
     * Get the port the server should run on
     * @return the port as an integer, or null if not specified
     */
    public int getPort()
            throws ServerConfigurationException
    {
        //integers default to 0
        if(port == 0)
        {
            throw new ServerConfigurationException(PORT_PROPERTY + "" +
                    "was not specified in the config file");
        }
        return this.port;
    }

    /**
     * Get the path to an ffmpeg executable
     * @return the path to an ffmpeg executable
     */
    public String getFfmpegPath()
            throws ServerConfigurationException
    {
        return verifyNotNull(this.ffmpegPath, String.class,
                FFMPEG_PATH_PROPERTY);
    }

    /**
     * Get the path to where the data folder should be created
     * @return the path to where the data folder should be created
     */
    public String getDataFolderPath()
            throws  ServerConfigurationException
    {
        return verifyNotNull(this.dataFolderPath, String.class,
                DATA_FOLDER_PATH_PROPERTY);
    }

    public boolean isChunkedResponse()
    {
        return this.chunkedResponse;
    }

    /**
     * Load the properties read by the Properties class into instance
     * variables
     */
    private void loadProperties()
    {
        for(String property: properties.stringPropertyNames())
        {
            switch(property)
            {
                case PORT_PROPERTY:
                    this.port = getInteger(this.port,
                            properties.get(PORT_PROPERTY));
                    break;
                case FFMPEG_PATH_PROPERTY:
                    this.ffmpegPath = getString(this.ffmpegPath,
                            properties.get(FFMPEG_PATH_PROPERTY));
                    break;
                case DATA_FOLDER_PATH_PROPERTY:
                    this.dataFolderPath = getString(dataFolderPath,
                            properties.get(DATA_FOLDER_PATH_PROPERTY));
                    break;
                case CHUNKED_HTTP_RESPONSE:
                    this.chunkedResponse = getBoolean(chunkedResponse,
                            CHUNKED_HTTP_RESPONSE);
                default:
                    logger.warn("property {} in config is not a valid " +
                            "configuration setting", property);
            }
        }
    }

    /**
     * Cast an object given by the Properties class into an integer
     * @param original the default value of the setting for
     *                 if the integer cannot be converted
     * @param integerToConvert the Object to cast into an integer
     */
    private int getInteger(int original, Object integerToConvert)
    {
        try
        {
            return Integer.parseInt((String) integerToConvert);
        }
        catch ( NumberFormatException | ClassCastException |
                NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid " +
                    "integer value", PORT_PROPERTY, e);
            return original;
        }
    }

    /**
     * Cast an object given by the Properties class into a String
     * @param original The defautl value of the setting for if the String
     *                 cannot be converted
     * @param stringToConvert the Object to cast into a String
     */
    private String getString(String original, Object stringToConvert)
    {
        try
        {
            return (String) stringToConvert;
        }
        catch (ClassCastException | NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid" +
                    "String value", FFMPEG_PATH_PROPERTY, e);
            return original;
        }
    }

    /**
     * Cast an object given by the Properties class into a boolean
     * @param original The defautl value of the setting for if the boolean
     *                 cannot be converted
     * @param booleanToConvert the Object to cast into a boolean
     */
    private boolean getBoolean(boolean original, Object booleanToConvert)
    {
        try
        {
            return (boolean) booleanToConvert;
        }
        catch (ClassCastException | NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid" +
                    "String value", FFMPEG_PATH_PROPERTY, e);
            return original;
        }
    }

    /**
     * generic method to verify that a requested configuration setting
     * was actually set. If it wasn't set, and thus is null, it will throw
     * an error
     * @param o the object to verify
     * @param c the class belonging to the object being verified
     * @param name the name of the object in the cofiguration
     * @param <T> the type of the object being verified
     * @return the verified object, which will not be null
     * @throws ServerConfigurationException if the passed object is null
     */
    private <T> T verifyNotNull(Object o, Class<T> c, String name)
            throws ServerConfigurationException
    {
        if(o == null)
        {
            throw new ServerConfigurationException(name + " was not " +
                    "specified in the config file");
        }
        else
        {
            return c.cast(o);
        }
    }

}
