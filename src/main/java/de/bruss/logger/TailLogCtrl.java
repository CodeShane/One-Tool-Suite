package de.bruss.logger;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import de.bruss.Context;

public class TailLogCtrl implements Initializable {
	@FXML
	public TextArea logView;

	private TailLog tailLog;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logView.setText("Log wird geladen...");
		logView.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				logView.setScrollTop(Double.MAX_VALUE); // this will scroll to the bottom
			}
		});

		try {
			tailLog = new TailLog(Context.getEditConfigCtrl().getEditConfig(), this);
			Thread t = new Thread(tailLog);
			t.start();
		} catch (Exception e) {
			System.err.println("Tailing Log failed!");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void stop() {
		tailLog.stop();
	}

}
