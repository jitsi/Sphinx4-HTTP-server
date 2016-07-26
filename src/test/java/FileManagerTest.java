import server.AudioFileManipulator;
import util.FileManager;

import java.io.File;

/**
 *
 */
public class FileManagerTest
{
    public static void main(String[] args) throws Exception
    {
        FileManager manager = FileManager.getInstance();
        AudioFileManipulator.OUTPUT = false;

        File file = TestFiles.TEST_FILE;

        File convertedFile = AudioFileManipulator.convertToWAV(file,
                manager.getNewFile(FileManager.CONVERTED_DIR).toString() + ".wav");

        System.out.println("result: " + manager.getDirectory(convertedFile));
    }
}
