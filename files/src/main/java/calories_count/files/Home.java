package calories_count.files;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Home extends Application {

    private Scene homeScene;

    @Override
    public void start(Stage primaryStage) {

        // Home Scene
        VBox homeLayout = new VBox(20);
        homeLayout.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("DayTracks");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px;");

        Button goToSecond = new Button("User Data");
        homeLayout.getChildren().addAll(titleLabel, goToSecond);
        homeScene = new Scene(homeLayout, 360, 640); // Mobile screen size

        /*// Second Scene
        VBox secondLayout = new VBox(20);
        secondLayout.setAlignment(Pos.CENTER);
        Button goBack = new Button("Back to Home");
        secondLayout.getChildren().add(goBack);
        secondScene = new Scene(secondLayout, 360, 640);*/

        // Button actions
        //User sampleUser = new User("Ethan", homeScene, "Male", 160, 70); // Example user
        //User userInfo = new User(primaryStage, homeScene, sampleUser);
        //goToSecond.setOnAction(e -> primaryStage.setScene(userInfo.getScene()));
        //goBack.setOnAction(e -> primaryStage.setScene(homeScene));

        // Set initial scene and show
        primaryStage.setScene(homeScene);
        primaryStage.setTitle("JavaFX Mobile App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
