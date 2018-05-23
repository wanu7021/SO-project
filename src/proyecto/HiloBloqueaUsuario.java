/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class HiloBloqueaUsuario extends Thread{
    private Usuario usuario;
    private Semaphore mutex;
    
    public HiloBloqueaUsuario(Usuario usuario, Semaphore mutex){
        this.usuario = usuario;
        this.mutex = mutex;
    }
    
    public void run(){
        
        
        try {
            mutex.acquire();
            usuario.setAnuladas(usuario.getAnuladas() + 1);
            usuario.setPrem(0);
            if(usuario.getAnuladas() == Global.cantidadBloqueado){
                usuario.setBloqueado(true);
                usuario.setPremiado(false);
                System.out.println("EL usuario " + usuario.getIdU() + " ha sido bloqueado");
                    
            }
            mutex.release();
        
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloBloqueaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
