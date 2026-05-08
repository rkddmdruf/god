package builder;
import javax.swing.JOptionPane;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

public class SimpleJarBuilder {

    static String SRC_DIR = "src";
    static String BUILD_DIR = "build";

    // 🔥 바탕화면 경로

    static String MAIN_CLASS = "main.Main1";
    
    
    public SimpleJarBuilder(){
        try {
            makeJar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "생성 완료");
    }
    
    static void makeJar() throws Exception {

        String currentJar = SimpleJarBuilder.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Main-Class", "main.Installer");
        String str = new File(SimpleJarBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        String fileName = "\\install.jar";
        File f = new File(str + fileName);
        int index = 1;
        while(f.isFile()) {
        	fileName = "\\install (" + index + ").jar";
        	f = new File(str + fileName);
        	index ++;
        }
        try (
            JarInputStream jis = new JarInputStream(new FileInputStream(currentJar));
        	
            JarOutputStream jos = new JarOutputStream(
                    new FileOutputStream( str + fileName ), manifest)
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