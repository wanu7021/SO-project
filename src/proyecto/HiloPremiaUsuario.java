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
public class HiloPremiaUsuario extends Thread{
    private Usuario usuario;
    
    public HiloPremiaUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    
    public void run(){
        try {
            usuario.getSem().acquire();
            usuario.setAnuladas(0);
            usuario.setPrem(usuario.getPrem() + 1);
            if(usuario.getPrem() == Global.cantidadPremiado){
                usuario.setPremiado(true);
            }
            usuario.getSem().release();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloPremiaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
