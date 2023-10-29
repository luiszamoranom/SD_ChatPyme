package cliente;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import modelo.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
*
* @author RPVZ
*/
public class ClienteChat {
    private Usuario usuario;
    
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
            
            usuario = Usuario.getInstance();

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

                                usuario.setTipo_usuario("Administrador");

                                System.out.println("Se logueará como admin");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                usuario.setRut(rut);
                                usuario.setLogueado(true);
                                ejecutarInterfazAdmin();
                                

                            } else if (rut.startsWith("10")) {
                                Usuario.getInstance().setTipo_usuario("Medico");
                                Usuario.getInstance().setRut(rut);

                                usuario.setTipo_usuario("Medico");
                                System.out.println("Se logueará como medico");
                                usuario.setRut(rut);
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                usuario.setLogueado(true);

                                try {
                                    ejecutarInterfaz("medico");
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                            } else if (rut.startsWith("20")) {
                                usuario.setTipo_usuario("Admision");

                                System.out.println("Se logueará como admision");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                usuario.setRut(rut);
                                try {
                                    ejecutarInterfaz("admision");
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                // Redireccionar a vista admision

                            } else if (rut.startsWith("30")) {
                                usuario.setTipo_usuario("Auxiliar");
                                System.out.println("Se logueará como auxiliar");
                                usuario.setRut(rut);
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                usuario.setLogueado(true);

                                try {
                                    ejecutarInterfaz("auxiliar");
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                            } else if (rut.startsWith("40")) {
                                usuario.setTipo_usuario("Examenes");

                                System.out.println("Se logueará como examenes");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                try {
                                    ejecutarInterfaz("examenes");
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                // Redireccionar a vista examenes

                            } else if (rut.startsWith("50")) {  
                                usuario.setTipo_usuario("Pabellon");

                                System.out.println("Se logueará como pabellon");
                                mensajeRutInvalido.setVisible(false);
                                marco.setVisible(false);
                                try {
                                    ejecutarInterfaz("pabellon");
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
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
                        abrirVentanaCambioContrasena(rut, false, marco);
                    }
                }
            });
            
        }

        private void abrirVentanaCambioContrasena(String rut, Boolean admin, JFrame marco) {
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
            //si el usuario no es admin, el campo de rut no se puede editar
            if(!admin){
                campoRut.setText(rut);
                campoRut.setEditable(false);
            }

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
                            //volver al marco anterior
                            marco.setVisible(true);
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

        public void ejecutarInterfaz(String rol) throws IOException{
            this.logueado=true;
            this.tipo_usuario=rol;
            crearInterfazGrafica();
            salidaDatos.writeUTF("Tu thread id es: " + usuario.getRut() +" y tu tipo de usuario es: "+usuario.getTipo_usuario());
            salidaDatos.writeUTF("Selecciona en el panel lateral izquierdo '*' si deseas enviar un mensaje a todos o el id del thread si deseas enviar un mensaje privado");
            escucharMensajes();
        }


        //crea una interfaz para el administrador, contiene cajas para la muestra de estadisticas y 2 botones para la creacion de usuarios y la modificacion de contraseñas
        private void ejecutarInterfazAdmin() {
            JFrame marco = new JFrame("Administrador");
            marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            marco.setSize(1000, 500);
        
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
        
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);

           //agrega una caja para mostrar estadisticas
            JPanel panelEstadisticas = new JPanel();
            panelEstadisticas.setLayout(new GridBagLayout());
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(panelEstadisticas, c);

            //agrega estadisticas de prueba, los casos son cuantos mensajes por grupo se han enviado
            JLabel etiquetaEstadisticas = new JLabel("Estadísticas");
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panelEstadisticas.add(etiquetaEstadisticas, c);

            //PRUEBA DE CONCEPTO DE COMO SE PODRIAN VER LAS ESTADISTICAS, NO ES NECESARIO QUE SE VEA ASI
            Box cajaEstadisticas = Box.createHorizontalBox();
            cajaEstadisticas.add(new JLabel("Doctores: 1000"));
            cajaEstadisticas.add(Box.createHorizontalStrut(10));
            cajaEstadisticas.add(new JLabel("Auxiliares: 1000"));
            cajaEstadisticas.add(Box.createHorizontalStrut(10));
            cajaEstadisticas.add(new JLabel("Admisión: 1000"));
            cajaEstadisticas.add(Box.createHorizontalStrut(10));
            cajaEstadisticas.add(new JLabel("Exámenes: 1000"));
            cajaEstadisticas.add(Box.createHorizontalStrut(10));
            cajaEstadisticas.add(new JLabel("Pabellón: 1000"));
            cajaEstadisticas.add(Box.createHorizontalStrut(10));
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panelEstadisticas.add(cajaEstadisticas, c);

            //borde con puntos y titulo para separar las estadisticas de los botones
            Border borde = BorderFactory.createDashedBorder(Color.BLACK, 1, 5, 5, false);
            TitledBorder titulo = BorderFactory.createTitledBorder(borde, "Estadísticas");
            titulo.setTitleJustification(TitledBorder.CENTER);
            panelEstadisticas.setBorder(titulo);
            

            //agrega 2 botones para crear usuarios y modificar contraseñas
            JButton botonCrearUsuario = new JButton("Crear Usuario");
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(botonCrearUsuario, c);

            JButton botonModificarContrasena = new JButton("Modificar Contraseña");
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(botonModificarContrasena, c);

            marco.add(panel);
            marco.setLocationRelativeTo(null);
            marco.setVisible(true);
        
            botonCrearUsuario.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    marco.setVisible(false);
                    ejecutarInterfazCrearUsuario(marco);
                }
            });
        
            botonModificarContrasena.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    marco.setVisible(false);
                    abrirVentanaCambioContrasena("", true, marco);
                }
            });
        }

        //interfaz para la creacion de usuarios, contiene cajas para ingresar los datos del usuario, rut, nombre, correo y combo box para seleccionar el tipo de usuario
        private void ejecutarInterfazCrearUsuario(JFrame admin) {
            JFrame marco = new JFrame("Crear Usuario");
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
        
            JLabel etiquetaNombre = new JLabel("Nombre:");
            c.gridx = 0;
            c.gridy = 1;
            panel.add(etiquetaNombre, c);
        
            JTextField campoNombre = new JTextField(20);
            c.gridx = 1;
            c.gridy = 1;
            panel.add(campoNombre, c);
        
            JLabel etiquetaCorreo = new JLabel("Correo:");
            c.gridx = 0;
            c.gridy = 2;
            panel.add(etiquetaCorreo, c);
        
            JTextField campoCorreo = new JTextField(20);
            c.gridx = 1;
            c.gridy = 2;
            panel.add(campoCorreo, c);
        
            JLabel etiquetaTipoUsuario = new JLabel("Tipo de Usuario:");
            c.gridx = 0;
            c.gridy = 3;
            panel.add(etiquetaTipoUsuario, c);
        
            JComboBox<Object> cajaSeleccion = new JComboBox<Object>();
            cajaSeleccion.addItem("Médico");
            cajaSeleccion.addItem("Admisión");
            cajaSeleccion.addItem("Auxiliar");
            cajaSeleccion.addItem("Exámenes");
            cajaSeleccion.addItem("Pabellón");
            c.gridx = 1;
            c.gridy = 3;
            panel.add(cajaSeleccion, c);
        
            JButton botonCrearUsuario = new JButton("Crear Usuario");
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;

            panel.add(botonCrearUsuario, c);

            JLabel mensajeRutInvalido = new JLabel("RUT inválido o ya existe");
            mensajeRutInvalido.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 5;
            panel.add(mensajeRutInvalido, c);
            mensajeRutInvalido.setVisible(false);

            JLabel mensajeCamposVacios = new JLabel("Debe completar todos los campos");
            mensajeCamposVacios.setForeground(Color.RED); // Color rojo para resaltar el mensaje
            c.gridx = 0;
            c.gridy = 5;
            panel.add(mensajeCamposVacios, c);
            mensajeCamposVacios.setVisible(false);

            //se agrega un boton para volver a la interfaz de administrador
            JButton botonVolver = new JButton("Volver");
            c.gridx = 0;
            c.gridy = 6;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(botonVolver, c);

        

            marco.add(panel);
            marco.setLocationRelativeTo(null);
            marco.setVisible(true);


            botonCrearUsuario.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String rut = campoRut.getText();
                    String nombre = campoNombre.getText();
                    String correo = campoCorreo.getText();
                    String tipoUsuario = cajaSeleccion.getSelectedItem().toString();
                    mensajeRutInvalido.setVisible(false);
                    mensajeCamposVacios.setVisible(false);
                    if (rut.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
                        // Al menos uno de los campos está vacío
                        mensajeCamposVacios.setVisible(true);
                    } else {
                        if(validarRut(rut)){//se debe agregar la validacion de que el rut no exista en la base de datos
                            System.out.println("Se creará el usuario");//aqui se hara la creacion del usuario
                            
                            //FALTA LOGICA PARA CREAR USUARIO EN LA BASE DE DATOS

                            admin.setVisible(true);//se hara visible la interfaz de administrador nuevamente
                            marco.setVisible(false);
                        }
                        else{
                            mensajeRutInvalido.setVisible(true);
                        }
                    }
                }
            });

            botonVolver.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    admin.setVisible(true);
                    marco.setVisible(false);
                }
            });
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
        if(usuario.getTipo_usuario().equals("Medico")){
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
                                if(usuario.getTipo_usuario().equals("Medico")){
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
