/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class HiloUsuario extends Thread{
    
    private Usuario usuario;
    
    public HiloUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    
    public void run(){
        int opcion;
        System.out.println("Ingrese la opcion\n");
        Scanner sc = new Scanner(System.in);
        opcion = sc.nextInt();
        switch(opcion){
            case 1: //leer publicacion validada
                Iterator<Publicacion> it =  Global.publicacionesPasivas.iterator();
                int i = 1;
                Publicacion p;
                while(it.hasNext()){
                    
                    p = it.next();
                    System.out.println((i++) + ". " + p.getTitulo() + "\n"); //imprimo los titulos de las 
                                                                             //publicaciones validadas
                }
                System.out.println("Seleccione un título: ");
                opcion = sc.nextInt();
                p = Global.publicacionesPasivas.get(opcion-1);
                p.toString();
        {
            try {
                this.leerPublicacion(p, 5000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
        
    }

    
    public void leerPublicacion(Publicacion p, long tiempo) throws InterruptedException{
        if(Global.publicacionesPasivas.contains(p)){
            Thread.sleep(tiempo);
        }
    }

    
    
    
}
