import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {
    public void display(String title, String message, String notificationType) {
        System.out.println("I'm here : 1 Notification.display");
        Notifications notificationWindow = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT)
                .onAction(e -> {

                });

        if (notificationType == "OK") {
            notificationWindow.show();
        } else if (notificationType == "ERROR") {
            notificationWindow.showError();
        }
        System.out.println("I'm here : 2 Notification.display");
    }
}
