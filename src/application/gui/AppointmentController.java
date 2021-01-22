package application.gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import Components.Card;
import Components.Toast;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Status;

public class AppointmentController implements Initializable{
	@FXML
	private AnchorPane root;
	@FXML
	private ListView<AnchorPane> list;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ComboBox<Integer> startHr;
	@FXML
	private ComboBox<Integer> startMin;
	@FXML
	private ComboBox<String> doctorCombo;
	@FXML
	private ComboBox<String> patientCombo;
	
	private Stage primaryStage;
	private Card card = new Card();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComboBox();
		populateListItem();
	}
	
	private void populateListItem() {
		int i = 0;
		for(Appointment a : Main.clinic.getAppointments()) {
			AnchorPane view = new AnchorPane();
			card.getAppointmentCard(view, a.getDate().getScheduledDateTime().getDayOfWeek().toString(), 
					a.getDate().getScheduledDateTime().getDayOfMonth() + "-" + a.getDate().getScheduledDateTime().getMonth().toString().substring(0,3),
					a.getDate().getStartTime().toString(), a.getDate().getEndTime().toString(), a.getStatus().toString(), i, root);
			list.getItems().add(view);
			i++;
		}
	}
	
	private void setComboBox() {
		startMin.getItems().add(0);
		startMin.getItems().add(30);
		
		for (int i = 0; i < 24; i++)
			startHr.getItems().add(i);
		
		for (int i = 0; i < Main.clinic.getDoctors().size(); i++)
			doctorCombo.getItems().add(((Doctor) Main.clinic.getDoctors().get(i)).getName());
		
		for (int i = 0; i < Main.clinic.getPatients().size(); i++)
			patientCombo.getItems().add(((Patient) Main.clinic.getPatients().get(i)).getName());
	}

	@FXML
	private void takeAppointment(ActionEvent event) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Doctor d = (Doctor) Main.clinic.findDoctorById(doctorCombo.getSelectionModel().getSelectedIndex());
					Patient p = (Patient) Main.clinic.findPatientById(patientCombo.getSelectionModel().getSelectedIndex());
					LocalDateTime date = LocalDateTime.of(datePicker.getValue().getYear(),
							datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth(), startHr.getValue(), startMin.getValue());
					Main.clinic.scheduleAppointment(p, d, Status.ONGOING, date);
					try {
						Main.writeToAppointmentFile();
						Main.clinic.getAppointments().clear();
						Main.readFromAppointmentsFile();
						loadNewScene("Appointment");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).run();
		} catch (NullPointerException e) {
			Toast.makeText(primaryStage, "All Fields Must Be Filled", 1000, 500, 500);
		}
	}
	
	private void loadNewScene(String fileName) {
		Fxmloader newScene = new Fxmloader();
		Pane view = newScene.loadPage(fileName);
		root.getChildren().removeAll(root.getChildren());
		root.getChildren().add(view);
	}
}
