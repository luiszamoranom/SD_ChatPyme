package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
*
* @author RPVZ
*/
public class ClienteChat {
    private Socket socket;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    private JTextArea areaTexto;
    private JTextField campoTexto;
    private JComboBox<Object> cajaSeleccion;
    private boolean logueado=false;
    private String tipo_usuario ="";

    public static void main(String[] args) {
        new ClienteChat();
    }

    public ClienteChat() {
        try {
            socket = new Socket("localhost", 5000);
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
            crearInterfazLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearInterfazLogin() {
        JFrame marco = new JFrame("Login");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1000, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
       
        /* OMAR
         * Quiero dos campos que sea para rut y contraseña y
         * si el rut empieza con:
         *  00 => derivar a vista administrador
         *  10 => derivar a vista medico
         *  20 => ad
         *  30 => ax
         *  40 => ex
         *  50 => pab
         */

         /* OMAR
          * Necesito que si el usaurio no tiene la contraseña cambiada, te tire a la vista de cambiar contraseña (crear),
          para ello podrian poner un checkbox en el login de modo que si esta seleccionado, se loguea como si ya tuviera
          la contraseña cambiada, en caso contrario, debería redireccionar a a cambiar la contraseña
          */

          /* DIEGO
           * Si se loguea como administrador, hay que hacer la vista de administrador (con metricas)
           * boton Registrar => hacer vista
           * boton Cambiar contraseña => hacer vista
           */
        JButton botonAdmin = new JButton("Admin");
        JButton botonInvitado = new JButton("Invitado");

   
        panel.add(botonAdmin, BorderLayout.WEST);
        panel.add(botonInvitado, BorderLayout.EAST);
        marco.add(panel);
        
        marco.setVisible(true);
        botonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marco.setVisible(false);
                ejecutarInterfaz();
            }
        });
    }

    public void ejecutarInterfaz(){
        this.logueado=true;
        this.tipo_usuario="admin";
        crearInterfazGrafica();
        escucharMensajes();
    }



    private void crearInterfazGrafica() {
        JFrame marco = new JFrame("Cliente Chat");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1000, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane panelDesplazamiento = new JScrollPane(areaTexto);

        campoTexto = new JTextField();
        JButton botonEnviar = new JButton("Enviar");

        cajaSeleccion = new JComboBox<Object>();  
        cajaSeleccion.addItem("*");   
        if(tipo_usuario=="admin"){
            cajaSeleccion.addItem("doctores");
            cajaSeleccion.addItem("auxiliares");
            cajaSeleccion.addItem("admision"); 
            cajaSeleccion.addItem("examenes");
            cajaSeleccion.addItem("pabellon"); 
        } 
            

        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object elementoSeleccionado = cajaSeleccion.getSelectedItem();
                    if (elementoSeleccionado != null) {
                        String mensaje = campoTexto.getText();
                        if (elementoSeleccionado instanceof String && "*".equals(elementoSeleccionado.toString())) {
                            salidaDatos.writeUTF(mensaje);
                        }else if (elementoSeleccionado instanceof String && "auxiliares".equals(elementoSeleccionado.toString())) {
                             salidaDatos.writeUTF("/auxiliares " + mensaje);
                        }else if (elementoSeleccionado instanceof Integer) {
                            int idDestino = (int) elementoSeleccionado;
                            salidaDatos.writeUTF("/privado " + idDestino + " " + mensaje);
                        }
                        campoTexto.setText("");
                    } else {
                        System.out.println("No hay destinatarios disponibles.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(panelDesplazamiento, BorderLayout.CENTER);
        panel.add(campoTexto, BorderLayout.NORTH);
        panel.add(botonEnviar, BorderLayout.EAST);
        panel.add(cajaSeleccion, BorderLayout.WEST);

        marco.add(panel);
        marco.setVisible(true);
    }

    private void escucharMensajes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(logueado){
                    try {
                        while (true) {
                            String mensaje = entradaDatos.readUTF();
                            if (mensaje.startsWith("/actualizar")) {
                                String[] ids = mensaje.split(" ");
                                cajaSeleccion.removeAllItems();
                                cajaSeleccion.addItem("*"); 
                                if(tipo_usuario=="admin"){
                                     cajaSeleccion.addItem("doctores");
                                    cajaSeleccion.addItem("auxiliares");
                                    cajaSeleccion.addItem("admision"); 
                                    cajaSeleccion.addItem("examenes");
                                    cajaSeleccion.addItem("pabellon"); 
                                } 
                                for (int i = 1; i < ids.length; i++) {
                                    cajaSeleccion.addItem(Integer.parseInt(ids[i]));
                                }
                            } else {
                                areaTexto.append(mensaje + "\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
