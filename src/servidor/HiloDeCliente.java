package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import modelo.Usuario;

/**
*
* @author RPVZ
*/
public class HiloDeCliente implements Runnable {
    private Usuario usuario;

    private int id;
    private Socket socket;
    private DataInputStream entradaDatos;
    DataOutputStream salidaDatos;
    private List<HiloDeCliente> clientesActivos;

    public HiloDeCliente(int id, String tipo_usuario, Socket socket, List<HiloDeCliente> clientesActivos) {
        this.id = id;
        this.socket = socket;
        usuario = Usuario.getInstance();
        this.clientesActivos = clientesActivos;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void run() {

            try {
                while (true) {
                    System.out.println(Usuario.getInstance().toString());
                    String texto = entradaDatos.readUTF();
                    if (texto.startsWith("/privado")) {
                        String[] partes = texto.split(" ", 3);
                        int idDestino = Integer.parseInt(partes[1]);
                        String mensajePrivado = partes[2];
                        enviarMensajePrivado(idDestino, mensajePrivado);
                    } else if(texto.startsWith("/auxiliares")){
                        String[] partes = texto.split(" ", 2);
                        String mensajePrivado = partes[1];
                        enviarMensajeAuxiliares(mensajePrivado);
                    }else {
                        enviarMensajeATodos(texto);
                    }
                }
            } catch (SocketException e) {
                System.out.println("Cliente con ID " + this.id + " se ha desconectado.");
                clientesActivos.remove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        
    }

    public void enviarMensajePrivado(int idDestino, String mensaje) {
        for (HiloDeCliente cliente : clientesActivos) {
            if (cliente.id == idDestino) {
                try {
                    cliente.salidaDatos.writeUTF("Mensaje privado de " + this.id + ": " + mensaje);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }else if(cliente.equals(this)){
                try {
                    cliente.salidaDatos.writeUTF("Mensaje de ti a "+this.id+": " + mensaje);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void enviarMensajeAuxiliares(String mensaje) {
        for (HiloDeCliente cliente : clientesActivos) {
            if (usuario.getTipo_usuario().equals("Auxiliar") ) {
                try {
                    cliente.salidaDatos.writeUTF("Mensaje a auxiliares de " +usuario.getTipo_usuario()+" "+ this.id + ": " + mensaje);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(cliente.equals(this)){
                try {
                    cliente.salidaDatos.writeUTF("Mensaje de ti a auxiliares: " + mensaje);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void enviarMensajeATodos(String mensaje) {
        for (HiloDeCliente cliente : clientesActivos) {
            try {
                if(cliente.equals(this)){
                    cliente.salidaDatos.writeUTF("Mensaje de ti  a todos/as: " + mensaje);
                }else{
                    cliente.salidaDatos.writeUTF("Mensaje de " + this.id + " a todos/as: " + mensaje);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}