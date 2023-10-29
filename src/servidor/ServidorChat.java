package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelo.Usuario;

/**
*
@author RPVZ
*/
public class ServidorChat {
    private static List<HiloDeCliente> clientesActivos = new ArrayList<>();

    public static void main(String[] args) {
        new ServidorChat();
    }

    public ServidorChat() {
        try {
            ServerSocket socketServidor = new ServerSocket(5000);
            while (true) {
                Socket cliente = socketServidor.accept();

 
                String randomRol="borrar";

                HiloDeCliente nuevoCliente = new HiloDeCliente(clientesActivos.size(),randomRol, cliente, clientesActivos);
                clientesActivos.add(nuevoCliente);
                Thread hilo = new Thread(nuevoCliente);
                hilo.start();

                /*
                try {
                    System.out.println("Escribió los datos");
                    nuevoCliente.salidaDatos.writeUTF("Tu thread id es: " + nuevoCliente.getId() +" y tu tipo de usuario es: "+nuevoCliente.getTipoUsuario());
                    nuevoCliente.salidaDatos.writeUTF("Selecciona en el panel lateral izquierdo '*' si deseas enviar un mensaje a todos o el id del thread si deseas enviar un mensaje privado");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */

                actualizarListaClientes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarListaClientes() {
        StringBuilder ids = new StringBuilder("/actualizar");
        for (HiloDeCliente cliente : clientesActivos) {
            ids.append(" ").append(cliente.getId());
        }
        String mensaje = ids.toString();
        for (HiloDeCliente cliente : clientesActivos) {
            try {
                cliente.salidaDatos.writeUTF(mensaje);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}