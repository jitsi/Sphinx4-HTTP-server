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
//        File[] testFiles = TestFiles.TEST_FILE_CHUNKS;
//        File[] filesToMerge = new File[testFiles.length];
//        for(int i = 0; i < testFiles.length; i++)
//        {
//            File file = testFiles[i];
//            File newFile = new File(FileManager.getInstance().
//                    getNewConvertedFilePath(".wav"));
//            FileInputStream in = new FileInputStream(file);
//            FileOutputStream out = new FileOutputStream(newFile);
//            while(in.available() > 0)
//            {
//                out.write(in.read());
//            }
//            filesToMerge[i] = newFile;
//        }
//
//        String filePath = FileManager.getInstance().
//                getNewConvertedFilePath("_merged.wav");
//        File merge = AudioFileManipulator.mergeWAVFiles(filePath, filesToMerge);
//
//        //delete test files
//        merge.delete();
//        for (File file: filesToMerge)
//        {
//            file.delete();
//        }
    }
}
