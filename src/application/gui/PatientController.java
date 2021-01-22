package application.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Components.Card;
import Components.Toast;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.IPatient;
import model.Patient;

public class PatientController implements Initializable {
	@FXML
	private ListView<AnchorPane> list;
	@FXML
	private AnchorPane root;
	@FXML
	private TextField nameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField phoneNumberField;

	private Stage primaryStage;

	Card card = new Card();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateListItem();
	}

	private void populateListItem() {
		for (IPatient p : Main.clinic.getPatients()) {
			AnchorPane view = new AnchorPane();
			card.getPatientCard(view, ((Patient) p).getName(), ((Patient) p).getEmail(), ((Patient) p).getPhoneNumber(),
					((Patient) p).getOwedBalance(), ((Patient) p).getTotalPaidBalance(), ((Patient) p).getId(), root);
			list.getItems().add(view);
		}
	}

	private void loadNewScene(String fileName) {
		Fxmloader newScene = new Fxmloader();
		Pane view = newScene.loadPage(fileName);
		root.getChildren().removeAll(root.getChildren());
		root.getChildren().add(view);
	}

	@FXML
	private void addPatient(ActionEvent event) throws IOException {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (nameField.getText().equals("") || emailField.getText().equals("") || phoneNumberField.getText().equals(""))
							throw new NullPointerException();
						
						String name = nameField.getText();
						String email = emailField.getText();
						String phone = phoneNumberField.getText();
						Main.clinic.createPatient(
								new Patient.Builder().setName(name).setPhoneNumber(phone).setEmail(email).build());
						Main.writeToPatientFile();
						Main.clinic.getPatients().clear();
						Main.readFromPatientsFile();
						loadNewScene("Patient");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).run();

			nameField.clear();
			emailField.clear();
			phoneNumberField.clear();
		} catch (NullPointerException e) {
			Toast.makeText(primaryStage, "All Fields Must Be Filled", 1000, 500, 500);
		}
	}
	
	@FXML
	private void delete() throws IOException {
		for(int i = 0; i < list.getItems().size(); i++) {
			if(((CheckBox) list.getItems().get(i).getChildren().get(3)).isSelected())
				Main.clinic.deletePatient(i);
			Main.writeToPatientFile();
			Main.clinic.getPatients().clear();
			Main.readFromPatientsFile();
			loadNewScene("Patient");
		}
	}
}
