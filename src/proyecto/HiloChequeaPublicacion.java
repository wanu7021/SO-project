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
public class HiloChequeaPublicacion extends  Thread{
    private Publicacion p;
    
    public HiloChequeaPublicacion(Publicacion p){
        this.p = p;
    }
    
    public void run(){
        obtenerActiva();
        if(!this.p.isActiva()){
            if(this.p.getPorcentajeAFavor()>0.6){
                try {
                    this.p.validarPublicacion();
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloChequeaPublicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    this.p.anularPublicacion();

                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloChequeaPublicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void obtenerActiva(){
        if (this.p.getPorcentajeAFavor()>0.6 || this.p.getPorcentajeEnContra()>0.4){
            this.p.setActiva(false);
        }
    }
    
}
