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

        File file = manager.getNewFile(FileManager.INCOMING_DIR, "test.wav");
        System.out.println(file);
    }
}
