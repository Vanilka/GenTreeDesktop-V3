package gentree.client.desktop;

import gentree.client.desktop.configuration.GenTreeDefaultProperties;
import gentree.client.desktop.extservice.mclog.McLogWatch;
import gentree.client.desktop.service.GenTreeContext;
import gentree.client.desktop.service.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Properties;

/**
 * Created by Martyna SZYMKOWIAK on 01/07/2017.
 */

public class GenTreeRun extends Application {

    private static ScreenManager sc = ScreenManager.INSTANCE;
    private static GenTreeContext gtc = GenTreeContext.INSTANCE;
    private static GenTreeDefaultProperties def = GenTreeDefaultProperties.INSTANCE;


    public static void main(String[] args) {
        Properties props = System.getProperties();
        props.setProperty("com.sun.prism.order", "sw");
        launch(args);
        props.setProperty("com.sun.prism.order", "sw");
    }

    @Override
    public void start(Stage stage) throws Exception {
        sc.init(stage);

    }

}

