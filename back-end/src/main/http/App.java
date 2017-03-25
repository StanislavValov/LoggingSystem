package main.http;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import main.core.CoreModule;
import main.persistent.PersistentModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class App {
    public static void main(String[] args) throws Exception {

        final Map<String, String> configuration = loadConfiguration(args);

        Module[] modules = new Module[]{

                new CoreModule(),
                new HttpModule(),
                new PersistentModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        Names.bindProperties(binder(), configuration);
                    }
                }
        };
        Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
        JettyServer jettyServer = injector.getInstance(JettyServer.class);
        jettyServer.startServer();
    }

    private static Map<String, String> loadConfiguration(String[] args) {

        Properties configuration = new Properties();
        String configurationFile = "back-end/configuration.properties";

        if (args.length > 0) {
            configurationFile = args[0];
        }

        try {
            InputStream input = new FileInputStream(new File(configurationFile));
            configuration.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> config = new HashMap<>();

        for (String key : configuration.stringPropertyNames()) {
            String value = configuration.getProperty(key);
            config.put(key, value);
        }

        return config;
    }
}