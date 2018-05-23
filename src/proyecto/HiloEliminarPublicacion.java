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
    private UUID usuarioEmisor;
    private Publicacion p;
    private Semaphore mutex;
    private long tiempo;
    
    public HiloEliminarPublicacion(UUID usuarioEmisor, Publicacion p, long tiempo, Semaphore mutex){
        this.p = p;
        this.usuarioEmisor = usuarioEmisor;
        this.mutex = mutex;
        this.tiempo = tiempo;
    }
    
    public void run(){
        
        if(this.p.getUsuarioEmisor().equals(this.usuarioEmisor)){
            try {
               //mutex.acquire();  //bloqueo para que no elimine una publicacion que alguien la este
                                  //votando o el sistema la quiera validar.
                System.out.println(usuarioEmisor+" bloquea para eliminar " + p.getTitulo());
                p.eliminarPublicacionActiva();
                //mutex.release();
                System.out.println(usuarioEmisor +" libera para eliminar " + p.getTitulo());
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloEliminarPublicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
