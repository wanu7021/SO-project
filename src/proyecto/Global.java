/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.*;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.PriorityQueue;

/**
 * Una lista, cola de datos de entradas y varios hilos con un propósito. 
 * Tengo que ir tomando cada dato de entrada y mandarlo a un hilo correspondiente.
 * Mando tantos datos a ejecutar en paralelo como hilos tenga. Cuando termino de ejecutar, 
 * espero a que todos los hilos que tengo ejecutando terminen. CUando terminen, sumo uno 
 * a la unidad de tiempo.
 * @author federico
 */
public class Global {
    public static List<Publicacion> publicacionesPasivas = new ArrayList<>(); //Publicaciones validadas. Recurso que todos tienen acceso(solo las validadas). 
                               //Las anuladas solo son un contador en el usuario correspondiente. 
    public static List<Usuario> usuarios = new ArrayList<>(); //todos los usuarios (expertos + consumidores + activos.
    public static List<Usuario>  usuariosExtertos = new ArrayList<>(); //consumen y votan con mayor prioridad
    public static List<Usuario>  usuariosConsumidores = new ArrayList<>(); //solo consumen
    public static List<Usuario>  usuariosActivos = new ArrayList<>(); //Habilitados a votar  y consumir
    public static List<Usuario>  usuariosBloqueados = new ArrayList<>(); 
    public static List<Usuario>  usuariosPremiados = new ArrayList<>(); //usuarios que consumen y votan con mayor prioridad a uno activo pero menor a un 
                            //experto
    public static List<Publicacion>  publicacionesActivas = new ArrayList<>(); //las que estan para votar, aun no han sido validadas ni anuladas.
    /*Los hilos hacen chequeos y modifican los recursos globales. */
    public static double votoComun = 1;
    public static double votoPremiado = 1.5;
    public static double votoExperto = 2;
    public static int cantidadUsuariosActivos = 0;
    public static int cantidadBloqueado = 3;
    public static int cantidadPremiado = 5;
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore mutex2 = new Semaphore(1);
    public static Semaphore mutexBloqueoUsuario = new Semaphore(1);
    public static Semaphore mutexVotacionUsuario = new Semaphore(0);
    public static Semaphore semContador = new Semaphore(4); //4 hilos como máximo van a correr
    public static int contador = 0;
    public static LinkedList<Tarea> colaDeTareas = new LinkedList<>();
    public static Semaphore mutexTareas = new Semaphore(1);
    public static int idPub = 0;
    public static int idUsuario = 0;
    public static ArrayList<Thread> listaTareas = new ArrayList<>();
    
    public static Usuario buscarUsuario(int id){
        Usuario usuario = null;
        for(int i = 0; i < Global.usuariosActivos.size(); i++){
            if(Global.usuariosActivos.get(i).getIdU() == id){
                usuario = Global.usuariosActivos.get(i);
                break;
            }
        }
        return usuario;           
    }
    
    public static Publicacion buscarPublicacion(int id){
        Publicacion pub = null;
        for(int i = 0; i< Global.publicacionesActivas.size();i++){
            if(Global.publicacionesActivas.get(i).getId() == id){      //PONER ID
                pub = Global.publicacionesActivas.get(i);
                break;
            }
        }
        return pub;
    }
    
    public static void crearHilo(String accion){
        String[] split = accion.split(";");
        switch(split[0]){
                case "Votar":
                    Usuario usVotante = Global.buscarUsuario(Integer.parseInt(split[2]));
                    
                    Publicacion pub = Global.buscarPublicacion(Integer.parseInt(split[3]));
                    
                    if(usVotante == null || pub == null) break;
                    
                    try{
                        //System.out.println(this.usuario.getIdU() +" bloquea para votar a " + pub.getTitulo());
                        if(usVotante.esActivo()){
                            if(split[1].equals("Favor")){
                                HiloVotar vot = new HiloVotar(true,pub,usVotante);
                                listaTareas.add(vot);
                                //vot.run();
                            }

                            else{
                                HiloVotar vot = new HiloVotar(false,pub,usVotante);
                                listaTareas.add(vot);
                                //vot.run();
                            }
                        }

                        //System.out.println(this.usuario.getIdU() +" libera para votar a " + pub.getTitulo());
                    }catch(Exception ex){
                        ex.getCause();
                    }
                    break;
                case "Leer":
                    Usuario usLector = Global.buscarUsuario(Integer.parseInt(split[2]));
                    
                    Publicacion pubLeer = Global.buscarPublicacion(Integer.parseInt(split[1]));
                    
                    if (usLector == null || pubLeer == null) break;
                    
                    HiloLeer leer = new HiloLeer(pubLeer, usLector);
                    listaTareas.add(leer);
                    //leer.run();
                    break;
                case "Crear":
                    Usuario usCreador = Global.buscarUsuario(Integer.parseInt(split[4]));
                    
                    if (usCreador == null) break;
                    
                    HIloPublicar crear = new HIloPublicar(split[1],split[2],split[3],usCreador,5000);
                    listaTareas.add(crear);
                    //crear.run();
                    break;
                case "Eliminar":
                    Usuario usEliminador = Global.buscarUsuario(Integer.parseInt(split[1]));
                    
                    Publicacion pubEliminar = Global.buscarPublicacion(Integer.parseInt(split[2]));
                    
                    if (usEliminador == null || pubEliminar == null) break;
                    
                    HiloEliminarPublicacion eliminar = new HiloEliminarPublicacion(usEliminador,pubEliminar);
                    listaTareas.add(eliminar);
                    //eliminar.run();
                    
                    break;
            }    
    }
    
}
