package huffman;

public class Node {
    char data;
    int weight;
    boolean flag;
    Node left, right;

    //Constructores
    public Node(char d, int w) {
        data = d;
        weight = w;
        flag = false;
        left = null;
        right = null;
    }
    
    public Node(int w) {
        weight = w;
        left = null;
        right = null;
    }

    //Métodos set
    public void setData(char data) {
        this.data = data;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public void setFlag(boolean flag){
        this.flag = flag;
    }
    
    public void setRight(Node right){
        this.right = right;
    }
    
    public void setLeft(Node left){
        this.left = left;
    }

    //Métodos get
    public char getData() {
        return data;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public boolean getFlag(){
        return flag;
    }
    
    public Node getRight(){
        return right;
    }
    
    public Node getLeft(){
        return left;
    }
}

