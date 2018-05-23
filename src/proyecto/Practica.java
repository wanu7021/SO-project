/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

/**
 *
 * @author federico
 */
public class Practica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        /*Thread procesoUno = new ProcesoUno();
        Thread procesoDos = new Thread();
        Thread principal = new ProgramaPrincipal();*/
        ProgramaPrincipal p = new ProgramaPrincipal();
        //Global g = new Global();
        p.cargar();
        Usuario u1 = Global.usuariosActivos.get(0);
        Usuario u2 = Global.usuariosActivos.get(1);
        Usuario u3 = Global.usuariosActivos.get(2);
        Usuario u4 = Global.usuariosActivos.get(3);
        
        System.out.println("U1 id: "+u1.getIdU()+"\nU2 id: "+u2.getIdU() + "\nU3 id: " + u3.getIdU());
        /*
        u2.votarPublicacion(Global.publicacionesActivas.get(0), false);
        u3.votarPublicacion(Global.publicacionesActivas.get(0), false);
        
        u4.votarPublicacion(Global.publicacionesActivas.get(0), true);
        u1.votarPublicacion(Global.publicacionesActivas.get(0), true);
        */
        //CASO 1:
        /*
        System.out.println("***********CASO 1***********");
        u1.votarPublicacion(Global.publicacionesActivas.get(1), true); 
        u2.votarPublicacion(Global.publicacionesActivas.get(1), false);
        System.out.println("***********FINALIZA CASO 1***********");
        System.out.println("***********CASO 2***********");
        u3.votarPublicacion(Global.publicacionesActivas.get(1), false); //comun
        u2.votarPublicacion(Global.publicacionesActivas.get(1), false); //experto
        System.out.println("***********FINALIZA CASO 2***********");
        System.out.println("***********CASO 4***********");
        u1.votarPublicacion(Global.publicacionesActivas.get(1), true); 
        u2.votarPublicacion(Global.publicacionesActivas.get(1), true);
        //el sistema valida luego de que u1 y u2 votan a favor, u3 no deberia poder votar
        u3.votarPublicacion(Global.publicacionesActivas.get(1), true);
        System.out.println("***********FINALIZA CASO 4***********");
        System.out.println("***********CASO 5**********");
        u1.setAnuladas(2);
        u2.votarPublicacion(Global.publicacionesActivas.get(0), false); //anula
        Thread.sleep(5005);
        u1.votarPublicacion(Global.publicacionesActivas.get(1), true);
        System.out.println("***********FINALIZA CASO 5***********");*/
        System.out.println("***********CASO 6**********");
        u1.eliminarPublicacion(Global.publicacionesActivas.get(0)); //chequear que sea el autor
        //Thread.sleep(6000);
        u2.votarPublicacion(Global.publicacionesActivas.get(0), true);
        
    }
    
}
