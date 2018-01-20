package gentree.client.visualization.elements.configuration.images;

/**
 * Created by vanilka on 17/01/2018.
 */
public enum ImageDeathPaths {

    CAUSE_NORMAL("tombstone.png"),
    CAUE_FIRE("fire.png"),
    CAUSE_LAUGH("laugh.png"),
    CAUSE_LIGHTERING("lightening.png"),
    CAUSE_STRAVING("starving.png");



    private final String path = "/layout/images/deaths/";
    private String file;

    private ImageDeathPaths(String filePath) {

        file = path.concat(filePath);
    }

    @Override
    public String toString() {
        return file;
    }
}
