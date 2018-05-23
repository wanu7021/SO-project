/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 *
 * @author federico
 */
public class Publicacion {
    private UUID usuarioEmisor;
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
    private Semaphore mutexGlobal;

    public Publicacion(String titulo, String contenido, UUID emisor, String tema) {
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
        this.mutexGlobal = Global.mutex2;
    }

    public UUID getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(UUID usuarioEmisor) {
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
    
    /**
     * Método que vota a favor la publicación
     * Bloquea la publicación para votar, vota, y se libera. 
     * @param valor "peso" del voto (1 si es normal, 1.5 si es premiado y 2 si es experto en el tema
     * @return true si se pudo hacer correctamente
     * @throws InterruptedException 
     */
    public boolean votarAFavor(double valor, UUID uuid) throws InterruptedException{
        mutexGlobal.acquire();
        mutex.acquire();
        
        if (Global.publicacionesActivas.contains(this)){
            System.out.println(this.titulo + " fue bloqueada (voto a favor) por "+uuid);
            Thread.sleep(5000);
            
            this.contadorVotosAFavor+= valor;
            obtenerPorcentajeAFavor();
            HiloChequeaPublicacion hilo = new HiloChequeaPublicacion(this);
            hilo.run();
            System.out.println(this.titulo + " fue liberada (voto a favor) por "+uuid);
        }
        mutex.release(); //sigue con el mutex bloqueado para evitar que otro usuario quiera votar,
                             //sino puede darse que un usuario vote una publicacion que deberia ser 
                             //validada 
        mutexGlobal.release();
        return true;
    }
    
    /**
     * Método que vota en contra la publicación
     * Bloquea la publicación para votar, vota, y se libera. 
     * @param valor "peso" del voto (1 si es normal, 1.5 si es premiado y 2 si es experto en el tema
     * @return true si se pudo hacer correctamente
     * @throws InterruptedException 
     */
    public boolean votarEnContra(double valor, UUID uuid) throws InterruptedException{
        mutexGlobal.acquire(); //para que no haya nadie votando o eliminando al mismo tiempo
        mutex.acquire();
        if (Global.publicacionesActivas.contains(this)){
            System.out.println(this.titulo + " fue bloqueada (voto en contra) por "+uuid);
            Thread.sleep(5000);
            this.contadorVotosEnContra+= valor;
            obtenerPorcentajeEnContra();
            HiloChequeaPublicacion hilo = new HiloChequeaPublicacion(this);
            hilo.run();
            System.out.println(this.titulo + " fue liberada (voto en contra) por "+uuid);
        }
        mutex.release();//sigue con el mutex bloqueado para evitar que otro usuario quiera votar,
                         //sino puede darse que un usuario vote una publicacion que deberia ser 
                         //anulada
        mutexGlobal.release();
        
        return true;
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
        HiloEliminarPublicacion hilo = new HiloEliminarPublicacion(this.usuarioEmisor, this, 5000, this.mutex);
        boolean flag = Global.publicacionesActivas.remove(this);
        mutex.release();
        System.out.println(this.titulo + " fue liberada (eliminar)");
        return flag;
    }
    
    public boolean anularPublicacion() throws InterruptedException{
        //mutex.acquire();
        boolean flag = Global.publicacionesActivas.remove(this);
        for(Usuario u : Global.usuarios){
            if(u.getIdU().equals(this.usuarioEmisor)){ 
                HiloBloqueaUsuario hiloB = new HiloBloqueaUsuario(u, u.getSem());
                hiloB.setPriority(Thread.MAX_PRIORITY);
                hiloB.start();
                break;
            }
        }
        System.out.println("Publicacion "+this.titulo+" fue anulada tras tener el " + 
                this.porcentajeEnContra*100 + "% de los votos en contra");
        //mutex.release();
        return flag;
    }
    
    public boolean validarPublicacion() throws InterruptedException{
        //mutex.acquire(); porque ya esta bloqueado por el votar()
        boolean flag = Global.publicacionesActivas.remove(this);
        if(flag){
            Global.publicacionesPasivas.add(this);
            for(Usuario u : Global.usuarios){
                if(u.getIdU().equals(this.usuarioEmisor)){
                    HiloPremiaUsuario hiloP = new HiloPremiaUsuario(u);
                    hiloP.setPriority(5);
                    break;
                }
            }
        }
        System.out.println("Publicacion "+this.titulo+" fue validada tras tener el " + 
                this.porcentajeAFavor*100 + "% de los votos a favor");
        //mutex.release();
        return flag;
    }
    
    @Override
    public String toString(){
        return "*********************************\n"+
                "Titulo: " + this.titulo + "\nTema: " + this.tema + "\n" + this.contenido + "\nEmisor: "+
                this.usuarioEmisor+"\n*********************************";
    }
    
    
    
}
