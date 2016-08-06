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

import org.jitsi.sphinx4http.server.ServerConfiguration;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 */
public class PropertiesTest
{

    private static final String path =
            "src/main/resources/sphinx4http.properties";

    @org.junit.Test
    public void testProperties() throws Exception
    {
        ServerConfiguration configuration = ServerConfiguration.getInstance();
//        Assert.assertNotNull(configuration.getPort());
//        Assert.assertNotNull(configuration.getFfmpegPath());
//        Assert.assertNotNull(configuration.getDataFolderPath());
        System.out.println(configuration.getPort());
        System.out.println(configuration.getFfmpegPath());
        System.out.println(configuration.getDataFolderPath());
    }
}
