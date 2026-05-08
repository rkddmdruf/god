package builder;
import javax.swing.JOptionPane;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

public class SimpleJarBuilder2 {

    public SimpleJarBuilder2(){
        try {
            makeJar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "생성 완료");
    }
    
    static void makeJar() throws Exception {

        String currentJar = SimpleJarBuilder2.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Main-Class", "main.Main2");

        try (
            JarInputStream jis = new JarInputStream(new FileInputStream(currentJar));
            JarOutputStream jos = new JarOutputStream(
                    new FileOutputStream(
                    		new File(SimpleJarBuilder2.class
                    		.getProtectionDomain()
                    		.getCodeSource()
                    		.getLocation()
                    		.toURI()
                    		).getParent() + "\\Nexus.jar"
                    ), manifest)
        ) {
            JarEntry entry;

            while ((entry = jis.getNextJarEntry()) != null) {

                if (entry.getName().equals("META-INF/MANIFEST.MF")) continue;

                jos.putNextEntry(new JarEntry(entry.getName()));
                jis.transferTo(jos);
                jos.closeEntry();
            }
        }
    }
}