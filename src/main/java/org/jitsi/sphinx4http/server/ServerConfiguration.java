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
import java.net.URISyntaxException;
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
     * Instance of the configuration class
     */
    private static ServerConfiguration config = new ServerConfiguration();

    /**
     * Stores the properties found in the file
     */
    private Properties properties;


    /**
     *
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

    public static ServerConfiguration getInstance()
    {
        return config;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getFfmpegPath()
    {
        return this.ffmpegPath;
    }

    public String getDataFolderPath()
    {
        return this.dataFolderPath;
    }

    private void loadProperties()
    {
        for(String property: properties.stringPropertyNames())
        {
            switch(property)
            {
                case PORT_PROPERTY:
                    loadPort(properties.get(PORT_PROPERTY));
                    break;
                case FFMPEG_PATH_PROPERTY:
                    loadFfmpegPath(properties.get(FFMPEG_PATH_PROPERTY));
                    break;
                case DATA_FOLDER_PATH_PROPERTY:
                    loadDataFolderPath(properties.
                            get(DATA_FOLDER_PATH_PROPERTY));
                    break;
                default:
                    logger.warn("property {} in config is not a valid " +
                            "configuration setting", property);
            }
        }
    }

    private void loadPort(Object integer)
    {
        try
        {
            this.port = Integer.parseInt((String) integer);
        }
        catch (ClassCastException | NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid " +
                    "integer value", PORT_PROPERTY, e);
        }
    }

    private void loadFfmpegPath(Object string)
    {
        try
        {
            this.ffmpegPath = (String) string;
        }
        catch (ClassCastException | NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid" +
                    "String value", FFMPEG_PATH_PROPERTY, e);
        }
    }

    private void loadDataFolderPath(Object string)
    {
        try
        {
            this.dataFolderPath = (String) string;
        }
        catch (ClassCastException | NullPointerException e)
        {
            logger.warn("Property {} in config file does not have a valid" +
                    "String value", DATA_FOLDER_PATH_PROPERTY, e);
        }
    }

}
