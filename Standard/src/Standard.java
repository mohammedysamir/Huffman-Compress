import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Standard {
    private HashMap<String, Tag> map = new HashMap<String, Tag>();
    private String OriginalFile = "";
    private String CompressFile = "";
    private String DecompressFile = "";
    private String content = "";

    public void Compress() throws IOException {
        // clear used variables
        map.clear();
        content = "";

        Path p = Paths.get(OriginalFile);
        content = ReadFromFile(p);

        //calculate probability
        CalcProb();


    }

    public void Decompress() throws IOException {
        String Result = "";
        Path input = Paths.get(CompressFile);
        String[] Text = ReadFromFile(input).split(" "); //split binaryvalues seperated by spaces to array
        for (int i = 0; i < Text.length; i++) {
            String bv = Text[i]; // set binary value from Text
            for (String c : map.keySet()) {
                if (map.get(c).getBinaryvalue() == bv)
                    Result += c;
                return;
            }
        }
        FileWriter outputFile=new FileWriter(DecompressFile);
        outputFile.close();
    }

    private void CalcProb() {
        // filling maps with char and probabilites
        for (int i = 0; i < content.length(); i++) {
            String c = Character.toString(content.charAt(i));
            if (map.containsKey(c)) {
                int occ = map.get(c).setNo_occurrance(map.get(c).getNo_occurrance() + 1); // increase # of occurrence
                Tag old = new Tag(map.get(c).getProbability(), map.get(c).getNo_occurrance(), c); // old tag
                // new tag update probability and no_occ
                Tag newTag = new Tag(map.get(c).setProbability((double) occ / content.length()),
                        map.get(c).setNo_occurrance(occ), "");
                map.replace(c, old, newTag);// replace new values
            } else {
                // set new tag (prob , occ , binary)
                // prob = 1/total_length , occ=1,binary=""
                Tag newTag = new Tag((double) 1 / content.length(), 1, "");
                map.put(c, newTag);
            }
        }
    }

    private String ReadFromFile(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8).replace("\n", "").replace("\r", "");
        // remove carrige return and line feed
    }

    // Check if Original data == Decompressed data
    public boolean isEqual() throws IOException {
        String Original = ReadFromFile(Paths.get(OriginalFile));
        String ResultDecompress = ReadFromFile(Paths.get(DecompressFile));
        return Original.equals(ResultDecompress);
    }

    // calculate compress size
    public void CalcCompress() {
        int compressSize = 0;
        for (Tag i : map.values()) {
            compressSize += i.getProbability() * content.length() * i.getBinaryvalue().length();
        }
        System.out.println(compressSize);
    }
}
