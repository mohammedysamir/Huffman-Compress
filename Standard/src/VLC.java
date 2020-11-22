import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VLC {

    public void Compress() throws IOException {
        // reset used varaible
        tags.clear();
        content = "";
        Binary="";
        
        FileWriter out = new FileWriter(CompressFile);
        String Compressed = "";

        Path input = Paths.get(OriginalFile);
        content = ReadFromFile(input);
        // calculate probability
        CalcProb(content);
        // call tree function to assign binary values   
        if(tags.size()==1 &&tags.get(0).Symbol=="") 
            BuiltTree();
        else tags.get(0).BinaryValue="0"; //one repeated char
        // read from file to compress
        for (int i = 0; i < content.length(); i++) {
            String ch = Character.toString(content.charAt(i));
                TraverseSymbol(tags.get(0),ch); //return value to Binary object
                Compressed +=Binary+ " ";
        }
        out.write(Compressed);
        out.close();
    }

    public void Decompress() throws IOException {
        Symbol="";
        Path p = Paths.get(CompressFile);
        String Result = "";
        String[] Text = ReadFromFile(p).split(" ");
        for (int i = 0; i < Text.length; i++) {
            TraverseBinary(tags.get(0),Text[i]);
            Result+=Symbol;
        }
        FileWriter outputFile = new FileWriter(DecompressFile);
        outputFile.write(Result);
        outputFile.close();
    }

    // Check if Original data == Decompressed data
    public boolean isEqual() throws IOException {
        String Original = ReadFromFile(Paths.get(OriginalFile));
        String ResultDecompress = ReadFromFile(Paths.get(DecompressFile));
        return Original.equals(ResultDecompress);
    }

    private String ReadFromFile(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8).replace("\n", "").replace("\r", "");
        // remove carrige return and line feed
    }

    private void BuiltTree() {
        // ArrayList<Node> nodes=tags; //works as temp ArrayList
        while (tags.size() > 1) { // group nodes until we have just one node(Root)
            // 1. sort
            SortArr(tags);
            // 2. Create parent node
            Node parent = new Node();
            // 3. sum children probability
            parent.probability = tags.get(0).probability + tags.get(1).probability;
            // 4. fix pointers
            parent.Right = tags.get(0);
            parent.Left = tags.get(1);
            tags.get(0).Parent = parent;
            tags.get(1).Parent = parent;
            // 5. remove least 2 nodes and add parent to array
            tags.remove(0);
            tags.remove(0);
            tags.add(parent);
        }
        Preorder(tags.get(0));
    }

    // used to calculate probability and fill tags array
    private void CalcProb(String text) {
        for (int i = 0; i < text.length(); i++) {
            String ch = Character.toString(text.charAt(i));
            boolean isFound = false;
            for (int j = 0; j < tags.size(); j++) {
                if (tags.get(j).Symbol.equalsIgnoreCase(ch)) {
                    // Symbol already in a tag
                    // update probability
                    tags.get(j).UpdateProbability(content.length());
                    isFound = true;
                    break;
                }
            }
            if (!isFound || tags.isEmpty()) {
                // first time to appear
                Node new_tag = new Node();
                new_tag.Symbol = ch;
                new_tag.UpdateProbability(content.length());
                tags.add(new_tag);
            }
        }
    }

    // sort arraylist by probability asc
    // Tested and worked fine
    private void SortArr(ArrayList<Node> t) {
        for (int i = 0; i < t.size(); i++) {
            for (int j = i + 1; j < t.size(); j++) {
                if (t.get(j).probability < t.get(i).probability) {
                    // swap
                    Node temp = t.get(j); // temp=j
                    t.set(j, t.get(i)); // j=i
                    t.set(i, temp); // i=temp
                }
            }
        }
    }

    // preorder traversal to assign values
    private void Preorder(Node root) {
        if (root == null)// end of the tree
            return;
        if (root.Parent == null)// root binaryvalue empty
            root.BinaryValue = "";
        else {
            if (root == root.Parent.Left)// is left child
                root.BinaryValue = root.Parent.BinaryValue + "0";
            else // right child
                root.BinaryValue = root.Parent.BinaryValue + "1";
           // System.out.println("Node Symbol: " + root.Symbol + " BinaryValue " + root.BinaryValue);
        }
        Preorder(root.Left); // traverse left
        Preorder(root.Right); // traverse right
    }

    // used to get binaryValue of specific Symbol
    private void TraverseSymbol(Node node, String value) {
        if (node == null)
            return ;
        if (node.Symbol.equalsIgnoreCase(value))
            {Binary= node.BinaryValue;return;}
        TraverseSymbol(node.Left, value);
        TraverseSymbol(node.Right, value);
    }

    // used to get  Symbol of specific binaryValue
    private void TraverseBinary(Node node, String value) {
        if (node == null)
            return ;
        if (node.BinaryValue.equalsIgnoreCase(value))
            {Symbol= node.Symbol;return;}
        TraverseBinary(node.Left, value);
        TraverseBinary(node.Right, value);
    }

    private String OriginalFile = "C:/Users/mohammed/Desktop/Multimedia/Huffman/Standard/Standard/src/OriginalFile.txt";
    private String CompressFile = "C:/Users/mohammed/Desktop/Multimedia/Huffman/Standard/Standard/src/CompressFile.txt";
    private String DecompressFile = "C:/Users/mohammed/Desktop/Multimedia/Huffman/Standard/Standard/src/DecompressFile.txt";
    private String content = "";
    ArrayList<Node> tags = new ArrayList<Node>(); // to store nodes
    private String Binary=""; //used with Traverse function to avoid returning null 
    private String Symbol="";//used with Traverse function to avoid returning null 
}
