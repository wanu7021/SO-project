/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.UUID;

/**
 *
 * @author federico
 */
public class HIloPublicar extends Thread{
    private String titulo;
    private String contenido;
    private String tema;
    private Usuario usuarioEmisor;
    private long tiempo;

    public HIloPublicar(String titulo, String contenido, String tema, Usuario usuarioEmisor, long tiempo) {
        this.titulo = titulo;
        this.tema = tema;
        this.contenido = contenido;
        this.usuarioEmisor = usuarioEmisor;
        this.tiempo = tiempo;
    }
    
    public void run(){
        if(usuarioEmisor.esActivo()){
            try{
                usuarioEmisor.getSem().acquire();
                System.out.println(usuarioEmisor.getNombre() + " se bloquea para crear una publicacion.");
                Publicacion p = new Publicacion(this.titulo, this.contenido, this.usuarioEmisor.getIdU(),
                               this.tema);
                Global.publicacionesActivas.add(p);
                Global.contador++;
                usuarioEmisor.getSem().release();
            }
            catch(Exception ex){
                ex.getCause();
            }
        }
        else{
            System.out.println("No es activo");
        }
    }

    
    
}
