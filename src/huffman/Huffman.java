package huffman;

import java.io.*;

/**
 * @author Juan Carlos Salazar Muñoz
 */
public class Huffman {
    
    static Node[] huffmanArray = new Node[0];
    static String[][] dictionary;
    static int count;

    public static void main(String[] args) throws IOException {
        String path = ("C:/Users/user/Desktop/Coco.txt");
        encrypt(path);
    }
    
    //Método para encriptar texto cuya ruta recibe como parámetro
    public static String[][] encrypt(String path) throws IOException{
        readFile(path);
        huffmanArray = order(huffmanArray);
        for (int i = 0; i < huffmanArray.length; i++) {
            System.out.print("[(" + huffmanArray[i].data + ")" + huffmanArray[i].weight + "]");
        }
        System.out.println("");
        dictionary = new String[2][huffmanArray.length];
        count = 0;
        dictionary = huffmanAlgorithm(huffmanArray, dictionary);
        System.out.println("");
        for (int i = 0; i < dictionary[0].length; i++) {
            System.out.print("[(" + dictionary[0][i] + ")" + dictionary[1][i] + "]");
        }
        System.out.println("");
        genBinary(path);
        return dictionary;
    }
    
    //Crea un árbol siguiendo el algoritmo de Huffman a partir de un vector de nodos
    public static String[][] huffmanAlgorithm(Node[] huffmanArray, String[][] dictionary) {
        while (huffmanArray.length > 1){//Lee todo el vector
            Node n = new Node(huffmanArray[0].weight + huffmanArray[1].weight);//Crea un nuevo nodo con la suma de los dos primeros
            n.setLeft(huffmanArray[0]);//Asigna nodo izquierdo del nuevo nodo
            n.setRight(huffmanArray[1]);//Asigna nodo derecho del nuevo nodo
            n.setData('¥'); //Identificado de los nuevos nodos (Solo visual)
            huffmanArray = adjust(huffmanArray, n); //Ajusta el vector
            for (int i = 0; i < huffmanArray.length; i++) {
                System.out.print("[(" + huffmanArray[i].data + ")" + huffmanArray[i].weight + "]");
            }
            System.out.println("");
        }
        String s = "";
        readInOrder(huffmanArray[0], s);
        return dictionary;
    }
    
    //Método recursivo para crear el diccionario leyendo in orden el árbol huffman
    public static void readInOrder(Node n, String s){        
        if (n != null) {   
            if(n.getData() != '¥'){
                System.out.print("[(" + n.getData() + ")" + s + "]");
                dictionary[0][count] = String.valueOf(n.getData());
                dictionary[1][count] = s;
                count++;
            }
            readInOrder(n.getLeft(), s = s + "0");
            s= s.substring(0, s.length() - 1);
            readInOrder(n.getRight(), s = s + "1");
            s= s.substring(0, s.length() - 1);
        }
    }
    
    //Ajusta el vector con un nuevo nodo
    public static Node[] adjust(Node[] huffmanArray, Node n){
        Node[] R = new Node[huffmanArray.length - 1]; //Crea nuevo vector con una posición menos del vector dado
        R[0] = n; //Copia en nuevo nodo en la primera posición de un nuevo vector
        for (int i = 2; i < huffmanArray.length; i++) {
            R[i - 1] = huffmanArray[i]; //Copia el resto de posiciones al nuevo vector
        }
        return order(R);
    } 
    
    //Lee archivo línea por línea
    public static void readFile(String file) throws FileNotFoundException, IOException{
        File archivo = null;
        FileReader fr = null;
        BufferedReader br;// = null;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (file);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea = br.readLine()) != null){
                System.out.println(linea);
                countCharacters(linea);
            }
        }
        catch(IOException e){
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
                if( null != fr ){   
                    fr.close();     
                }                  
            }catch (IOException e2){
            }
        }
    }
    
    //Cuenta los caracteres de un string dado y guarda el conteo en un vector de nodos
    public static void countCharacters(String linea) {
        for (int i = 0; i < linea.length(); i++) { //Recorre la línea de texto
            int iE = exists(linea.charAt(i), huffmanArray); //Verifica posición de letra leída
            if(iE != -1){
                int count = 1;
                for (int j = i + 1; j < linea.length(); j++) { //Cuenta la cantidad de caracteres restantes
                    if(linea.charAt(i) == linea.charAt(j)){
                        count++;
                    }
                }
                if(iE == -2){ //Si no existe
                    huffmanArray = addNode(huffmanArray, linea.charAt(i), count);//Crea un nuevo nodo
                }else{
                    huffmanArray[iE].setWeight(huffmanArray[iE].getWeight() + count); //Suma el contéo al conteo del nodo correspondiente a la letra
                    huffmanArray[iE].setFlag(true); //Marca el nodo como leído
                }
            }
        }
        blackFlag(huffmanArray); //Desmarca todos los nodos como leídos
    }
    
    //Valida si el nodo existe y ya fue leído
    public static int exists(char c, Node[] n){
        int response = -2; //Valor predeterminado por si no encuentra el nodo
        for (int i = 0; i < n.length; i++) {
            if(c == n[i].data && n[i].flag == true){ //Si encuentra el nodo ya leído
                response = -1;
            }
            else if(c == n[i].data && n[i].flag == false) //Si encuentra el nodo sin leer retorna la posición
                response = i;
        }
        return response; //Retorna -1 si ya fue leído, retorna -2 si no existe o la posición
    }
    
    //Agrega una nueva posición al vector
    public static Node[] addNode(Node[] n, char c, int w){
        Node[] R = new Node[n.length + 1];//Crea un nuevo vector con una posición más
        Node newNode = new Node(c, w); //Crea un nuevo nodo con los parámetros dados
        newNode.flag = true; //Marca el nodo como leído
        R[0] = newNode; //Agrega el nuevo nodo en la primera posición del nuevo vector
        System.arraycopy(n, 0, R, 1, n.length);//Copia el resto de posiciones al nuevo vector
        return R;
    }
    
    //Recorre el vector dado para marcar todos los nodos como no leídos
    public static void blackFlag(Node[] n){
        for (Node n1 : n) {
            n1.flag = false;
        }
    }
    
    //Ordena los nodos del vector de menor a mayor según la frecuencia de la letra 
    private static Node[] order(Node[] huffmanArray) {
        Node aux;
        for (int i = 0; i < huffmanArray.length; i++) {
            for (int j = i + 1; j < huffmanArray.length; j++) {
                if(huffmanArray[i].weight > huffmanArray[j].weight){
                    aux = huffmanArray[i];
                    huffmanArray[i] = huffmanArray[j];
                    huffmanArray[j] = aux;
                }
            }
        }        
        return huffmanArray;
    }
    
    public static void printFile(){
        FileWriter fichero = null;
        PrintWriter pw;// = null;
        try
        {
            fichero = new FileWriter("C:/Users/user/Desktop/prueba.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < 10; i++)
                pw.println("Linea " + i);

        } catch (IOException e) {
        } finally {
            try {
            // Aseguramos que se cierra el fichero.
            if (null != fichero)
               fichero.close();
            } catch (IOException e2) {
            }
        }
    }
    
    //Genera el archivo comprimido
    public static void genBinary(String path) throws FileNotFoundException, IOException{
        //Para el binario
        FileOutputStream fos = new FileOutputStream("C:/Users/user/Desktop/prueba.arq");
        BufferedOutputStream salida = new BufferedOutputStream(fos);

        File archivo = null;
        FileReader fr = null;
        BufferedReader br;// = null;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (path);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            
            String linea;
            while((linea = br.readLine()) != null){
                String bitString = "";
                for (int i = 0; i < linea.length(); i++) {
                    for (int j = 0; j < dictionary[0].length; j++) {
                        if (linea.charAt(i) == dictionary[0][j].charAt(0)) {
                            bitString = bitString + dictionary[1][j];
                        }
                    }
                }
                System.out.println(bitString);
                int i = 0;
                int f = 7;
                for (int j = 0; f < bitString.length(); j++) {
                    String sByte = bitString.substring(i, f);
                    int iByte = (Integer.valueOf(sByte.substring(0,1)) * 64) +
                            (Integer.valueOf(sByte.substring(1,2)) * 32) +
                            (Integer.valueOf(sByte.substring(2,3)) * 16) +
                            (Integer.valueOf(sByte.substring(3,4)) * 8) +
                            (Integer.valueOf(sByte.substring(4,5)) * 4) +
                            (Integer.valueOf(sByte.substring(5,6)) * 2) +
                            Integer.valueOf(sByte.substring(6,7));
                    salida.write(iByte);
                    System.out.print(iByte + " ");
                    i = i + 7;
                    f = f + 7;
                }
                if (true) { //Quedan caracteres menores a un byte
                    
                }
            }
        }
        catch(IOException e){
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
                if( null != fr ){   
                    fr.close();     
                }                  
            }catch (IOException e2){
            }
        }

        //Cerramos el binario
        salida.flush();
        fos.close();
    }
}