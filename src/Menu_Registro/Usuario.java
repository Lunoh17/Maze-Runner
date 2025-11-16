package Menu_Registro;

public class Usuario{
    private String correo;
    private static final String Email = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String Email2 = "^[A-Za-z0-9._%+-]+@$";

    public Usuario(){}

    public Usuario(String correo) {
        if(UsuaValidacion(correo) != null) {
            this.correo = UsuaValidacion(correo);
        }
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String UsuaValidacion(String correo){
        if(correo != null){
            if(correo.matches(Email)){
                return correo;
            }else if(correo.matches(Email2)){
                int indice;
                indice = correo.indexOf('@');
                String usuario = correo.substring(0,indice);
                correo = usuario + "@email.com";
                return correo;
            }else{
                System.out.println("❌ERROR Correo no valido, debe contener una dirrecion de email valida o contener un @");
                return null;
            }
        }else{
            System.out.println("❌ERROR el Correo no puede ser NULL");
            return null;
        }
    }
}
