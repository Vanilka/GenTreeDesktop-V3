package gentree.client.visualization.service.implementation;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;

/**
 * Created by vanilka on 17/03/2018.
 */
public class GenTreeImageGeneratorVanilla {
    public static final GenTreeImageGeneratorVanilla INSTANCE = new GenTreeImageGeneratorVanilla();

    private final SnapshotParameters snapshotParameters = new SnapshotParameters();

    /*
        PANES
     */
    private JPanel imagePane = new JPanel();

    private GenTreeImageGeneratorVanilla() {
        initHeader();
        initParameters();
        generateImagePane();
    }

    private void generateImagePane() {
        imagePane.setLayout(new BoxLayout(imagePane, 1  ));

        JButton button = new JButton("Button1");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePane.add(button);

        button = new JButton("Button2");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePane.add(button);

        button = new JButton("Button3");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePane.add(button);
    }


    private void initParameters() {
        snapshotParameters.setFill(Color.TRANSPARENT);
        snapshotParameters.setDepthBuffer(false);
    }

    private void initHeader() {


    }

    public void doScreen(Pane nodeContent, String text) {



        imagePane.setSize((int)nodeContent.getWidth(),(int) nodeContent.getHeight());
        nodeContent.getChildren().forEach(child -> {
            System.out.println("Wil work for: " + child);
            WritableImage snapshot = child.snapshot(snapshotParameters, null);
            BufferedImage imageb = SwingFXUtils.fromFXImage(snapshot, null);
            JButton b = new JButton("My button-".concat(child.getId()));
            b.setSize(1300, 50);
            imagePane.add(b);
        });

        System.out.println("ImagePane elements : " +imagePane.getComponentCount());



        BufferedImage bufImage = new BufferedImage(imagePane.getSize().width, imagePane.getSize().height, BufferedImage.TYPE_INT_RGB);
        imagePane.paint(bufImage.createGraphics());

        File file = new File("./GenTreeTest" + Instant.now().toEpochMilli() + ".png");

        try {
            ImageIO.write(bufImage, "png", file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
