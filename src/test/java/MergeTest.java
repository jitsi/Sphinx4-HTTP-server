import server.Ffmpeg;
import util.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * test the merging method of the program ffmpeg
 */
public class MergeTest
{
    public static void main(String[] args) throws Exception
    {
        File[] testFiles = new File[]
                {
                    new File("src/test/resources/merge/merge1.wav"),
                    new File("src/test/resources/merge/merge2.wav"),
                    new File("src/test/resources/merge/merge3.wav"),
                    new File("src/test/resources/merge/merge4.wav"),
                    new File("src/test/resources/merge/merge5.wav"),
                    new File("src/test/resources/merge/merge6.wav"),
                    new File("src/test/resources/merge/merge7.wav"),
                    new File("src/test/resources/merge/merge8.wav"),
                    new File("src/test/resources/merge/merge9.wav"),
                    new File("src/test/resources/merge/merge10.wav")
                };

        File[] filesToMerge = new File[10];
        for(int i = 0; i < testFiles.length; i++)
        {
            File file = testFiles[i];
            File newFile = new File(FileManager.getInstance().
                    getNewConvertedFilePath(".wav"));
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(newFile);
            while(in.available() > 0)
            {
                out.write(in.read());
            }
            filesToMerge[i] = newFile;
        }

        String filePath = FileManager.getInstance().
                getNewConvertedFilePath("_merged.wav");
        File mergedFile = Ffmpeg.mergeWAVFiles(filePath, filesToMerge);

        //delete test files
        mergedFile.delete();
        for (File file: filesToMerge)
        {
            file.delete();
        }
    }
}
