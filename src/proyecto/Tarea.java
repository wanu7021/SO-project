/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;

/**
 *
 * @author federico
 */
public class Tarea {
    private String datos;
    private double prioridad;
    //private ArrayList tareas;
    
    
    public Tarea(String datos){
        this.datos = datos;
        //this.tareas = new ArrayList();
        this.prioridad = this.setPriority();
    }

   /* public ArrayList getTareas() {
        return tareas;
    }

    public void setTareas(ArrayList tareas) {
        this.tareas = tareas;
    }*/

    
    
    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public double getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
    
    public double setPriority(){
        String[] split = this.datos.split(";");
        if(split[0].equals("Votar")){
            boolean favor = false;
            if(split[1].equals("Favor")) favor = true;
            Usuario votante = Global.buscarUsuario(Integer.parseInt(split[2]));
            if(votante == null)return -1;
            Publicacion pub = Global.buscarPublicacion(Integer.parseInt(split[3]));
            if(pub == null) return -1;
            
            /*this.tareas.add("Votar");
            this.tareas.add(favor);
            this.tareas.add(pub.getTema());*/
                
            if(votante.getExpertise().contains(pub.getTema())){
                //tareas.add("experto");
                return Global.votoExperto;
            }
            
            else if(votante.isPremiado()) {
                //tareas.add("premiado");
                return Global.votoPremiado;
            }
            
            else {
                //tareas.add("comun");
                return Global.votoComun;
            }                        
        }
        
        else if(split[0].equals("Leer")){
            return 0;
        }
        
        else if(split[0].equals("Eliminar")){
            return 3;
        }
        
        return 0.5;
    }
    
    
}
