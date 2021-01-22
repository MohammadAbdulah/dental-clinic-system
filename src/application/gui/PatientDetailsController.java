package application.gui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import Components.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Patient;
import model.Visit;

public class PatientDetailsController implements Initializable {

	@FXML
	private ListView<AnchorPane> list;
	@FXML
	private Text name;
	@FXML
	private Text email;
	@FXML
	private Text phone;

	Card card = new Card();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			addVisitDetails();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addVisitDetails() throws FileNotFoundException {
		int i = 0;
		for (Visit v : ((Patient) Main.clinic.findPatientById(Card.patientId)).getPreviousVisits()) {
			AnchorPane view = new AnchorPane();
			card.getPatientDetailsCard(view, v, i);
			list.getItems().add(view);
			i++;
		}
		
		name.setText(((Patient) Main.clinic.findPatientById(Card.patientId)).getName());
		email.setText(((Patient) Main.clinic.findPatientById(Card.patientId)).getEmail());
		phone.setText(((Patient) Main.clinic.findPatientById(Card.patientId)).getPhoneNumber());
	}
}
