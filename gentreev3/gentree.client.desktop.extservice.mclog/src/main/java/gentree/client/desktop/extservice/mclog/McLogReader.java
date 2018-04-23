package gentree.client.desktop.extservice.mclog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by vanilka on 10/04/2018.
 */

public class McLogReader {
    public static final McLogReader INSTANCE = new McLogReader();

    private Path path;

    @Getter
    private ObservableList<String> logContent = FXCollections.observableArrayList();

    private McLogReader() {

    }


    public void setPath(String p) {
        path = Paths.get(p);
    }


    public void loadFile() {
        logContent.clear();
        try {
            Stream<String> lines = Files.lines(path).filter(line -> line.contains(TemplatesAllowed.TEMPLATE_CHANGE_AGE) || line.contains(TemplatesAllowed.TEMPLATE_MARRIED));
            lines.forEach(logContent::add);
            lines.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
