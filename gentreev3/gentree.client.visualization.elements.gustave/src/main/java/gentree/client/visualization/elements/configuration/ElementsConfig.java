package gentree.client.visualization.elements.configuration;

import gentree.common.configuration.enums.Age;
import gentree.common.configuration.enums.Gender;
import gentree.common.configuration.enums.Race;
import gentree.common.configuration.enums.RelationType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Created by Martyna SZYMKOWIAK on 15/11/2017.
 */
public class ElementsConfig {
    public static final ElementsConfig INSTANCE = new ElementsConfig();

    private final HashMap<Race, Color> raceConfigurator = new HashMap<>();
    private final HashMap<Race, Image> raceImagesMap = new HashMap<>();
    private final HashMap<Age, Image> ageImagesMap = new HashMap<>();
    private final HashMap<Gender, Image> genderImagesMap = new HashMap<>();
    private final HashMap<RelationType, Image> relationTypeImagesMap = new HashMap<>();

    private ElementsConfig() {
        initMap();
        initRaceImageMap();
        initAgeImageMap();
        initGenderImageMap();
        initRelationImagesMap();
    }



    private void initMap() {
        raceConfigurator.put(Race.HUMAIN, Color.GREEN);
        raceConfigurator.put(Race.WEREWOLF, Color.BROWN);
        raceConfigurator.put(Race.VAMPIRE, Color.RED);
    }

    private void initRaceImageMap() {
        
        raceImagesMap.put(Race.HUMAIN, new Image(ImageFiles.HUMAIN.toString()));
        raceImagesMap.put(Race.ALIEN, new Image(ImageFiles.ALIEN.toString()));
        raceImagesMap.put(Race.FAIRY, new Image(ImageFiles.FAIRY.toString()));
        raceImagesMap.put(Race.GENIE, new Image(ImageFiles.GENIE.toString()));
        raceImagesMap.put(Race.GHOST, new Image(ImageFiles.GHOST.toString()));
        raceImagesMap.put(Race.HYBRID, new Image(ImageFiles.HYBRID.toString()));
        raceImagesMap.put(Race.IM_FRIEND, new Image(ImageFiles.IM_FRIEND.toString()));
        raceImagesMap.put(Race.MERMAID, new Image(ImageFiles.MERMAID.toString()));
        raceImagesMap.put(Race.SERVO, new Image(ImageFiles.SERVO.toString()));
        raceImagesMap.put(Race.WHITCH, new Image(ImageFiles.WHITCH.toString()));
        raceImagesMap.put(Race.WEREWOLF, new Image(ImageFiles.WEREWOLF.toString()));
        raceImagesMap.put(Race.ZOMBI, new Image(ImageFiles.ZOMBI.toString()));
        raceImagesMap.put(Race.VEGE, new Image(ImageFiles.VEGE.toString()));
        raceImagesMap.put(Race.VAMPIRE, new Image(ImageFiles.VAMPIRE.toString()));
        raceImagesMap.put(Race.MUMMY, new Image(ImageFiles.MUMMY.toString()));
    }

    private void initGenderImageMap() {
        genderImagesMap.put(Gender.F,new Image(ImageFiles.GENDER_FEMALE.toString()));
        genderImagesMap.put(Gender.M,new Image(ImageFiles.GENDER_MALE.toString()));
    }

    private void initAgeImageMap() {
        ageImagesMap.put(Age.BABY, new Image(ImageFiles.BABY.toString()));
        ageImagesMap.put(Age.TOODLER, new Image(ImageFiles.TOODLER.toString()));
        ageImagesMap.put(Age.CHILD, new Image(ImageFiles.CHILD.toString()));
        ageImagesMap.put(Age.ADO, new Image(ImageFiles.ADO.toString()));
        ageImagesMap.put(Age.YOUNG_ADULT, new Image(ImageFiles.YOUNG_ADULT.toString()));
        ageImagesMap.put(Age.ADULT, new Image(ImageFiles.ADULT.toString()));
        ageImagesMap.put(Age.SENIOR, new Image(ImageFiles.SENIOR.toString()));
    }

    private void initRelationImagesMap() {
        relationTypeImagesMap.put(RelationType.NEUTRAL, new Image(ImageFiles.RELATION_NEUTRAL.toString()));
        relationTypeImagesMap.put(RelationType.MARRIED, new Image(ImageFiles.RELATION_MARRIED.toString()));
        relationTypeImagesMap.put(RelationType.LOVE, new Image(ImageFiles.RELATION_LOVE.toString()));
        relationTypeImagesMap.put(RelationType.FIANCE, new Image(ImageFiles.RELATION_FIANCE.toString()));
    }


    public Image getImageOfRelationType(RelationType type) {
        if(relationTypeImagesMap.containsKey(type)) return relationTypeImagesMap.get(type);
        return relationTypeImagesMap.get(RelationType.NEUTRAL);
    }

    public Image getImageOfRace(Race race) {
        if (raceImagesMap.containsKey(race)) return raceImagesMap.get(race);
        return raceImagesMap.get(Race.HUMAIN);
    }

    public Image getImageOfAge(Age age) {
        if (ageImagesMap.containsKey(age)) return ageImagesMap.get(age);
        return ageImagesMap.get(Age.YOUNG_ADULT);
    }

    public Color findColor(Race race) {
        if (raceConfigurator.containsKey(race)) {
            return raceConfigurator.get(race);
        }
        return Color.GREEN;
    }


    public Image getImageOfGender(Gender item) {
     return  genderImagesMap.containsKey(item) ? genderImagesMap.get(item) : genderImagesMap.get(Gender.M);
    }
}
