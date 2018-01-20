package gentree.client.visualization.elements.configuration.images;

/**
 * Created by vanilka on 27/12/2016.
 */
public enum ImageFiles {
    NO_NAME_MALE("backgrounds/NoNameMale.png"),
    NO_NAME_FEMALE("backgrounds/NoNameFemale.png"),
    GENERIC_MALE("backgrounds/GenericMale.png"),
    GENERIC_FEMALE("backgrounds/GenericFemale.png"),
    EQUAL_TRIANGLE("backgrounds/equalTrangle.png"),
    GENDER_FEMALE("backgrounds/female.png"),
    GENDER_MALE("backgrounds/male.png");

    private final String path = "/layout/images/";
    private String file;

    private ImageFiles(String filePath) {

        file = path.concat(filePath);
    }

    @Override
    public String toString() {
        return file;
    }
}

