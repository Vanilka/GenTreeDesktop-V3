package gentree.client.visualization.controls.skin;

import gentree.client.visualization.controls.CircleEmbleme;
import gentree.client.visualization.elements.configuration.AutoCleanable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;


/**
 * Created by vanilka on 03/12/2017.
 */
public class CircleEmblemeSkin extends SkinBase<CircleEmbleme> implements AutoCleanable {

    @Getter
    private StackPane backgroundPane;
    private ImageView imgView;

    private boolean invalidate = false;

    private  InvalidationListener invalidListener = this::invalidated;
    private ChangeListener<String> pathListener = this::pathChanged;
    private ChangeListener<Image> imageListener = this::imageChanged;

    {
        backgroundPane = new StackPane();
        imgView = new ImageView();

    }

    public CircleEmblemeSkin(CircleEmbleme control) {
        super(control);
        initialize();
    }

    private void pathChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            this.imgView.setImage(new Image(newValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageChanged(ObservableValue<? extends Image> observableValue, Image oldValue, Image newValue) {
    try {
        this.imgView.setImage(newValue);
    } catch (Exception e) {
        e.printStackTrace();
    }

    }



    private void initialize() {
        initVisualization();
        initListeners();
    }


    private void initListeners() {
        initSizeListeners();
        getSkinnable().imgPathProperty().addListener(pathListener);
        getSkinnable().imageProperty().addListener(imageListener);
    }

    private void initSizeListeners() {
        getSkinnable().widthProperty().addListener(invalidListener);
        getSkinnable().heightProperty().addListener(invalidListener);

        imgView.fitHeightProperty().bind(getSkinnable().prefWidthProperty().subtract(5));
        imgView.fitWidthProperty().bind(getSkinnable().prefWidthProperty().subtract(5));


    }

    private void initVisualization() {
        getChildren().add(backgroundPane);
        backgroundPane.getChildren().addAll(imgView);

        if(getSkinnable().getImage() != null) {
            this.imgView.setImage(getSkinnable().getImage());
        } else if (getSkinnable().getImgPath() != null) {
            this.imgView.setImage(new Image(getSkinnable().getImgPath()));
        }
    }


    @Override
    public void clean() {


        backgroundPane.getChildren().clear();
        backgroundPane = null;

        imgView.setImage(null);
        imgView = null;

        invalidListener = null;
        pathListener = null;

        getChildren().clear();
    }

    protected void initBorder(Color color, Pane node) {
        node.setBorder(new Border
                (new BorderStroke(color,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        BorderWidths.DEFAULT)));
    }

    private void invalidated(Observable observable) {
        invalidate = true;
    }
}
