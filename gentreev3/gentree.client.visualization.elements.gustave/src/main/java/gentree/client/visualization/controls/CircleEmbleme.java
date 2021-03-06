package gentree.client.visualization.controls;

import gentree.client.visualization.controls.skin.CircleEmblemeSkin;
import gentree.client.visualization.elements.configuration.AutoCleanable;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vanilka on 03/12/2017.
 */
public class CircleEmbleme extends Control implements AutoCleanable {
    private static final String DEFAULT_CLASS_NAME = "circle-embleme";

    private SimpleStringProperty imgPath = new SimpleStringProperty();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public CircleEmbleme() {
        initialize();
    }


    public CircleEmbleme(@NamedArg("imgpath") String imgPath) {
        this();
        this.imgPath.set(imgPath);

    }

    public CircleEmbleme(@NamedArg("image")Image image) {
        this.image.set(image);
    }

    /**
     * Returnt Class css Metadata
     *
     * @return
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.cssMetaDataList;
    }

    @Override
    public void clean() {
        if (getSkin() != null) ((CircleEmblemeSkin) getSkin()).clean();
        getChildren().clear();
        imgPath = null;
        setSkin(null);
    }

    private void initialize() {
        getStyleClass().add(DEFAULT_CLASS_NAME);
    }

    /**
     * Get Control Css metadata
     *
     * @return
     */
    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Create DefaultSkin override
     *
     * @return
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new CircleEmblemeSkin(this);
    }

    /**
     * Return default Css
     *
     * @return
     */
    @Override
    public String getUserAgentStylesheet() {
        return CircleEmbleme.class.getResource("/layout/style/circle-embleme.css").toExternalForm();
    }


    /*
        GETTERS AND SETTERS
     */

    public SimpleStringProperty imgPathProperty() {
        return imgPath;
    }

    public String getImgPath() {
        return imgPath.get();
    }

    public void setImgPath(String imgPath) {
        this.imgPath.set(imgPath);
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    /**
     * Inner static class Styleable properties
     */
    private static class StyleableProperties {

        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        static {
            List<CssMetaData<? extends Styleable, ?>> temp
                    = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(temp);
            cssMetaDataList = Collections.unmodifiableList(temp);
        }
    }
}

