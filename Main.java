package encryptdecrypt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        int argsQuantity = args.length; // cantidad de argumentos ingresados;

        // Se incializan las variables de control para los argumentos de entrada
            int key = 0;                // clave
            boolean hKey = false;       // hay clave: false
            String mode = "";           // modalidad
            boolean hMode = false;      // hay modalidad: false
            String data = "";           // datos
            boolean hData = false;      // hay datos: false
            String fileIn = "";         // nombre y ruta del archivo de entrada
            boolean hIn = false;        // hay archivo de entrada: false
            String fileOut = "";        // nombre y ruta del archivo de salida
            String algTo = "";          // algoritmo que se aplica
            boolean hAlg = false;       // se determino el algoritmo a usar: false

        // Se revisan todos los argumentos, extrayendo los relevantes,
        // el siguiente (i+) a la cadena ubicada (i) es su argumento.

        for (int i = 0; i < argsQuantity; i++) {
            if ("-key".equals(args[i])) {           // cadena localizada en i,
               key = Integer.parseInt(args[i+1]);   // Se extrae la clave key, en i+1
               hKey = true;                         // hKey, hay clave: true
            }
            if ("-mode".equals(args[i])) {       // cadena localizada en i,
                mode = args[i+1];                // Se extrae la modalidad "enc" o "dec"
                hMode = true;                    // hMode, hay modalidad: true
            }
            if ("-data".equals(args[i])) {      // cadena localizada en i,
                data = args[i+1];               // Se extrae la cadena con el mensaje
                hData = true;                   // hData hay datos: true
            }
            if ("-in".equals(args[i])) {        // cadena localizada en i,
                fileIn = args[i+1];             // Extrae la cadena con el nombre y ruta del archivo con data
                hIn = true;                     // hIn archivo con datos: true
            }
            if ("-out".equals(args[i])) {
            fileOut = args[i+1];                // Extrae la cadena con el nombre y ruta del archivo de salida
            }
            if ("-alg".equals(args[i])) {
                algTo = args[i+1];                // Extrae la cadena con el nombre del algoritmo
                hAlg = true;                      // hAlg se dio el algoritmo a usar: true
            }
        }
        // Reglas en ausencia de argumentos y de prevalecencia

        if (!hMode) { mode = "enc";}                // "enc" si no hay modalidad
        if (!hKey) { key = 0;}                      // key = 0 si no hay clave
        if (!hData && !hIn) { data = "";}           // Si no hay mensaje de entrada se asume ""
        if (hData && hIn) {  hIn = false;}          // si hay archivo y entrada de args, prevalecen args
        if (!hAlg) { algTo = "unicode";}            // si no se define algoritmo se toma unicode
        if (hIn) {                                  // si hay archivo de entrada se llama al procedimiento de leerlo
            data = readFileAsString(fileIn);
        }
        // Se realiza la operacion segun la modalidad y el algoritmo

        MessageEncrypter encryption = new MessageEncrypter();
        MessageDecrypter decryption = new MessageDecrypter();
        String message = "";

        switch (mode) {
            case "enc":
                switch (algTo) {
                    case "unicode": encryption.setMethod(new unicodeEncryptionMethod());
                        message = encryption.encrypt(data, key);
                        break;
                    case "shift": encryption.setMethod(new shiftEncryptionMethod());
                        message = encryption.encrypt(data, key);
                        break;
                    default: message = "Algoritmo no soportado";
                }
                break;
            case "dec":
                switch (algTo) {
                    case "unicode": decryption.setMethod(new UnicodeDecryptionMethod());
                        message = decryption.decrypt(data, key);
                        break;
                    case "shift": decryption.setMethod(new ShiftDecryptionMethod());
                        message = decryption.decrypt(data, key);
                        break;
                    default: message = "Algoritmo no soportado";
                }
                break;
        }
        messageOut(fileOut, message);
    }

    // contexto de encripcion
    public static class MessageEncrypter {
        private EncryptionMethod method;
        public void setMethod(EncryptionMethod method) {
            this.method = method;
        }
        public String encrypt(String message, int key) {
            return this.method.encrypt(message, key);
        }
    }
    // interface con la estrategia de encripcion
    interface EncryptionMethod {
        String encrypt(String data, int clave);
    }
    // Estrategia de encripcion desplazando +clave caracteres unicode
    static class unicodeEncryptionMethod implements EncryptionMethod {
        public String encrypt(String data, int clave) {
            char[] parcelacionCadena = data.toCharArray();
            for (int i = 0; i < parcelacionCadena.length; i++) {
                parcelacionCadena[i] += clave;
            }
        return new String(parcelacionCadena);
        }
    }
    // Estrategia de encripcion desplazando +clave caracteres shift A-Z a-z
    static class shiftEncryptionMethod implements EncryptionMethod {
        public String encrypt(String data, int key) {
            char[] stringToChar = data.toCharArray();
            StringBuilder message = new StringBuilder();
            for (char letter: stringToChar) {
                if (Character.isUpperCase(letter)) {
                    letter += key;
                    if (letter > 'Z') {
                        letter -=26;
                    }
                } else if (Character.isLowerCase(letter)) {
                    letter += key;
                    if (letter > 'z') {
                        letter -= 26;
                    }
                }
                message.append(letter);
            }
            return  message.toString();
        }
    }
    // contexto de desencripcion
    public static class MessageDecrypter {
        private DecryptionMethod method;
        public void setMethod(DecryptionMethod method) {
            this.method = method;
        }
        public String decrypt(String message, int key) {
            return this.method.decrypt(message, key);
        }
    }
    // interface con la estrategia de desencripcion
    interface DecryptionMethod {
        String decrypt(String data, int clave);
    }
    // Estrategia de desencripcion desplazando +clave caracteres unicode
    static class UnicodeDecryptionMethod implements DecryptionMethod {
        @Override
        public String decrypt(String data, int clave) {
            char[] charToString = data.toCharArray();
            for (int i = 0; i < charToString.length; i++) {
                charToString[i] -= clave;
            }
            return new String(charToString);
        }
    }
    // Estrategia de desencripcion desplazando +clave caracteres shift A-Z a-z
    static class ShiftDecryptionMethod implements DecryptionMethod {
        @Override
        public String decrypt(String data, int key) {
            char[] stringToChar = data.toCharArray();
            StringBuilder message = new StringBuilder();
            for (char letter: stringToChar) {
                if (Character.isUpperCase(letter)) {
                    letter -= key;
                    if (letter < 'A') {
                        letter +=26;
                    }
                } else if (Character.isLowerCase(letter)) {
                    letter -= key;
                    if (letter < 'a') {
                        letter += 26;
                    }
                }
                message.append(letter);
            }
            return  message.toString();
        }
    }

    // Se entrega el mensaje a salida estandar o al archivo
    public static void messageOut (String fileOut, String message) {
        if ("".equals(fileOut)) { System.out.println(message);}   // Si no hay archivo de salida, salida standard
        else {
            try ( FileWriter archivo = new FileWriter(fileOut);     // En caso de error cierra el archivo
                  PrintWriter escritor = new PrintWriter(archivo))    // try () try with resources
            { escritor.println(message);}
            catch (Exception e) { System.out.println("Error IO");}
        }
    }

    // Procedimiento para leer el archivo como una cadena
    // manejando la excepcion en caso de no existir el archivo
    public static String readFileAsString (String fileName) {
        try { File dataFile = new File(fileName);
            if (dataFile.isFile()) {
                return new String (Files.readAllBytes(Paths.get(fileName)));
            } else {
                return null;
            }
        }
        catch (Exception e) {
            return "Error: El archivo de datos no existe.";
        }
    }
}
