/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

/**
 *
 * @author Enzo
 */
public class HiloVotar extends Thread{
    private boolean aFavor;
    private Publicacion pub;
    private Usuario us;
    private int voto;
    
    public HiloVotar(boolean aFavor, Publicacion pub, Usuario us){
        this.aFavor = aFavor;
        this.pub = pub;
        this.us = us;
        voto = 0;
    }
    
    public void run(){
        if(!us.esActivo()){
            System.out.println("El usuario no es activo");
        }
        
        else if(pub.getVotantes().contains(us.getIdU())){
            System.out.println(us.getNombre()+" ya voto esta publicacion");
        }
        else{
            try{
                if(us.isPremiado()){
                    Thread.sleep(10);
                }
                else if(!us.getExpertise().contains(pub.getTema())){
                    Thread.sleep(20);
                }
                pub.getMutex().acquire();
                us.getSem().acquire();
                if(pub.getUsuarioEmisor() == us.getIdU()){
                    System.out.println(us.getNombre()+" no puede votar su propia publicacion.");
                }else{
                    System.out.println(us.getNombre()+" va a votar "+ pub.getTitulo());
                    Thread.sleep(5000);
                    if(us.getExpertise().contains(pub.getTema())){
                        if (aFavor){
                            pub.votarAFavor(Global.votoExperto, us.getIdU());
                        }
                        else{
                            pub.votarEnContra(Global.votoExperto, us.getIdU());
                        }
                    }
                    else if(us.isPremiado()){
                        if (aFavor){
                            pub.votarAFavor(Global.votoPremiado, us.getIdU());
                        }
                        else{
                            pub.votarEnContra(Global.votoPremiado, us.getIdU());
                        }
                    }
                    else{
                        if (aFavor){
                            pub.votarAFavor(Global.votoComun, us.getIdU());
                        }
                        else{
                            pub.votarEnContra(Global.votoComun, us.getIdU());
                        }
                    }
                    System.out.println(pub.getTitulo()+" fue liberada por "+us.getNombre());
                }
                Global.contador++;
                us.getSem().release();
                pub.getMutex().release();
            }
            catch(InterruptedException ex){
                ex.getCause();
            }
        }
    }
}
