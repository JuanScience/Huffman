/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Methods {
    static Node[] huffmanArray = new Node[0];
    static String[][] dictionary;
    static int count;
    
    //Método para encriptar texto cuya ruta recibe como parámetro
    public static String[][] encrypt(String pathTxtToEn) throws IOException{
        readFile(pathTxtToEn);
        huffmanArray = order(huffmanArray);
        dictionary = new String[2][huffmanArray.length];
        count = 0;
        dictionary = huffmanAlgorithm(huffmanArray, dictionary);
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
        }
        String s = "";
        readInOrder(huffmanArray[0], s);
        return dictionary;
    }
    
    //Método recursivo para crear el diccionario leyendo in orden el árbol huffman
    public static void readInOrder(Node n, String s){        
        if (n != null) {   
            if(n.getData() != '¥'){
                dictionary[0][count] = String.valueOf(n.getData());
                dictionary[1][count] = s;
                count++;
            }
            readInOrder(n.getLeft(), s = s + "0");
            s= s.substring(0, s.length() - 1);
            readInOrder(n.getRight(), s = s + "1");
            s = s.substring(0, s.length() - 1);
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
    //-----------------------------------------------------------------------
    //Genera el archivo comprimido
    public static void genBinary(String pathTxtToEn, String pathArq) throws FileNotFoundException, IOException{
        try {

            //Para escribir el binario
            FileOutputStream fileOutput = new FileOutputStream(pathArq);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);

            //Para leer el archivo en texto
            File archivo = new File (pathTxtToEn);
            FileReader fr = new FileReader (archivo);
            BufferedReader bufferedInput = new BufferedReader(fr);
            
            String bitString; //Variable para almacenar los bits encriptados
            
            //Escribimos el largo del diccionario
            int lenDictionary = dictionary[0].length;
            
            bufferedOutput.write(lenDictionary + 40);
            bufferedOutput.write(13);
            
            //Escribimos el diccionario            
            for (int i = 0; i < lenDictionary; i++) { //Recorre el diccionario
                bufferedOutput.write(dictionary[0][i].getBytes()); //Imprime el byte correspondiente a la letra
                bufferedOutput.write(dictionary[1][i].trim().getBytes()); //Imrpime los bytes correspondientes al código
                bufferedOutput.write(13); //Salto de línea
            }

            String linea;
            
            while((linea = bufferedInput.readLine()) != null){
                bitString = "";
                for (int i = 0; i < linea.length(); i++) { //Recorre la línea para encriptar
                    for (int j = 0; j < dictionary[0].length; j++) { //Recorre el diccionario por cada caracter
                        if (linea.charAt(i) == dictionary[0][j].charAt(0)) { //Si lo encuentra en el diccionario
                            bitString = bitString + dictionary[1][j];       //Agrega su codificación al String encriptado
                        }
                    }
                }
                int residualBits = bitString.length() % 7;
                int i = 0;
                int f = 7;
                
                if(linea.length() > 0){ //Imprime byte de lectura de último byte, si la línea no está vacía
                    bufferedOutput.write(-residualBits);
                }
                while (f < bitString.length()) { //Corta el string cada siete caracteres
                    String sByte = bitString.substring(i, f);
                    int iByte = (Integer.valueOf(sByte.substring(0, 1)) * 64) +
                            (Integer.valueOf(sByte.substring(1, 2)) * 32) +
                            (Integer.valueOf(sByte.substring(2, 3)) * 16) +
                            (Integer.valueOf(sByte.substring(3, 4)) * 8) +
                            (Integer.valueOf(sByte.substring(4, 5)) * 4) +
                            (Integer.valueOf(sByte.substring(5, 6)) * 2) +
                             Integer.valueOf(sByte.substring(6, 7));
                    bufferedOutput.write(-iByte); //Imprime el byte negativo
                    i = i + 7;
                    f = f + 7;
                }
                int iByte = 0;
                int valueByte = 64;
                for (int j = 0; j < residualBits; j++) { //Si quedan bits menores a un byte al final
                    String sByte = bitString.substring(i);
                    iByte = iByte + (Integer.valueOf(sByte.substring(j, j + 1)) * valueByte);
                    valueByte = valueByte / 2;
                }
                iByte = -iByte;
                
                bufferedOutput.write(iByte);//Imprime el byte negativo
                bufferedOutput.write(13);//Salto de línea
            }
            bufferedInput.close();
            bufferedOutput.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //-----------------------------------------------------------------------
    //Desencripta
    public static void decrypt(String pathArq, String pathTxtToDe) throws IOException {
        try {
            
            //Para leer el texto del archivo (Solo para el diccionario)
            File archivo = new File (pathArq);
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
            
            //Objetos para escribir el texto del archivo descifrado
            FileWriter fichero = new FileWriter(pathTxtToDe);
            PrintWriter pw = new PrintWriter(fichero);

            dictionary = new String[2][0];//Reinicia el diccionario
            
            // Lectura del DICCIONARIO
            String linea;
            int lengthDictionary = 0;
            //Lee el largo del diccionario:
            if((linea = br.readLine()) != null){ //Lee la primera línea del archivo
                lengthDictionary = (linea.substring(0, 1).getBytes()[0]) - 40;
            }
            
            for (int i = 0; i < lengthDictionary; i++) {//Lee las líneas del diccionario
                linea = br.readLine();
                incDictionary(linea.substring(0, 1), linea.substring(1).trim());//Ingresa los nuevos datos al diccionario
            }

            //Cerramos el BufferedRead después de leer el diccionario (Texto)
            fr.close();
            
            //Objetos para leer archivo encriptado en binario
            FileInputStream fileInput = new FileInputStream(pathArq);
            BufferedInputStream bi = new BufferedInputStream(fileInput);
            
            //Guardamos todos los bytes en un arreglo
            byte [] array = new byte[bi.available()];
            bi.read(array);
            
            //Avanzamos hasta que encontremos el final del diccionario
            int contn = 0; //Contador de retornos de carro
            int i = 0;      //Contador para el arreglo de bytes
            
            while ( contn != lengthDictionary + 1 ) {
                if (array[i] == 13){
                    contn++;
                }
                i++;
            }
            //Recorremos el texto encriptado
            linea = String.valueOf(-array[i]);
            i++;
            for (int j = i; j < array.length; j++) {
                if (array[j] != 13) {              //Mientras no llegue a un salto de línea      
                    linea = linea + ByteToString((int)-array[j]);
                }else {
                    pw.println(decodeHuffman(linea));
                    linea = "";
                    if( j + 1 < array.length )
                        linea = String.valueOf(-array[j + 1]);
                    j++;
                }
            }
            
            //Cierra el BufferedInput
            bi.close();
            
            //Cierra el fichero desencriptado
            fichero.close();
            
        }catch(IOException e){
        }
    }

    //Incrementa en uno las posiciones del diccionario e ingresa la posición nueva
    private static void incDictionary(String caracter, String code) {
        String[][] ndictionary = new String[2][dictionary[0].length + 1];
        for (int i = 0; i < dictionary[0].length; i++) { //Copia todas las posiciones del diccionario viejo al nuevo
            ndictionary[0][i] = dictionary[0][i];
            ndictionary[1][i] = dictionary[1][i];
        }
        ndictionary[0][dictionary[0].length] = caracter; //Copia el último caracter
        ndictionary[1][dictionary[1].length] = code;     //Copia el último código
        dictionary = ndictionary;
    }

    public static String decodeHuffman(String linea) {
        String toPrint = "";
        
        int i = 1;
        int j = 2;
        String sbString = "";
        while (i < ((((linea.length() - 1) / 7 ) - 1) * 7) + Character.getNumericValue(linea.charAt(0)) + 1) {
            sbString = linea.substring(i, j);
            for (int m = 0; m < dictionary[0].length; m++) {
                if (sbString.matches(dictionary[1][m]) ) {
                    toPrint = toPrint + dictionary[0][m];
                    i = j;
                }
            }
            j++;
        }
        
        return toPrint;
    }

    public static String ByteToString(int b) {
        String toConcatenate = "";
        int w = 64;
        for (int i = 6; i >= 0; i--) {
            if(b >= w){
                toConcatenate = toConcatenate + "1";
                b = b - w;
            }else{
                toConcatenate = toConcatenate + "0";
            }
            w = w / 2;
        }
        return toConcatenate;
    }
}
