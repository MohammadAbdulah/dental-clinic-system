package application.gui;

import Components.Card;
import Components.Toast;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Doctor;
import model.Schedule;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorWorkScheduleController implements Initializable {
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
	private ComboBox<Integer> endHr;
	@FXML
	private ComboBox<Integer> endMin;
	@FXML
	private Text doctorName;

	private Card card = new Card();
	private Stage primaryStage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateListItem();
		setComboBox();
	}

	@FXML
	private void addWorkDay(ActionEvent event) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						LocalDateTime date = LocalDateTime.of(datePicker.getValue().getYear(),
								datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth(), 0, 0);
						LocalTime start = LocalTime.of(startHr.getValue(), startMin.getValue());
						LocalTime end = LocalTime.of(endHr.getValue(), endMin.getValue());
						Schedule workSchedule = new Schedule(date, start, end);
						List<Schedule> s = new ArrayList<Schedule>();
						s.add(workSchedule);
						Main.clinic.findDoctorById(Card.doctorId).addWorkSchedule(s);
						Main.writeToDoctorFile();
						Main.clinic.getDoctors().clear();
						Main.readFromDoctorsFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			loadNewScene("DoctorWorkSchedule");
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

	private void populateListItem() {
		int i = 0;

		for (Schedule s : ((Doctor) Main.clinic.findDoctorById(Card.doctorId)).getWorkSchedule()) {
			AnchorPane view = new AnchorPane();
			int day = s.getScheduledDateTime().getDayOfMonth();
			String month = s.getScheduledDateTime().getMonth().toString().substring(0, 3);
			card.getWorkScheduleCard(view, s.getScheduledDateTime().getDayOfWeek() + "", day + "-" + month,
					s.getStartTime().toString(), s.getEndTime().toString(), i);
			list.getItems().add(view);
			i++;
		}
	}

	private void setComboBox() {
		for (int i = 0; i < 60; i++) {
			startMin.getItems().add(i);
			endMin.getItems().add(i);
		}

		for (int i = 0; i < 24; i++) {
			startHr.getItems().add(i);
			endHr.getItems().add(i);
		}
	}
	
	@FXML
	private void delete() throws IOException {
		for(int i = 0; i < list.getItems().size(); i++) {
			if(((CheckBox) list.getItems().get(i).getChildren().get(4)).isSelected()) {
				Main.clinic.deleteDoctorWorkSchedule(Card.doctorId, i);
			}
			Main.writeToDoctorFile();
			Main.clinic.getDoctors().clear();
			Main.readFromDoctorsFile();
			loadNewScene("DoctorWorkSchedule");
		}
	}
}
