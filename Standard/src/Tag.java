public class Tag {
    // this class stores probability of char and its binary value
    private double probability;
    private int no_occurrance;
    private String Binaryvalue;

    Tag() {
        probability = 0.0;
        Binaryvalue = "";
        no_occurrance = 0;
    }

    Tag(double prob, int nO, String BV) {
        Binaryvalue = BV;
        probability = prob;
        no_occurrance = nO;
    }

    public String setBinaryvalue(String binaryvalue) {
        Binaryvalue = binaryvalue;
        return Binaryvalue;
    }

    public double setProbability(double probability) {
        this.probability = probability;
        return probability;
    }

    public int setNo_occurrance(int no_occurrance) {
        this.no_occurrance = no_occurrance;
        return no_occurrance;
    }

    public int getNo_occurrance() {
        return no_occurrance;
    }

    public String getBinaryvalue() {
        return Binaryvalue;
    }

    public double getProbability() {
        return probability;
    }
}
