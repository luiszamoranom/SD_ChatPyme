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


          private void crearInterfazLogin() {
            JFrame marco = new JFrame("Login");
            marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            marco.setSize(1000, 500);
        
            JPanel panel = new JPanel();
            
            panel.setLayout(new GridBagLayout());
            
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
        
            JLabel etiquetaRut = new JLabel("RUT:");
            c.gridx = 0;
            c.gridy = 0;
            panel.add(etiquetaRut, c);
        
            JTextField campoRut = new JTextField(20);
            c.gridx = 1;
            c.gridy = 0;
            panel.add(campoRut, c);
            
            JLabel ejemploRut = new JLabel("Ejemplo: 12.345.678-9");
            c.gridx = 2;
            c.gridy = 0;
            panel.add(ejemploRut, c);
        
            JLabel etiquetaContrasena = new JLabel("Contraseña:");
            c.gridx = 0;
            c.gridy = 1;
            panel.add(etiquetaContrasena, c);
        
            JPasswordField campoContrasena = new JPasswordField(20);
            c.gridx = 1;
            c.gridy = 1;
            panel.add(campoContrasena, c);
        
            JButton botonIniciarSesion = new JButton("Iniciar Sesión");
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(botonIniciarSesion, c);

            JCheckBox checkBox = new JCheckBox("Constraseña cambiada");
            c.gridx = 2;
            c.gridy = 2; // Ubicado en la misma fila que el botón
            panel.add(checkBox, c);
            checkBox.setSelected(true); 

            JLabel mensajeRutInvalido = new JLabel("RUT inválido,no existe o contraseña incorrecta");
            mensajeRutInvalido.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 3;
            panel.add(mensajeRutInvalido, c);
            mensajeRutInvalido.setVisible(false);
        
            marco.add(panel);
            marco.setLocationRelativeTo(null);
            marco.setVisible(true);
        
            botonIniciarSesion.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String rut = campoRut.getText();

                    char[] contrasena = campoContrasena.getPassword();
                    
                    if (checkBox.isSelected()){
                        if (validarRut(rut)) {
                            if (rut.startsWith("00")) {//habria que implementar las validaciones para ver si la cuenta existe o no y que su  contraseña sea la correcta
                                // Redireccionar a vista administrador
                                System.out.println("Se logueará como admin");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("admin");

                            } else if (rut.startsWith("10")) {
                                System.out.println("Se logueará como medico");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("medico");
                                // Redireccionar a vista medico

                            } else if (rut.startsWith("20")) {
                                System.out.println("Se logueará como admision");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("admision");
                                // Redireccionar a vista admision

                            } else if (rut.startsWith("30")) {
                                System.out.println("Se logueará como auxiliar");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("auxiliar");
                                // Redireccionar a vista auxiliar

                            } else if (rut.startsWith("40")) {
                                System.out.println("Se logueará como examenes");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("examenes");
                                // Redireccionar a vista examenes

                            } else if (rut.startsWith("50")) {  
                                System.out.println("Se logueará como pabellon");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                ejecutarInterfaz("pabellon");
                                // Redireccionar a vista pabellon   

                            } else  {
                                mensajeRutInvalido.setVisible(true);
                            }
                        }
                        else{
                            mensajeRutInvalido.setVisible(true);
                        }
                    
                    }
                    else{
                        marco.setVisible(false);
                        abrirVentanaCambioContrasena(rut);
                    }
                }
            });
            
        }

        private void abrirVentanaCambioContrasena(String rut) {
            JFrame marcoCambioContrasena = new JFrame("Cambiar Contraseña");
            marcoCambioContrasena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            marcoCambioContrasena.setSize(1000, 500);
            JPanel panelCambioContrasena = new JPanel();
            panelCambioContrasena.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
        
            JLabel etiquetaRut = new JLabel("RUT:");
            c.gridx = 0;
            c.gridy = 0;
            panelCambioContrasena.add(etiquetaRut, c);
        
            JTextField campoRut = new JTextField(20);
            c.gridx = 1;
            c.gridy = 0;
            panelCambioContrasena.add(campoRut, c);
        
            JLabel ejemploRut = new JLabel("Ejemplo: 12.345.678-9");
            c.gridx = 2;
            c.gridy = 0;
            panelCambioContrasena.add(ejemploRut, c);
        
            JLabel etiquetaContrasena1 = new JLabel("Nueva Contraseña:");
            c.gridx = 0;
            c.gridy = 1;
            panelCambioContrasena.add(etiquetaContrasena1, c);
        
            JPasswordField campoContrasena1 = new JPasswordField(20);
            c.gridx = 1;
            c.gridy = 1;
            panelCambioContrasena.add(campoContrasena1, c);
        
            JLabel etiquetaContrasena2 = new JLabel("Confirmar Contraseña:"); // Cambié el nombre de la etiqueta
            c.gridx = 0;
            c.gridy = 2; 
            panelCambioContrasena.add(etiquetaContrasena2, c);
        
            JPasswordField campoContrasena2 = new JPasswordField(20);
            c.gridx = 1;
            c.gridy = 2; 
            panelCambioContrasena.add(campoContrasena2, c);
        
            JButton botonCambiarContraseña = new JButton("Cambiar Contraseña"); // Cambié el texto del botón
            c.gridx = 0;
            c.gridy = 3; 
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panelCambioContrasena.add(botonCambiarContraseña, c);

            JLabel mensajeRutInvalido = new JLabel("RUT inválido o no existe");
            mensajeRutInvalido.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 4;
            panelCambioContrasena.add(mensajeRutInvalido, c);
            mensajeRutInvalido.setVisible(false);
        
            JLabel mensajeContraseñasInvalidas = new JLabel("Debe completar ambos campos de contraseña");
            mensajeContraseñasInvalidas.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 4;
            panelCambioContrasena.add(mensajeContraseñasInvalidas, c);
            mensajeContraseñasInvalidas.setVisible(false);

            JLabel mensajeContraseñasNoCoinciden = new JLabel("Las contraseñas no coinciden");
            mensajeContraseñasNoCoinciden.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 4;
            panelCambioContrasena.add(mensajeContraseñasNoCoinciden, c);
            mensajeContraseñasNoCoinciden.setVisible(false);

            marcoCambioContrasena.add(panelCambioContrasena);
            marcoCambioContrasena.setLocationRelativeTo(null);
            marcoCambioContrasena.setVisible(true);

            botonCambiarContraseña.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nuevaContrasena1 = new String(campoContrasena1.getPassword());
                    String nuevaContrasena2 = new String(campoContrasena2.getPassword());
                   String rut = campoRut.getText();
                    mensajeContraseñasNoCoinciden.setVisible(false);
                    mensajeContraseñasInvalidas.setVisible(false);
                    mensajeRutInvalido.setVisible(false);
                    
                    if (nuevaContrasena1.isEmpty() || nuevaContrasena2.isEmpty()) {
                        // Al menos uno de los campos de contraseña está vacío
                        mensajeContraseñasInvalidas.setVisible(true);
                    } else if (!nuevaContrasena1.equals(nuevaContrasena2)) {
                        // Las contraseñas no coinciden
                        mensajeContraseñasNoCoinciden.setVisible(true);
                    } else {
                        if(validarRut(rut)){//se debe agregar la validacion de que el rut exista en la base de datos
                            System.out.println("Se cambiará la contraseña");//aqui se hara el cambio de contraseña y se redireccionara al login nuevamente
                            crearInterfazLogin();
                            marcoCambioContrasena.setVisible(false);
                        }
                        else{
                            mensajeRutInvalido.setVisible(true);
                        }
                    }
                }
            });
        }
        private boolean validarRut(String rut) {
            String regex = "^\\d{1,3}(\\.\\d{3})*-\\d|k|K$"; // Acepta formato con puntos y guión
            return rut.matches(regex);
        }
        public void ejecutarInterfaz(String rol){
        this.logueado=true;
        this.tipo_usuario=rol;
        crearInterfazGrafica();
        escucharMensajes();
        }
    
        
    
    /*private void crearInterfazLogin() {
        JFrame marco = new JFrame("Login");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1000, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
       
     
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
    }*/





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
        marco.setLocationRelativeTo(null);
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
