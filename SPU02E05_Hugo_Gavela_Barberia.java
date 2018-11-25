
import java.util.ArrayList;
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
public class SPU02E05_Hugo_Gavela_Barberia {

    final static int NUMERO_SILLAS = 4;
    final static int NUMERO_CLIENTES = 10;
    public static final String[] NOMBRES = {"Arturo","Manolo"}; //6
    
    static class Barbero extends Thread{
        int id_Barbero;
        boolean ocupado;
        String name;
        Silla sillas;
        
        public Barbero(String n, Silla s ,int i){
            this.name = n;
            this.sillas = s;
            this.id_Barbero = i;
        }

        @Override
        public void run() {
            while(true){
                /*
                while(sillas.cola_clientes.isEmpty()){
                    System.out.println(name+" : No hay ningun cliente, se pone a dormir");
                    sillas.Dormir();
                    System.out.println(sillas.cola_clientes.size());
                }*/
            }
           
        }
        
        boolean isOcupado() {
            return ocupado;
        }
        
        void Cortar(String n){
            try {
                sillas.cola_clientes.remove(n);
                sillas.sillas_ocupadas--;
                System.out.println(name+" : Empieza a cortar el pelo a "+n);
                sleep((int) (Math.random() * 5000) + 5000);
                System.out.println(name+" : Termnia de cortar el pelo a "+n);
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_Barberia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    static class Cliente extends Thread{
        String name;
        Silla sillas;
        Barbero[] barberos;
        
        public Cliente(String n, Silla s, Barbero[] b){
            this.name = n;
            this.sillas = s;
            this.barberos = b;
        }

        @Override
        public void run() {
            EsperarTiempo();
            System.out.println(name+ " : Llega a la barberia");
            while(sillas.sillas_ocupadas == NUMERO_SILLAS){
                System.out.println(name+" : Barberia llena, se va a pasear al perro");
                EsperarTiempo();
            }
            sillas.sillas_ocupadas++;
            System.out.println(name+ " : Entra en la barberia");
            sillas.cola_clientes.add(name);
            while(sillas.cola_clientes.indexOf(name) != 0){
                System.out.println(name +" : Esperando su turno");
                sillas.Dormir();
            }
            int id = obtenerBarbero();
            while(id == -1){
                System.out.println(name+" : Barberos ocupados");
                sillas.Dormir();
                id = obtenerBarbero();
            }
            barberos[id].Cortar(name);
            System.out.println(name+" : Sale de la berberia");
            barberos[id].ocupado = false;
            sillas.Despertar();
            
        }
        
        void EsperarTiempo(){
            try {
                sleep((int) (Math.random() * 20000) + 5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_Barberia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int obtenerBarbero(){
            int id = -1;
            for (Barbero barbero : barberos) {
                if(!barbero.isOcupado()){
                    id = barbero.id_Barbero;
                    barbero.ocupado = true;
                    break;
                }
            }
            return id;
        }
    }
    
    static class Silla {
        int sillas_ocupadas = 0;
        ArrayList<String>cola_clientes = new ArrayList<>();
        
        synchronized void Dormir(){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_Barberia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        synchronized void Despertar(){
            notifyAll();
        }
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Barbero[] barberos = new Barbero[2];
        Cliente[] clientes = new Cliente[NUMERO_CLIENTES];
        Silla sillas = new Silla();
        for (int i = 0;i<2;i++){
            barberos[i] = new Barbero(NOMBRES[i],sillas,i);
            barberos[i].start();
        }
        
        for (int i = 0; i< NUMERO_CLIENTES;i++){
            clientes[i] = new Cliente("Cliente "+i,sillas,barberos);
            clientes[i].start();
        }
    }
    
    
}
