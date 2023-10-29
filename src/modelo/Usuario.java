package modelo;

public class Usuario {

    public static Usuario INSTANCE;

    private String rut;
    private String nombre_completo;
    private String correo;
    private String contrasena;
    private String tipo_usuario;
    private boolean cambio_contraseña;

    private boolean logueado;

    public Usuario(){}
    

    public static Usuario getInstance(){
        if(INSTANCE==null){
            return new Usuario();
        }
        return INSTANCE;
    } 


    public boolean isLogueado() {
        return this.logueado;
    }

    public boolean getLogueado() {
        return this.logueado;
    }

    public void setLogueado(boolean logueado) {
        this.logueado = logueado;
    }

    
    public String getRut() {
        return this.rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre_completo() {
        return this.nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipo_usuario() {
        return this.tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public boolean isCambio_contraseña() {
        return this.cambio_contraseña;
    }

    public boolean getCambio_contraseña() {
        return this.cambio_contraseña;
    }

    public void setCambio_contraseña(boolean cambio_contraseña) {
        this.cambio_contraseña = cambio_contraseña;
    }


    @Override
    public String toString() {
        return "{" +
            " rut='" + getRut() + "'" +
            ", nombre_completo='" + getNombre_completo() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", contrasena='" + getContrasena() + "'" +
            ", tipo_usuario='" + getTipo_usuario() + "'" +
            ", cambio_contraseña='" + isCambio_contraseña() + "'" +
            ", logueado='" + isLogueado() + "'" +
            "}";
    }


}
