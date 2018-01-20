package gentree.client.visualization.elements.configuration.images;

/**
 * Created by vanilka on 17/01/2018.
 */
public enum  ImageAgeFiles {
    BABY("baby.png"),
    TOODLER("toodler.png"),
    CHILD("childhood.png"),
    ADO("ado.png"),
    YOUNG_ADULT("joung_adult.png"),
    ADULT("adult.png"),
    SENIOR("senior.png");

    private final String path = "/layout/images/ages/";
    private String file;

    private ImageAgeFiles(String filePath) {

        file = path.concat(filePath);
    }

    @Override
    public String toString() {
        return file;
    }
}
