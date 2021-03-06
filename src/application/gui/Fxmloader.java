package application.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class Fxmloader {
	@FXML
	private Pane view;

	@SuppressWarnings("static-access")
	public Pane loadPage(String fileName) {
		try {
			URL fileUrl = MenuController.class.getResource("/application/gui/" + fileName + ".fxml");
			
			if (fileUrl == null)
				throw new java.io.FileNotFoundException();
			
			view = new FXMLLoader().load(fileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return view;
	}
}