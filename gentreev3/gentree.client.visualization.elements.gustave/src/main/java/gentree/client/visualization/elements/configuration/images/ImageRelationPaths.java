package gentree.client.visualization.elements.configuration.images;

/**
 * Created by vanilka on 17/01/2018.
 */
public enum ImageRelationPaths {

    RELATION_NEUTRAL("backgrounds/neutral.png"),
    RELATION_MARRIED("backgrounds/maried.png"),
    RELATION_FIANCE("backgrounds/fiance.png"),
    NEW_ADDITION("backgrounds/new.png"),
    RELATION_LOVE("backgrounds/love.png");

    private final String path = "/layout/images/";
    private String file;

    private ImageRelationPaths(String filePath) {

        file = path.concat(filePath);
    }

    @Override
    public String toString() {
        return file;
    }
}
