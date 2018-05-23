/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.concurrent.Semaphore;

/**
 *
 * @author federico
 */
public class HiloVoto extends Thread{
    private Semaphore mutex;
    private Usuario usuario;
    private Publicacion pub;
    private boolean aFavor;
    private long tiempo;
    
    public HiloVoto(Usuario usuario, Publicacion pub, boolean aFavor, long tiempo){
        mutex = Global.mutex;
        this.usuario = usuario;
        this.pub = pub;
        this.aFavor = aFavor;
        this.tiempo = tiempo;
    }
    
    public void run(){
        try{
            mutex.acquire(); 
            //System.out.println(this.usuario.getIdU() +" bloquea para votar a " + pub.getTitulo());
            if(usuario.esActivo()){
                if(aFavor){
                    if(this.usuario.getExpertise().contains(pub.getTema())){
                        pub.votarAFavor(Global.votoExperto,usuario.getIdU());
                        
                    }else if(this.usuario.isPremiado()){
                        pub.votarAFavor(Global.votoPremiado,usuario.getIdU());
                    }else{
                        pub.votarAFavor(Global.votoComun,usuario.getIdU());
                    }
                    //ystem.out.println("El usuario " + this.usuario.getIdU() + "voto a favor a " +
                                            //this.pub.getTitulo());
                }

                else{
                    if(this.usuario.getExpertise().contains(pub.getTema())){
                        pub.votarEnContra(Global.votoExperto,usuario.getIdU());
                    }else if(this.usuario.isPremiado()){
                        pub.votarEnContra(Global.votoPremiado,usuario.getIdU());
                    }else{
                        pub.votarEnContra(Global.votoComun,usuario.getIdU());
                    }
                    //System.out.println("El usuario " + this.usuario.getIdU() + "voto en contra a " +
                                           // this.pub.getTitulo());
                }
            }
            
            mutex.release();
            //System.out.println(this.usuario.getIdU() +" libera para votar a " + pub.getTitulo());
        }catch(InterruptedException ex){
            ex.getCause();
        }
        
    }
}
