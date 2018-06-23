/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class Publicacion {
    private int usuarioEmisor;
    private String contenido;
    private double contadorVotosAFavor;
    private double contadorVotosEnContra;
    private boolean activa; //true si la publicacion es activa, false si es anulada
    private boolean validada;
    private double porcentajeAFavor;
    private double porcentajeEnContra;
    private String titulo;
    private String tema;
    private Semaphore mutex;
    private int id;
    private ArrayList<Integer> votantes;

    public Publicacion(String titulo, String contenido, int emisor, String tema) {
        activa = true;
        validada = false;
        this.contenido = contenido;
        this.usuarioEmisor = emisor;
        this.titulo = titulo;
        contadorVotosAFavor = 0;
        contadorVotosEnContra = 0;
        porcentajeAFavor = 0;
        porcentajeEnContra = 0;
        this.tema = tema;
        this.mutex = new Semaphore(1);
        this.id = Global.idPub++;
        votantes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getVotantes() {
        return votantes;
    }
    
    public int getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(int usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public double getContadorVotosAFavor() {
        return contadorVotosAFavor;
    }

    public void setContadorVotosAFavor(double contadorVotosAFavor) {
        this.contadorVotosAFavor = contadorVotosAFavor;
    }

    public double getContadorVotosEnContra() {
        return contadorVotosEnContra;
    }

    public void setContadorVotosEnContra(double contadorVotosEnContra) {
        this.contadorVotosEnContra = contadorVotosEnContra;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isValidada() {
        return validada;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }

    public double getPorcentajeAFavor() {
        return porcentajeAFavor;
    }

    public void setPorcentajeAFavor(double porcentajeAFavor) {
        this.porcentajeAFavor = porcentajeAFavor;
    }

    public double getPorcentajeEnContra() {
        return porcentajeEnContra;
    }

    public void setPorcentajeEnContra(double porcentajeEnContra) {
        this.porcentajeEnContra = porcentajeEnContra;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Semaphore getMutex() {
        return mutex;
    }
    
    /**
     * Método que vota a favor la publicación
     * Bloquea la publicación para votar, vota, y se libera. 
     * @param valor "peso" del voto (1 si es normal, 1.5 si es premiado y 2 si es experto en el tema
     * @return true si se pudo hacer correctamente
     * @throws InterruptedException 
     */
    public boolean votarAFavor(double valor, int uuid) throws InterruptedException{
        
        if (Global.publicacionesActivas.contains(this)){
            this.contadorVotosAFavor+= valor;
            this.votantes.add(uuid);
            obtenerPorcentajeAFavor();
            chequeaPublicacion();
        }
        return true;
    }
    
    /**
     * Método que vota en contra la publicación
     * Bloquea la publicación para votar, vota, y se libera. 
     * @param valor "peso" del voto (1 si es normal, 1.5 si es premiado y 2 si es experto en el tema
     * @return true si se pudo hacer correctamente
     * @throws InterruptedException 
     */
    public boolean votarEnContra(double valor, int uuid) throws InterruptedException{
        if (Global.publicacionesActivas.contains(this)){
            this.contadorVotosEnContra+= valor;
            this.votantes.add(uuid);
            obtenerPorcentajeEnContra();
            chequeaPublicacion();
        }
        
        return true;
    }
    
    public void chequeaPublicacion(){
        obtenerActiva();
        if(!this.isActiva()){
            if(this.porcentajeAFavor>0.6){
                try {
                    this.validarPublicacion();
                } catch (InterruptedException ex) {
                    ex.getCause();
                }
            }
            else{
                try {
                    
                    this.anularPublicacion();

                } catch (InterruptedException ex) {
                    ex.getCause();
                }
            }
        }
    }
    
    public void obtenerActiva(){
        if (this.porcentajeAFavor>0.6 || this.porcentajeEnContra>0.4){
            this.activa = false;
        }
    }
    
    public void obtenerPorcentajeAFavor(){
        this.porcentajeAFavor = contadorVotosAFavor / Global.cantidadUsuariosActivos;
    }

    public void obtenerPorcentajeEnContra(){
        this.porcentajeEnContra = contadorVotosEnContra / Global.cantidadUsuariosActivos;
    }
    
    public boolean eliminarPublicacionActiva() throws InterruptedException{
        mutex.acquire();
        System.out.println(this.titulo + " fue bloqueada (eliminar)");
        Usuario us = Global.buscarUsuario(this.usuarioEmisor);
        HiloEliminarPublicacion hilo = new HiloEliminarPublicacion(us, this);
        boolean flag = Global.publicacionesActivas.remove(this);
        mutex.release();
        System.out.println(this.titulo + " fue liberada (eliminar)");
        return flag;
    }
    
    public boolean anularPublicacion() throws InterruptedException{
        //mutex.acquire();
        boolean flag = Global.publicacionesActivas.remove(this);
        for(Usuario u : Global.usuarios){
            if(u.getIdU() == (this.usuarioEmisor)){ 
                bloquearUsuario(u);
                break;
            }
        }
        System.out.println("Publicacion "+this.titulo+" fue anulada tras tener el " + 
                this.porcentajeEnContra*100 + "% de los votos en contra");
        //mutex.release();
        return flag;
    }
    
    public void bloquearUsuario(Usuario usuario){
        try {
            usuario.getSem().acquire();
            usuario.setAnuladas(usuario.getAnuladas() + 1);
            usuario.setPrem(0);
            if(usuario.getAnuladas() == Global.cantidadBloqueado){
                usuario.setBloqueado(true);
                usuario.setPremiado(false);
                System.out.println("EL usuario " + usuario.getIdU() + " ha sido bloqueado");
                    
            }
            usuario.getSem().release();
        
        } catch (InterruptedException ex) {
            ex.getCause();
        }
    }
    
    public boolean validarPublicacion() throws InterruptedException{
        //mutex.acquire(); porque ya esta bloqueado por el votar()
        boolean flag = Global.publicacionesActivas.remove(this);
        if(flag){
            Global.publicacionesPasivas.add(this);
            for(Usuario u : Global.usuarios){
                if(u.getIdU() == (this.usuarioEmisor)){
                    premiarUsuario(u);
                    break;
                }
            }
        }
        System.out.println("Publicacion "+this.titulo+" fue validada tras tener el " + 
                this.porcentajeAFavor*100 + "% de los votos a favor");
        //mutex.release();
        return flag;
    }
    
    public void premiarUsuario(Usuario usuario){
        try {
            usuario.getSem().acquire();
            usuario.setAnuladas(0);
            usuario.setPrem(usuario.getPrem() + 1);
            if(usuario.getPrem() == Global.cantidadPremiado){
                usuario.setPremiado(true);
            }
            usuario.getSem().release();
        } catch (InterruptedException ex) {
            ex.getCause();
        }
    }
    
    @Override
    public String toString(){
        return "*********************************\n"+
                "Titulo: " + this.titulo + "\nTema: " + this.tema + "\n" + this.contenido + "\nEmisor: "+
                this.usuarioEmisor+"\n*********************************";
    }
    
    
    
}
