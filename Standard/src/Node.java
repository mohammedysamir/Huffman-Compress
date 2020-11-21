public class Node{
    //Tag Attributes
    public String Symbol,BinaryValue;
    public double probability;
    private double no_occ;
    //Tree Pointers
    public Node Right,Left,Parent;

    Node(){
        Symbol="";
        BinaryValue="";
        probability=0.0;
        no_occ=0.0;
        Right=Left=Parent=null;
    }
    
    //increase number of occ and update probability
    public void UpdateProbability(int length){
        no_occ++;
        Double len=Double.valueOf(length);
        probability=no_occ/len;
    }
}