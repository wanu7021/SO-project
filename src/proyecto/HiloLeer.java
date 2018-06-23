/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class HiloLeer extends Thread{
    private Publicacion p;
    private Usuario usuario;
    private long tiempo;
    
    public HiloLeer(Publicacion p, Usuario usuario){
        this.p = p;
        this.usuario = usuario;
        this.tiempo = tiempo;
    }
    
    @Override
    public void run(){
        try {
            usuario.getSem().acquire();
            System.out.println("El usuario " + this.usuario.getIdU() + " esta leyendo " + this.p.getTitulo());
            Thread.sleep(10000);
            Global.contador++;
            usuario.getSem().release();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloLeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
