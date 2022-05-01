package uk.ac.soton.comp2211.component;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Component to show notifications on the starting screen.
 */
public class NotificationsBox extends VBox {
    
    VBox notifBox;
    
    public NotificationsBox() {
        this.notifBox = new VBox();
        
        Text title = new Text("Notifications:");
        this.getChildren().addAll(title, new Text(""), this.notifBox);
    }
    
    public void addNotifications(ArrayList<String> notifs) {
        this.notifBox.getChildren().clear();
        for (String notif : notifs) {
            this.getChildren().add(new Text(notif));
        }
    }
    
}
