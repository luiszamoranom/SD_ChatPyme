package servidor;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                DataInputStream inFromClient = new DataInputStream(cliente.getInputStream());
                String initialMessage = inFromClient.readUTF();
                System.out.println(initialMessage);

                //separamos el mensaje inicial para obtener el tipo de usuario
                String[] parts = initialMessage.split(":");
                String tipoUsuario = parts[1];

                HiloDeCliente nuevoCliente = new HiloDeCliente(clientesActivos.size(),tipoUsuario, cliente, clientesActivos);
                clientesActivos.add(nuevoCliente);
                Thread hilo = new Thread(nuevoCliente);
                hilo.start();

                try {
                    nuevoCliente.salidaDatos.writeUTF("Tu thread id es: " + nuevoCliente.getId() +" y tu tipo de usuario es: "+nuevoCliente.getTipoUsuario());
                    nuevoCliente.salidaDatos.writeUTF("Selecciona en el panel lateral izquierdo '*' si deseas enviar un mensaje a todos o el id del thread si deseas enviar un mensaje privado");
                } catch (Exception e) {
                    e.printStackTrace();
                }

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