package gentree.client.visualization.elements.configuration.images;

/**
 * Created by vanilka on 17/01/2018.
 */
public enum ImageRacesPaths {
    HUMAIN("sim.png"),
    VAMPIRE("Vampire.png"),
    VEGE("plant.png"),
    ALIEN("Alien.png"),
    GHOST("fairy.png"),
    ZOMBI("zombi.png"),
    SERVO("Servo.png"),
    WEREWOLF("Werewolf.png"),
    GENIE("Genie.png"),
    WHITCH("which.png"),
    FAIRY("fairy.png"),
    IM_FRIEND("imfriend.png"),
    MUMMY("Mummy.png"),
    MERMAID("Mermaid.png"),
    HYBRID("hybrid.png");

    private final String path = "/layout/images/races/";
    private String file;

    private ImageRacesPaths(String filePath) {

        file = path.concat(filePath);
    }

    @Override
    public String toString() {
        return file;
    }
}
