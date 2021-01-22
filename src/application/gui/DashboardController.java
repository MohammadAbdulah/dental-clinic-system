package application.gui;

import java.net.URL;
import java.util.ResourceBundle;
import Components.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Appointment;
import model.Status;

public class DashboardController implements Initializable{
	@FXML
	private VBox vbox;
	@FXML
	private Text numberOfAppointments;
	@FXML
	private Text numberOfPatients;
	@FXML
	private Text canceledText;
	
	private Card card = new Card();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getAppointmentsCards();
	}
	
	private void getAppointmentsCards() {	
		for(Appointment a : Main.clinic.getAppointments()) {
			AnchorPane view = new AnchorPane();
			card.getDashboardCard(view, a);
			VBox.setMargin(view, new Insets(12, 3, 0, 3));
			vbox.getChildren().add(view);
		}
		numberOfAppointments.setText(Main.clinic.getAppointments().size() + "");
		numberOfPatients.setText(Main.clinic.getPatients().size() + "");
		
		int i = 0;
		for(Appointment a : Main.clinic.getAppointments())
			if(a.getStatus() == Status.CANCELED)
				i++;
		canceledText.setText(i + "");
	}
}
