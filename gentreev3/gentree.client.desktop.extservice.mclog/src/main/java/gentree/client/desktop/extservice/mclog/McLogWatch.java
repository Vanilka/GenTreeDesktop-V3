package gentree.client.desktop.extservice.mclog;

import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

/**
 * Created by vanilka on 22/04/2018.
 */
public class McLogWatch extends Thread {

    private Path mcLogPath = Paths.get("");



    @Override
    public void run() {

        try {

            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = mcLogPath.getParent().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;

                    Path path = ev.context();

                    if (!path.getFileName().equals(mcLogPath.getFileName())) {
                        continue;
                    }

                    // process file
                    Platform.runLater(this::loadFile);

                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {

        try {

          Files.lines(mcLogPath).filter(line -> line.contains("has aged-up to ")).forEach(System.out::println);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
