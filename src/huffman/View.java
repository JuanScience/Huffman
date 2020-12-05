package huffman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class View extends JFrame implements ActionListener {
    
    JLabel labelSub, labelRegister;
    JTextField txtRegister;
    JButton encrypt, decrypt;
    String texto;
    
    public View(){
        components();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 220);             //Tamaño
        setLocationRelativeTo(null);    //centra ventana en objeto
        setLayout(null);                //Elimina plantilla
        setResizable(true);
        setVisible(true);
        setTitle("Código Huffman");
    }
    
    public void components(){
        labelSub = new JLabel();
        labelSub.setBounds(10, 20, 600, 20);
        labelSub.setText("Bienvenido al compresor de archivos con algoritmo de Huffman");
        add(labelSub);
        
        encrypt = new JButton();
        encrypt.setBounds(50, 70, 300, 20);
        encrypt.setText("Comprimir");
        add(encrypt);
        encrypt.addActionListener(this);

        decrypt = new JButton();
        decrypt.setBounds(50, 120, 300, 20);
        decrypt.setText("Descomprimir");
        add(decrypt);  
        decrypt.addActionListener(this);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(encrypt)){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setDialogTitle("Escoja el archivo de texto a comprimir");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            FileFilter filter = new FileNameExtensionFilter("TXT file", "txt");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result != JFileChooser.CANCEL_OPTION) {

                File textFile = fileChooser.getSelectedFile();

                if ((textFile != null) || (textFile.getName().equals(""))) {
                    try {
                        
                        String textPath = textFile.getAbsolutePath();
                        String arqPath = textPath.substring(0, textPath.lastIndexOf(".")) + ".arq";
                        
                        Methods.encrypt(textPath);
                        
                        Methods.genBinary(textPath, arqPath);
                        
                        JOptionPane.showMessageDialog(null, "Archivo comprimido en:\n" + arqPath);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        if(e.getSource().equals(decrypt)){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setDialogTitle("Escoja el archivo de texto a descomprimir");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            FileFilter filter = new FileNameExtensionFilter("ARQ file", "arq");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result != JFileChooser.CANCEL_OPTION) {

                File textFile = fileChooser.getSelectedFile();

                if ((textFile != null) || (textFile.getName().equals(""))) {
                    try {
                        
                        String arqPath = textFile.getAbsolutePath();
                        String textPath = arqPath.substring(0, arqPath.lastIndexOf(".")) + "(Descomprimido).txt";
                        
                        Methods.decrypt(arqPath, textPath);
                        
                        JOptionPane.showMessageDialog(null, "Archivo descomprimido en:\n" + textPath);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    
}