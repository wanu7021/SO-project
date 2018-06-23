/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class HiloEliminarPublicacion extends Thread{
    private Usuario usuarioEmisor;
    private Publicacion p;
    private Semaphore mutex;
    private long tiempo;
    
    public HiloEliminarPublicacion(Usuario usuarioEmisor, Publicacion p){
        this.p = p;
        this.usuarioEmisor = usuarioEmisor;
        this.tiempo = tiempo;
    }
    
    public void run(){
        
        if(this.p.getUsuarioEmisor() == this.usuarioEmisor.getIdU()){
            try {
               //mutex.acquire();  //bloqueo para que no elimine una publicacion que alguien la este
                                  //votando o el sistema la quiera validar.
                p.getMutex().acquire();
                usuarioEmisor.getSem().acquire();
                System.out.println(usuarioEmisor.getNombre()+" bloquea para eliminar " + p.getTitulo());
                Thread.sleep(3000);
                Global.publicacionesActivas.remove(p);
                System.out.println(usuarioEmisor.getNombre() +" libera para eliminar " + p.getTitulo());
                usuarioEmisor.getSem().release();
                p.getMutex().release();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloEliminarPublicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else System.out.println(usuarioEmisor.getNombre() + " no es el dueño de " + p.getTitulo());
        Global.contador++;
        try{
            Thread.sleep(3000);
            wait();
        }
        catch(Exception ex){
            ex.getCause();
        }
    }
}
