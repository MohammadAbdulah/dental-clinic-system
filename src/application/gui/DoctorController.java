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
import model.Appointment;
import model.Doctor;
import model.IDoctor;

public class DoctorController implements Initializable {
	@FXML
	private AnchorPane root;
	@FXML
	private TextField nameField;
	@FXML
	private ListView<AnchorPane> list;

	private Card card = new Card();
	private Stage primaryStage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateListItem();
	}

	private void populateListItem() {
		int i = 0;
		for (IDoctor d : Main.clinic.getDoctors()) {
			int numOfApp = 0;
			for(Appointment a : Main.clinic.getAppointments()) 
				if(a.getDoctor() == d)
					numOfApp++;
			
			AnchorPane view = new AnchorPane();
			card.getDoctorCard(view, ((Doctor) d).getName(), ((Doctor) d).getWorkSchedule().size() + "", root, i, numOfApp);
			list.getItems().add(view);
			i++;
		}
	}

	@FXML
	private void addDoctor(ActionEvent event) throws IOException {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (nameField.getText().equals(""))
						throw new NullPointerException();
					
					try {
						Doctor d = new Doctor(nameField.getText());
						Main.clinic.createDoctor(d);
						Main.writeToDoctorFile();
						Main.clinic.getDoctors().clear();
						Main.readFromDoctorsFile();
						nameField.clear();
						loadNewScene("Doctor");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).run();
		} catch (NullPointerException e) {
			Toast.makeText(primaryStage, "All Fields Must Be Filled", 1000, 500, 500);
		}
	}
	
	@FXML
	private void delete() throws IOException {
		for(int i = 0; i < list.getItems().size(); i++) {
			if(((CheckBox) list.getItems().get(i).getChildren().get(3)).isSelected())
				Main.clinic.deleteDoctor(i);
			Main.writeToDoctorFile();
			Main.clinic.getDoctors().clear();
			Main.readFromDoctorsFile();
			loadNewScene("Doctor");
		}
	}
	
	private void loadNewScene(String fileName) {
		Fxmloader newScene = new Fxmloader();
		Pane view = newScene.loadPage(fileName);
		root.getChildren().removeAll(root.getChildren());
		root.getChildren().add(view);
	}
}
