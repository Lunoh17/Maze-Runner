package MazeRunner.ucab.edu.ve;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ControladorBD {
    public static void guardar(Laberinto laberinto){
        Gson gson = new Gson();
//        laberinto.jugador.celdaActual=null; // Avoid circular reference during serialization
        // Determine project root and target JSON file
        String projectRoot = System.getProperty("user.dir");
        File outFile = new File(projectRoot, "laberinto.json");

        // Ensure parent directories exist (should be project root, but safe)
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created) {
                System.err.println("Warning: could not create directory: " + parent.getAbsolutePath());
            }
        }

        // Write JSON using UTF-8
        try (FileOutputStream fos = new FileOutputStream(outFile);
             Writer osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             JsonWriter writer = new JsonWriter(osw)) {

            gson.toJson(laberinto, Laberinto.class, writer);
            writer.flush();

        } catch (IOException e) {
            // Print error so caller can see what happened
            System.err.println("Error saving laberinto to " + outFile.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
