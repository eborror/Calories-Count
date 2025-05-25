package calories_count.files;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

     @FXML
    private void switchToUserForm() throws IOException {
        App.setRoot("user_form");
    }
}
