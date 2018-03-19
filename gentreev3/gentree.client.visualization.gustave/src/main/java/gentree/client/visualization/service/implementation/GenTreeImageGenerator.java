package gentree.client.visualization.service.implementation;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by Martyna SZYMKOWIAK on 02/11/2017.
 */
public class GenTreeImageGenerator {

    public static final GenTreeImageGenerator INSTANCE = new GenTreeImageGenerator();

    private final SnapshotParameters snapshotParameters = new SnapshotParameters();

    /*
        PANES
     */
    private BorderPane imagePane = new BorderPane();
    private AnchorPane headerPane = new AnchorPane();
    private HBox content = new HBox();
    private Label title = new Label("Family Name");
    private Insets insets30 = new Insets(30);
    private Insets insets10 = new Insets(10);

    private GenTreeImageGenerator() {
        initHeader();
        initParameters();
        BorderPane.setMargin(headerPane, insets30);
        BorderPane.setMargin(content, insets30);
    }

    private void generateImagePane() {
        imagePane = null;
        imagePane = new BorderPane();
        imagePane.setTop(headerPane);
        imagePane.setCenter(content);
        imagePane.setPadding(new Insets(30, 30, 30, 30));
        imagePane.autosize();
        imagePane.setStyle(
                "-fx-background-image: url(" +
                        "'layout/images/backgrounds/gentreebackground.jpg');" +
                        "-fx-background-size: cover;"
        );

        content.setPadding(insets30);
        content.setStyle(" -fx-background-color: rgba(251, 251, 255, 0.45);; " +
                "-fx-background-radius: 10 10 10 10; " +
                "-fx-border-radius: 10 10 10 10;");
    }

    private void initParameters() {
        snapshotParameters.setFill(Color.TRANSPARENT);
        snapshotParameters.setDepthBuffer(false);
    }

    private void initHeader() {
        headerPane.getChildren().add(this.title);
        AnchorPane.setTopAnchor(this.title, 10.0);
        AnchorPane.setBottomAnchor(this.title, 10.0);
        AnchorPane.setLeftAnchor(this.title, 50.0);
        AnchorPane.setRightAnchor(this.title, 50.0);
        this.title.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 36));
        headerPane.setPrefHeight(70);
        headerPane.setPadding(insets30);
        headerPane.setStyle(" -fx-background-color: rgba(251, 251, 255, 0.45);; " +
                "-fx-background-radius: 10 10 10 10; " +
                "-fx-border-radius: 10 10 10 10;");


    }

    public WritableImage doScreen(Pane nodeContent, String text) {
        this.title.setText(text);
        content.getChildren().clear();
        nodeContent.getChildren().forEach(child -> {
            content.getChildren().add(new ImageView(child.snapshot(snapshotParameters, null)));
        });

        generateImagePane();
        System.out.println("Content Children's count " +content.getChildren().size());
        Scene scene = new Scene(imagePane);
        return scene.snapshot(null);

    }
}
