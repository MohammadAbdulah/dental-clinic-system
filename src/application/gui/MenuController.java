package application.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MenuController implements Initializable {

	@FXML
	private BorderPane root;
	@FXML
	private Text month;
	@FXML
	private Text day;
	@FXML
	private Text date;
	@FXML
	private Text day1;
	@FXML
	private Text date1;
	@FXML
	private Text day2;
	@FXML
	private Text date2;
	@FXML
	private Text day3;
	@FXML
	private Text date3;
	@FXML
	private Text day4;
	@FXML
	private Text date4;
	@FXML
	private Button dashboard;
	@FXML
	private Button patient;
	@FXML
	private Button doctor;
	@FXML
	private Button appointment;
	@FXML
	private Text appointment1Text;
	@FXML
	private Text appointment2Text;
	@FXML
	private Text appointment3Text;
	
	private LocalDate today = LocalDate.now();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadNewScene("Dashboard");
		setCalender();
	}
	
	private void loadNewScene(String fileName) {
		Fxmloader newScene = new Fxmloader();
		Pane view = newScene.loadPage(fileName);
		root.setCenter(view);

		switch (fileName) {
			case "Dashboard": {
				dashboard.setStyle("-fx-background-color: #FFA146;-fx-background-radius: 11;");
				patient.setStyle("-fx-background-color: transparent;");
				doctor.setStyle("-fx-background-color: transparent;");
				appointment.setStyle("-fx-background-color: transparent;");
				break;
			}
			case "Doctor": {
				dashboard.setStyle("-fx-background-color: transparent;");
				patient.setStyle("-fx-background-color: transparent;");
				doctor.setStyle("-fx-background-color: #FFA146;-fx-background-radius: 11;");
				appointment.setStyle("-fx-background-color: transparent;");
				break;
			}
			case "Patient": {
				dashboard.setStyle("-fx-background-color: transparent;");
				patient.setStyle("-fx-background-color: #FFA146;-fx-background-radius: 11;");
				doctor.setStyle("-fx-background-color: transparent;");
				appointment.setStyle("-fx-background-color: transparent;");
				break;
			}
			case "Appointment": {
				dashboard.setStyle("-fx-background-color: transparent;");
				patient.setStyle("-fx-background-color: transparent;");
				doctor.setStyle("-fx-background-color: transparent;");
				appointment.setStyle("-fx-background-color: #FFA146;-fx-background-radius: 11;");
				break;
			}
		}
	}

	private void setCalender() {
		month.setText(getMonth(today.getMonthValue()));

		day.setText(getDay(today.getDayOfWeek().plus(1).getValue()));
		date.setText(today.getDayOfMonth() + "");

		day1.setText(getDay(today.getDayOfWeek().minus(1).getValue()));
		date1.setText(today.minusDays(2).getDayOfMonth() + "");

		day2.setText(getDay(today.getDayOfWeek().getValue()));
		date2.setText(today.minusDays(1).getDayOfMonth() + "");

		day3.setText(getDay(today.getDayOfWeek().plus(2).getValue()));
		date3.setText(today.plusDays(1).getDayOfMonth() + "");

		day4.setText(getDay(today.getDayOfWeek().plus(3).getValue()));
		date4.setText(today.plusDays(2).getDayOfMonth() + "");
		
		appointment1Text.setText(Main.clinic.getAppointments().get(0).getDate().getStartTime() + "");
		appointment2Text.setText(Main.clinic.getAppointments().get(1).getDate().getStartTime() + "");
		appointment3Text.setText(Main.clinic.getAppointments().get(2).getDate().getStartTime() + "");
	}

	private String getMonth(int n) {
		switch (n) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		}
		return "";
	}

	private String getDay(int n) {
		switch (n) {
		case 1:
			return "Su";
		case 2:
			return "Mo";
		case 3:
			return "Tu";
		case 4:
			return "We";
		case 5:
			return "Th";
		case 6:
			return "Fr";
		case 7:
			return "Sa";
		}
		return "";
	}

	@FXML
	private void handleAppointmentButton(ActionEvent event) {
		loadNewScene("Appointment");
	}
	
	@FXML
	private void handleDashboardButton(ActionEvent event) {
		loadNewScene("Dashboard");
	}
	
	@FXML
	private void handlePatientButton(ActionEvent event) {
		loadNewScene("Patient");
	}

	@FXML
	private void handleDoctorButton(ActionEvent event) {
		loadNewScene("Doctor");
	}
}
