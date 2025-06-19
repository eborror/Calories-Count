//***********************************************************************
//   App.java           House Tully
//
//  The App class invokes the main method to start the application.
//***********************************************************************

package calories_count.files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    public static User currentUser;

    // Start the App and set opening scene
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("TrackedFoods"), 415, 620);
        stage.setScene(scene);
        stage.setTitle("Calories Count");
        stage.show();
    }

    // Switches the current scene to a different fxml view
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Loads fxml file
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
