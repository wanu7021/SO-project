/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author federico
 */
public class ProgramaPrincipal extends Thread{
    
    
    public void cargar(){
        String[] archivoU = ManejadorArchivosGenerico.leerArchivo("./src/proyecto/usuarios.txt", false);
        String[] archivoP = ManejadorArchivosGenerico.leerArchivo("./src/proyecto/publicaciones.txt", false);
        for(String linea : archivoU){
            String[] split = linea.split(";");
            String nom = split[0];
            int tel = Integer.parseInt(split[2]);
            String dir = split[1];
            String[] exp = split[3].split(",");
            ArrayList l = new ArrayList();
            for(String c : exp){
                l.add(c);
            }
            Usuario usuario = new Usuario(nom, dir, tel, l);
            Global.usuarios.add(usuario);
            Global.usuariosActivos.add(usuario);
            Global.cantidadUsuariosActivos++;
        }
        
        int i = 0;
        for(String linea : archivoP){
            
            String[] split = linea.split(";");
            String titulo = split[0];
            String contenido = split[1];
            String tema = split[2];
            UUID emisor = Global.usuariosActivos.get(i++).getIdU();
            Publicacion p = new Publicacion(titulo, contenido, emisor, tema);
            Global.publicacionesActivas.add(p);
            
        }
        
        
    }
    
    
}
