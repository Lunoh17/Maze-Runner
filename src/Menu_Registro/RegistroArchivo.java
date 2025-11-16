package Menu_Registro;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public abstract class RegistroArchivo {
    private static final String nombreArchivo = "Registro-Login.txt";

    public void GuardarArchivo(String palabra){
        String[] partes = palabra.split(":");
        if (partes.length != 2) return;
        String usuario = partes[0];
        String contrasenia = partes[1];
        String datosGuardar;
        try {
            String contraseniaCifrada = AESCifrado.Cifrado(contrasenia);
            datosGuardar = usuario+":"+contraseniaCifrada;

            FileWriter archivo = new FileWriter(nombreArchivo,true);
            PrintWriter out = new PrintWriter(archivo);
            out.println(datosGuardar);
            out.close();

        } catch (Exception e){
            System.out.println("Ocurrio un error al agregar el Usuario/Contraseña");
            e.printStackTrace();
        }
    }

    public boolean BuscarArchivoRegistro(String usuario) {
        try {
            File archivo = new File(nombreArchivo);
            Scanner leer = new Scanner(archivo);
            leer.useDelimiter(Pattern.compile(":|\r\n|\n"));
            while(leer.hasNext()){
                String AuxUsuario = leer.next().trim();
                if(leer.hasNext()){
                    String AuxContrasenia = leer.next().trim();
                    if(AuxUsuario.equals(usuario)){
                        leer.close();
                        System.out.println("Este Usuario Ya se encuentra Registrado");
                        return true;
                    }
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("❌Ocurrio un error al verificar el Usuario/Contraseña");
            e.printStackTrace();
            return false;
        }
    }

    public boolean BuscarArchivoSesion(String usuario, String contrasenia){
        try{
            File archivo = new File(nombreArchivo);
            Scanner leer = new Scanner(archivo);
            while(leer.hasNext()){
                String linea = leer.nextLine().trim();
                if (linea.isEmpty()) continue;
                String [] partes = linea.split(":");
                if (partes.length != 2) continue;
                String AuxUsuario = partes[0].trim();
                String AuxContraseniaCifrada = partes[1].trim();
                if(AuxUsuario.equals(usuario)){
                    String AuxContraseniaDescifrada = AESCifrado.Descifrado(AuxContraseniaCifrada);
                    if(AuxContraseniaDescifrada.equals(contrasenia)){
                        leer.close();
                        return true;
                    }else{
                        leer.close();
                        Scanner leeropc = new Scanner(System.in);
                        System.out.println("Contraseña Incorrecta");
                        System.out.println("Deseas Recuperar Contraseña");
                        System.out.println("1. Si");
                        System.out.println("0. NO");
                        int opc;
                        opc = leeropc.nextInt();
                        if(opc == 1){
                            System.out.println("Recuperacion de Usuario:Contraseña "+AuxUsuario+":"+AuxContraseniaDescifrada);
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }
            return false;
        }catch (FileNotFoundException e) {
            System.out.println("❌Ocurrio un error al verificar el Usuario/Contraseña");
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            System.out.println("❌ERROR al descifrar los datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public boolean BuscarArchivoContrasenia(String usuario) {
        try {
            File archivo = new File(nombreArchivo);
            Scanner leer = new Scanner(archivo);
            while(leer.hasNext()){
                String linea = leer.nextLine().trim();
                if (linea.isEmpty()) continue;
                String [] partes = linea.split(":");
                if (partes.length != 2) continue;
                String AuxUsuario = partes[0].trim();
                String AuxContraseniaCifrada = partes[1].trim();
                if(AuxUsuario.equals(usuario)){
                    String AuxContraseniaDescifrada = AESCifrado.Descifrado(AuxContraseniaCifrada);
                    leer.close();
                    System.out.println("La Contraseña del Usuario "+AuxUsuario+"= "+AuxContraseniaDescifrada);
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("❌Ocurrio un error al verificar el Usuario/Contraseña");
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            System.out.println("❌ERROR al descifrar los datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
