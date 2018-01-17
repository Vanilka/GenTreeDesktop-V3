package gentree.client.desktop.configuration;

import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.configuration.ElementsConfig;
import gentree.client.visualization.elements.configuration.ImageFiles;
import gentree.common.configuration.enums.Age;
import gentree.common.configuration.enums.Gender;
import gentree.common.configuration.enums.Race;
import gentree.common.configuration.enums.RelationType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Created by vanilka on 11/01/2018.
 */
public class CellFactoryProvider {

    public static final Callback<TableColumn.CellDataFeatures<Member, String>, ObservableValue<String>> PHOTO_VALUE_FACTORY = getPhotoValueFactory();
    public static final Callback<ListView<RelationType>, ListCell<RelationType>> CUSTOM_RELATION_LIST_CELL = getCustomRelationListCell();
    public static final Callback<ListView<Realm>, ListCell<Realm>> CUSTOM_REALM_LIST_CELL = getCustomRealmListCell();
    public static final Callback<ListView<Race>, ListCell<Race>> RACE_LIST_CELL = getRaceListCell();
    public static final Callback<ListView<Age>, ListCell<Age>> AGE_LIST_CELL = getAgeListCell();
    public static final Callback<ListView<Gender>, ListCell<Gender>> GENDER_LIST_CELL = getGenderListCell();
    public static final Callback<TableColumn<Relation, RelationType>, TableCell<Relation, RelationType>> RELATION_TYPE_CELL_FACTORY = getRelationTypeCellFactory();

    private static final String TABLE_RELATION_SIM_RIGHT = "RIGHT";
    private static final String TABLE_RELATION_SIM_LEFT = "LEFT";
    private static int TABLE_IMAGE_MEMBER_HEIGHT = 80;
    private static int TABLE_IMAGE_MEMBER_WIDTH = 60;

    public static final Callback<TableColumn<Relation, Member>, TableCell<Relation, Member>> MEMBER_LEFT_CELL_FACTORY = getMemberCellFactory(TABLE_RELATION_SIM_LEFT);
    public static final Callback<TableColumn<Relation, Member>, TableCell<Relation, Member>> MEMBER_RIGHT_CELL_FACTORY = getMemberCellFactory(TABLE_RELATION_SIM_RIGHT);
    public static final Callback<TableColumn<Member, String>, TableCell<Member, String>> SIM_PHOTO_CELL_FACTORY = getPhotoCellFactory();

    private static Callback<TableColumn<Relation, Member>, TableCell<Relation, Member>> getMemberCellFactory(String parameter) {
        return new Callback<TableColumn<Relation, Member>, TableCell<Relation, Member>>() {
            @Override
            public TableCell<Relation, Member> call(TableColumn<Relation, Member> param) {
                return new TableCell<Relation, Member>() {

                    @Override
                    protected void updateItem(Member item, boolean empty) {
                        super.updateItem(item, empty);
                        ImageView imageview = new ImageView();
                        if (item != null) {
                            imageview.setFitHeight(TABLE_IMAGE_MEMBER_HEIGHT);
                            imageview.setFitWidth(TABLE_IMAGE_MEMBER_WIDTH);
                            imageview.setImage(new Image(item.getPhoto()));
                            setGraphic(imageview);
                        } else {
                            if (!empty) {
                                imageview.setFitHeight(TABLE_IMAGE_MEMBER_HEIGHT);
                                imageview.setFitWidth(TABLE_IMAGE_MEMBER_WIDTH);
                                String path = parameter.equals(TABLE_RELATION_SIM_LEFT) ?
                                        ImageFiles.NO_NAME_FEMALE.toString() : ImageFiles.NO_NAME_MALE.toString();
                                imageview.setImage(new Image(path));
                                setGraphic(imageview);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }

                };
            }
        };
    }

    private static Callback<TableColumn<Relation, RelationType>, TableCell<Relation, RelationType>> getRelationTypeCellFactory() {
        return new Callback<TableColumn<Relation, RelationType>, TableCell<Relation, RelationType>>() {
    @Override
    public TableCell<Relation, RelationType> call(TableColumn<Relation, RelationType> param) {
        return new TableCell<Relation, RelationType>() {
            @Override
            protected void updateItem(RelationType item, boolean empty) {
                super.updateItem(item, empty);
                ImageView imageview = new ImageView();
                if (item != null) {
                    imageview.setImage(ElementsConfig.INSTANCE.getImageOfRelationType(item));
                    imageview.setFitHeight(40);
                    imageview.setFitWidth(40);
                    setGraphic(imageview);
                } else {
                    setGraphic(null);
                }
            }
        };
    }
};
    }

    private static Callback<TableColumn<Member, String>, TableCell<Member, String>> getPhotoCellFactory() {

        return new Callback<TableColumn<Member, String>, TableCell<Member, String>>() {
            @Override
            public TableCell<Member, String> call(TableColumn<Member, String> param) {

                return new TableCell<Member, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        ImageView imageview = new ImageView();
                        imageview.setFitHeight(TABLE_IMAGE_MEMBER_HEIGHT);
                        imageview.setFitWidth(TABLE_IMAGE_MEMBER_WIDTH);
                        if (item != null) {
                            imageview.setImage(new Image(item));
                            setGraphic(imageview);
                        } else {
                            if (!empty) {

                                imageview.setImage(new Image(ImageFiles.GENERIC_MALE.toString()));
                                setGraphic(imageview);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };
    }

    private static Callback<TableColumn.CellDataFeatures<Member, String>, ObservableValue<String>> getPhotoValueFactory() {
        return param -> new ReadOnlyObjectWrapper<>(param.getValue().getPhoto());
    }

    private static Callback<ListView<RelationType>, ListCell<RelationType>> getCustomRelationListCell() {
        int relationWidth = 50;
        int relationHeight = 50;

        return new Callback<ListView<RelationType>, ListCell<RelationType>>() {
            @Override
            public ListCell<RelationType> call(ListView<RelationType> param) {
                return new ListCell<RelationType>() {
                    @Override
                    public void updateItem(RelationType item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                           setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfRelationType(item), relationWidth, relationHeight));
                           setText("");
                        } else {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfRelationType(RelationType.NEUTRAL), relationWidth, relationHeight));
                            setText("");
                        }
                    }

                };
            }
        };
    }

    private static Callback<ListView<Realm>, ListCell<Realm>> getCustomRealmListCell() {
        return new Callback<ListView<Realm>, ListCell<Realm>>() {

            @Override
            public ListCell<Realm> call(ListView<Realm> param) {
                final ListCell<Realm> realmCell = new ListCell<Realm>() {
                    @Override
                    protected void updateItem(Realm item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText("");
                        }
                    }
                };
                return realmCell;
            }
        };
    }

    private static Callback<ListView<Race>, ListCell<Race>> getRaceListCell() {
        return new Callback<ListView<Race>, ListCell<Race>>() {
            @Override
            public ListCell<Race> call(ListView<Race> param) {
                return new ListCell<Race>() {
                    @Override
                    public void updateItem(Race item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfRace(item), 40, 40));
                            setText("");
                        } else {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfRace(Race.HUMAIN), 40, 40));
                            setText("");
                        }
                    }

                };
            }
        };

    }

    private static Callback<ListView<Age>, ListCell<Age>> getAgeListCell() {
        return new Callback<ListView<Age>, ListCell<Age>>() {
            @Override
            public ListCell<Age> call(ListView<Age> param) {
                return new ListCell<Age>() {
                    @Override
                    public void updateItem(Age item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfAge(item), 30, 40));
                            setText("");
                        } else {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfAge(Age.YOUNG_ADULT), 30, 40));
                            setText("");
                        }
                    }

                };
            }
        };

    }

    private static Callback<ListView<Gender>, ListCell<Gender>> getGenderListCell() {
        Callback<ListView<Gender>, ListCell<Gender>> callback = new Callback<ListView<Gender>, ListCell<Gender>>() {
            @Override
            public ListCell<Gender> call(ListView<Gender> param) {
                final ListCell<Gender> genderCell = new ListCell<Gender>() {
                    @Override
                    public void updateItem(Gender item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfGender(item), 30, 30));
                            setText("");
                        } else {
                            setGraphic(setGraphicToImageView(ElementsConfig.INSTANCE.getImageOfGender(Gender.M), 30, 30));
                            setText("");
                        }
                    }

                };
                return genderCell;
            }
        };
        return callback;

    }

    private static ImageView setGraphicToImageView(String path, int width, int height) {
        ImageView imv = new ImageView(path);
        imv.setFitWidth(width);
        imv.setFitHeight(height);
        return imv;
    }

    private static ImageView setGraphicToImageView(Image image, int width, int height) {
        ImageView imv = new ImageView(image);
        imv.setFitWidth(width);
        imv.setFitHeight(height);
        return imv;
    }
    
    private static ImageView setGraphicToImageView(ImageView imv, String path) {
        imv = new ImageView(path);
        return imv;
    }

}
