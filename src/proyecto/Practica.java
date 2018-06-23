/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

/**
 *
 * @author federico
 */
public class Practica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        /*Thread procesoUno = new ProcesoUno();
        Thread procesoDos = new Thread();
        Thread principal = new ProgramaPrincipal();*/
        
        
        
        ProgramaPrincipal p = new ProgramaPrincipal();
        //Global g = new Global();
        p.cargar();
        
        Comparator<Tarea> c = new Comparator<Tarea>() {
            @Override
            public int compare(Tarea o1, Tarea o2) {
                if(o1.getPrioridad() > o2.getPrioridad()) return 1;
                return -1;
            }
        };
        Global.colaDeTareas.sort(c); //ordenada por prioridad     
        
        
        int tiempo = 0;
        
        /**
         * Aca tengo que tomar una tarea de la cola, ejecutarla y bloquear el semaforo contador
         * , lo cual decrementa en 1. Para mandar a ejecutar, tengo que agarrar la tarea 
         * siempre del principio, ya que es la más piorizante, tomar la accion, ver cuál es,
         * y crear un hilo en base a eso.
         * 
         */
        int k = Global.colaDeTareas.size()-1;
        while (k>=0){
            System.out.println("Tiempo "+ tiempo++);
            
            //Global.mutexTareas.acquire();
          
            
            if(k>2){

                String accion = Global.colaDeTareas.get(k).getDatos();
                String accion1 = Global.colaDeTareas.get(k-1).getDatos();
                String accion2 = Global.colaDeTareas.get(k-2).getDatos();
                String accion3 = Global.colaDeTareas.get(k-3).getDatos();

                Global.crearHilo(accion);
                Global.crearHilo(accion1);
                Global.crearHilo(accion2);
                Global.crearHilo(accion3);
                
                
                Global.listaTareas.get(0).start();
                Global.listaTareas.get(1).start();
                Global.listaTareas.get(2).start();
                Global.listaTareas.get(3).start();
                
                
                while(true){
                    if (Global.contador%4 == 0 && Global.contador != 0){
                        break;
                    }
                    Thread.sleep(1);
                }
                
                Global.mutexTareas.release();
                
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                
            }
            else if(k == 2){
                String accion = Global.colaDeTareas.get(k).getDatos();
                String accion1 = Global.colaDeTareas.get(k-1).getDatos();
                String accion2 = Global.colaDeTareas.get(k-2).getDatos();

                Global.crearHilo(accion);
                Global.crearHilo(accion1);
                Global.crearHilo(accion2);

                Global.listaTareas.get(0).start();
                Global.listaTareas.get(1).start();
                Global.listaTareas.get(2).start();
                
                while(true){
                    if (Global.contador%4 == 3 && Global.contador != 0){
                        break;
                    }
                    Thread.sleep(1);
                }
                Global.mutexTareas.release();
                
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                
            }
            else if (k==1){
                
                String accion = Global.colaDeTareas.get(k).getDatos();
                String accion1 = Global.colaDeTareas.get(k-1).getDatos();

                Global.crearHilo(accion);
                Global.crearHilo(accion1);

                Global.listaTareas.get(0).start();
                Global.listaTareas.get(1).start();
                
                while(true){
                    if (Global.contador%4 == 2 && Global.contador != 0){
                        break;
                    }
                    Thread.sleep(1);
                }
                Global.mutexTareas.release();
                
                Global.listaTareas.remove(0);
                Global.listaTareas.remove(0);
                
            }
            else{
                
                String accion = Global.colaDeTareas.get(k).getDatos();
               
                Global.crearHilo(accion);
               
                Global.listaTareas.get(0).start();
                
                while(true){
                    if (Global.contador%4 == 1 && Global.contador != 0){
                        break;
                    }
                    Thread.sleep(1);
                }
                Global.mutexTareas.release();
                
                Global.listaTareas.remove(0);
                
            }
            
            
            
            k-=4;
                       
        }
        
    }

    
}
