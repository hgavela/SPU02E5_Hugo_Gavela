
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hugo Gavela
 */
public class SPU02E05_Hugo_Gavela {
    
    static Process fill;
    final static String[] OPCION = {"SPU02E05_Hugo_Gavela_Barberia","SPU02E05_Hugo_Gavela_SupermercadoModerno"};
    
    public static void main(String[] args) {
        try {
            String palabra = "";
            int id = opcion();
            boolean isWindows = detectarOs();
            if (isWindows) {
                fill = new ProcessBuilder("java",OPCION[id]).start();
            } else{
                fill = new ProcessBuilder("/usr/bin/java", OPCION[id]).start();
            }
            
            InputStream is = fill.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();

            
        } catch (IOException ex) {
            Logger.getLogger(SPU02E05_Hugo_Gavela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static boolean detectarOs(){
        return System.getProperty("os.name").startsWith("Windows");
    }
    
    static int opcion(){
        int op;
        boolean sem = true;
        do{
            System.out.println("Programa a ejecutrar:\n"
                    + "  0.- Barberia\n"
                    + "  1.- Supermercado Moderno");
            Scanner sc = new Scanner (System.in);
            op = sc.nextInt();
            if (op == 0 || op == 1){sem =false;}
        }while(sem);
        return op;
    }

}
