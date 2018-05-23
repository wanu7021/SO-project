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
import java.util.concurrent.Semaphore;

/**
 *
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
    


}
