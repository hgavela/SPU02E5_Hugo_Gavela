import static java.lang.Thread.sleep;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hugo Gavela
 */
public class SPU02E05_Hugo_Gavela_SupermercadoModerno {
    final static int NUMERO_CLIENTES = 10;
    final static int NUMERO_CAJAS = 3;
    static Thread[] clients;
    static DecimalFormat df2 = new DecimalFormat(".##");
    static ArrayList<Double> total = new ArrayList<>();
    
    
    static class Cola {

        ArrayList<Boolean> libres;
        ArrayList<String> cola_clientes = new ArrayList<>();

        public Cola(int tamany) {
            this.libres = new ArrayList<>();
            for (int i = 0; i < tamany; i++) {
                libres.add(true);
            }
        }
        //Metodo void que se encarga de poner en wait al thread que lo ejecuta
        synchronized void esperarCola() {

            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_SupermercadoModerno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Metodo coloca a true la posicion que se le pasa y despiarta a todos los threads
        synchronized void salirSuper(String n, int i) {
            System.out.println(n + " : Sale del supermercado\n");
            libres.set(i, true);
            notifyAll();
        }
        //Metodo que quita el strin de la arraylist que se le pasa por parametro y despierta a todos los threads
        synchronized void siguiente(String n) {
            cola_clientes.remove(n);
            notifyAll();
        }
        //Metodo integer que devuelve la primera posicion de la arraylist que este en true, en caso de que esten todas en false devuelve un -1
        int comprobarCola() {
            int id = -1;
            for (int i = 0; i < NUMERO_CAJAS; i++) {
                if (libres.get(i)) {
                    id = i;
                    libres.set(id, false);
                    break;
                }
            }
            return id;
        }
    }

    static class Client extends Thread {

        String nombre;
        Cola cola;
        double gastado;
        int id;

        public Client(String n, Cola c) {
            this.nombre = n;
            this.cola = c;
        }
        //Metodo que pone en sleep el threan entre 5 y 10 segundos
        void tiempoEspera() {
            try {
                sleep((int) (Math.random() * 5000) + 5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_SupermercadoModerno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Metedo pone en el thread en sleep entre 2 y 5 segs, genera una valor entre 01.00 y 50.00 lo muestra por pantalla y lo añade a la arraylist de doubles
        void compar() {
            try {
                sleep((int) (Math.random() * 3 + 2) * 1000);
                gastado = Math.random() * (50 - 1) + 1;
                System.out.println(nombre + " : Ha gastado " + df2.format(gastado));
                total.add(gastado);
            } catch (InterruptedException ex) {
                Logger.getLogger(SPU02E05_Hugo_Gavela_SupermercadoModerno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            tiempoEspera();
            System.out.println(nombre + " : Ha entrado en el supermercado");
            tiempoEspera();
            cola.cola_clientes.add(nombre);
            System.out.println(nombre + " : Llega a la cola");
            //Bucle while que mientras que el thread no este en la posicion 0 de la arraylist incica el metodo esperarCola()
            while (cola.cola_clientes.indexOf(nombre) != 0) { 
                cola.esperarCola();
            }
            id = cola.comprobarCola();
            //Bucle while que mientras el id sea -1 invoca el metodo esperarCola()
            while (id == -1) {
                System.out.println(nombre + " : Cajas ocupadas, esperando su turno");
                cola.esperarCola();
                id = cola.comprobarCola();
            }
            cola.siguiente(nombre);
            System.out.println(nombre + " : Pasa a la caja " + id);
            compar();
            cola.salirSuper(nombre, id);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        //Creamos un objeto cola y uno clients
        Cola c = new Cola(NUMERO_CAJAS);
        clients = new Thread[NUMERO_CLIENTES];
        //Bucle for que inicialice todos los threas de la array clients
        for (int i = 0; i < NUMERO_CLIENTES; i++) {
            clients[i] = new Client("Cliente " + i, c);
            clients[i].start();
        }

        for (Thread t : clients) {
            t.join();
        }
        
        //Sumamos todos los generado por cliente y mostramos el resultado por pantalla
        double recuento = 0;
        for (int i = 0; i < total.size(); i++) {
            recuento = recuento + total.get(i);
        }
        System.out.println("El supermercado ha cerrado\nLos ingresos han sido: " + df2.format(recuento));
    }

}