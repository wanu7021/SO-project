/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 *
 * @author federico
 */
public class Usuario {
    private final int idU;
    private int telefono;
    private String nombre, direccion;
    private boolean esActivo;
    private ArrayList expertise;
    private boolean premiado;
    private int anuladas;
    private boolean bloqueado;
    private int prem;
    private Semaphore mutex;
    
    public Usuario(String nombre, String direccion, int telefono, ArrayList expertise){
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.idU = Global.idUsuario++;
        this.expertise = expertise;
        this.esActivo = true;
        this.premiado = false;
        this.expertise = expertise;
        this.anuladas = 0;
        this.bloqueado = false;
        this.prem = 0;
        this.mutex = new Semaphore(1);
    }
    
    public int getIdU() {
        return idU;
    }


    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean esActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public ArrayList getExpertise() {
        return expertise;
    }

    public void setExpertise(ArrayList expertise) {
        this.expertise = expertise;
    }

    public boolean isPremiado() {
        return premiado;
    }

    public void setPremiado(boolean premiado) {
        this.premiado = premiado;
    }

    public int getAnuladas() {
        return anuladas;
    }

    public void setAnuladas(int anuladas) {
        this.anuladas = anuladas;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public int getPrem() {
        return prem;
    }

    public void setPrem(int prem) {
        this.prem = prem;
    }
    
    public boolean crearPublicacion(String contenido, String titulo, String tema){
        if(!this.esActivo || this.bloqueado) return false;
        Publicacion p = new Publicacion(titulo, contenido, this.idU, tema);
        Global.publicacionesActivas.add(p);
        return true;
    }
    
    public void votarPublicacionConsola() throws InterruptedException{
        Iterator<Publicacion> it = Global.publicacionesActivas.iterator();
        int i = 1, opcion;
        double tipoVoto;
        Publicacion pub = null;
        if(!Global.publicacionesActivas.isEmpty()){
            System.out.println("Seleccione la publicación a votar");
            while(it.hasNext()){
                pub = it.next();
                System.out.println((i++) + ". " + pub.getTitulo()); //imprimo todos los titulos
            }
            
            Scanner sc = new Scanner(System.in);
            opcion = sc.nextInt();
            if(opcion >= 1 && opcion <= Global.publicacionesActivas.size()){
                pub = Global.publicacionesActivas.get(opcion-1);   //tomo la opcion
            }else{
                System.out.println("Ingrese una opción correcta\n");
                return;
            }
        
            if(this.expertise.contains(pub.getTema())){
                tipoVoto = Global.votoExperto;
            }else if(this.premiado){
                tipoVoto = Global.votoPremiado;
            }else{
                tipoVoto = Global.votoComun;
            }
        
            System.out.println("Ingrese '1' si desea votar a favor o '2' para votar en contra\n");
            opcion = sc.nextInt();
            if(opcion == 1){
                pub.votarAFavor(tipoVoto, this.idU);
            }
            else if(opcion == 2){
               pub.votarEnContra(tipoVoto, this.idU);
            }
            
            
        }
        else{
            System.out.println("No hay publicaciones activas para votar");
        }
    }
    
    public void votarPublicacion(Publicacion p, boolean aFavor) throws InterruptedException{
        System.out.println(this.idU + " quiere votar una publicacion "+p.getTitulo());
        mutex.acquire();
        System.out.println("obtuvo el recurso");
        System.out.println(this.bloqueado);
        if(!this.esActivo || this.bloqueado){
            return;
        }
        
        HiloVotar hilo = new HiloVotar(aFavor, p,this);
        boolean experto = this.expertise.contains(p.getTema());
        if(experto){
            hilo.setPriority(8); //menos que bloquear pero mas que premiado
        }else if(this.isPremiado()){
            hilo.setPriority(4); //menos que experto y menos que el hilo premiado
        }else{
            hilo.setPriority(1); //más basica
        }
        
        hilo.start();
        mutex.release();
        
    }
    
    public void leerPublicacion(Publicacion p) throws InterruptedException{
        if(this.bloqueado) return;
        HiloLeer hilo = new HiloLeer(p, this);
        mutex.acquire();
        hilo.start();
        mutex.release();
    }
    
    public void eliminarPublicacion(Publicacion p) throws InterruptedException{
        if(this.bloqueado || p.getUsuarioEmisor() == (this.idU)) return;
        HiloEliminarPublicacion hilo = new HiloEliminarPublicacion(this, p);
        mutex.acquire();
        hilo.start();
        mutex.release();
    }
    
    public Semaphore getSem(){
        return this.mutex;
    }
    
    
}
