import server.AudioConverter;
import util.TimeStrings;

import java.io.File;

/**
 * Created by workingnik on 14/07/16.
 */
public class ConversionTest
{
    public static void main(String[] args) throws Exception
    {
        File file = new File("src/test/resources/test.webm");

        String path = file.getParentFile().getAbsolutePath() + "/" +
                TimeStrings.getNowString() + ".wav";
        File convertedFile = AudioConverter.convertToWAV(file, path);
        convertedFile.delete();
    }
}
