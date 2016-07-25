import server.AudioFileManipulator;
import server.AudioTranscriber;
import util.FileManager;
import util.TimeStrings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Tests whether the transcribing works
 */
public class TranscribingTest
{
    public static void main(String[] args) throws Exception
    {
//        AudioTranscriber transcriber = new AudioTranscriber();
//        String result = "";
//        for(File file : TestFiles.TEST_FILE_CHUNKS)
//        {
//            String innerResult = transcriber.transcribe(file);
//            result += innerResult + "\n";
//        }
//
//        //mimic server
//
//        ArrayList<String> results = new ArrayList<>();
//
//        //place the files in the /converted folder
//        File[] chunks = new File[TestFiles.TEST_FILE_CHUNKS.length];
//        int i = 0;
//        for(File file: TestFiles.TEST_FILE_CHUNKS)
//        {
//
//            File newFile = new File(FileManager.getInstance().
//                    getNewConvertedFilePath(".wav"));
//            FileInputStream in = new FileInputStream(file);
//            FileOutputStream out = new FileOutputStream(newFile);
//            while(in.available() > 0)
//            {
//                out.write(in.read());
//            }
//            chunks[i] = newFile;
//            i++;
//        }
//
//        File chunk1 = chunks[0];
//        File chunk2 = chunks[1];
//        File chunk3 = chunks[2];
//        File chunk4 = chunks[3];
//        File chunk5 = chunks[4];
//
//        //first chunk 1 gets given
//        results.add(transcriber.transcribe(chunk1));
//
//        //then chunk 2
//        File mergedChunk1and2 = AudioFileManipulator.
//                mergeWAVFiles("src/test/resources/trash/" +
//                                TimeStrings.getNowString() + ".wav",
//                        chunk1,
//                        chunk2);
//        results.add(transcriber.transcribe(mergedChunk1and2));
//
//        //chunk 3
//        File mergedChunk2and3 = AudioFileManipulator.
//                mergeWAVFiles("src/test/resources/trash/" +
//                                TimeStrings.getNowString() + ".wav",
//                        chunk2,
//                        chunk3);
//        results.add(transcriber.transcribe(mergedChunk2and3));
//
//        //chunk 4
//        File mergedChunk3and4 = AudioFileManipulator.
//                mergeWAVFiles("src/test/resources/trash/" +
//                                TimeStrings.getNowString() + ".wav",
//                        chunk3,
//                        chunk4);
//        results.add(transcriber.transcribe(mergedChunk3and4));
//
//        //chunk 5
//        File mergedChunk4and5 = AudioFileManipulator.
//                mergeWAVFiles("src/test/resources/trash/" +
//                                TimeStrings.getNowString() + ".wav",
//                        chunk4,
//                        chunk5);
//        results.add(transcriber.transcribe(mergedChunk4and5));
//
//
//        //print result
//        System.out.println("###########################");
//        System.out.println(result);
//        System.out.println("###########################");
//
//        System.out.println("\n\n##################\nmerged results:\n");
//        for(String s : results)
//        {
//            System.out.println(s);
//        }

    }
}
