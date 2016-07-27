import server.AudioFileManipulator;
import util.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * test the merging method of the program ffmpeg
 */
public class MergeTest
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            File[] testFiles = TestFiles.TEST_FILE_CHUNKS;
            File[] filesToMerge = new File[testFiles.length];
            for (int i = 0; i < testFiles.length; i++)
            {
                File file = testFiles[i];
                File newFile = new File(
                        FileManager.getInstance()
                        .getNewFile(FileManager.CONVERTED_DIR, ".wav")
                        .toString());

                FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(newFile);
                while (in.available() > 0)
                {
                    out.write(in.read());
                }
                filesToMerge[i] = newFile;
            }

            File merged = AudioFileManipulator.mergeWAVFiles(filesToMerge);

            if(!merged.exists())
            {
                throw new Exception();
            }
            //delete test files
            merged.delete();
            for (File file : filesToMerge)
            {
                file.delete();
            }
            System.exit(0);
        }
        catch (Exception e)
        {
            throw new Exception("failure in MergeTest");
        }
    }
}
