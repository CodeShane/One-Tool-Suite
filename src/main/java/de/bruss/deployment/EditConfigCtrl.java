package de.bruss.deployment;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class EditConfigCtrl {

	@FXML
	private TextField host;
	@FXML
	private TextField name;
	@FXML
	private TextField localPath;
	@FXML
	private TextField remotePath;
	@FXML
	private TextField serviceName;
	@FXML
	private TextField port;
	@FXML
	private TextField localDbName;
	@FXML
	private TextField remoteDbName;

	private DeploymentTabCtrl deploymentTabCtrl;
	private Config editConfig;

	@FXML
	protected void save(ActionEvent event) {
		if (editConfig == null) {
			Config config = new Config(localPath.getText(), remotePath.getText(), host.getText(), name.getText(), serviceName.getText(), port.getText(), localDbName.getText(), remoteDbName.getText());
			ConfigService.addConfig(config);
		} else {
			editConfig.setHost(this.host.getText());
			editConfig.setLocalPath(this.localPath.getText());
			editConfig.setRemotePath(this.remotePath.getText());
			editConfig.setName(this.name.getText());
			editConfig.setServiceName(this.serviceName.getText());
			editConfig.setPort(this.port.getText());
			editConfig.setLocalDbName(this.localDbName.getText());
			editConfig.setRemoteDbName(this.remoteDbName.getText());
			ConfigService.save(editConfig);
		}

		deploymentTabCtrl.refresh();
	}

	public void initData(DeploymentTabCtrl deploymentTabCtrl) {
		this.deploymentTabCtrl = deploymentTabCtrl;
	}

	public void initData(DeploymentTabCtrl deploymentTabCtrl, Config editConfig) {
		this.deploymentTabCtrl = deploymentTabCtrl;
		this.editConfig = editConfig;

		this.host.setText(editConfig.getHost());
		this.localPath.setText(editConfig.getLocalPath());
		this.remotePath.setText(editConfig.getRemotePath());
		this.name.setText(editConfig.getName());
		this.serviceName.setText(editConfig.getServiceName());
		this.port.setText(editConfig.getPort());
		this.localDbName.setText(editConfig.getLocalDbName());
		this.remoteDbName.setText(editConfig.getRemoteDbName());
	}

	@FXML
	protected void searchJarPath(ActionEvent event) {
		final DirectoryChooser fileChooser = new DirectoryChooser();

		File file = fileChooser.showDialog(new Stage());
		if (file != null) {
			localPath.setText(file.getAbsolutePath());
		}

	}

	@FXML
	protected void addConfig(ActionEvent event) {
		editConfig = new Config();

		this.host.setText(editConfig.getHost());
		this.localPath.setText(editConfig.getLocalPath());
		this.remotePath.setText(editConfig.getRemotePath());
		this.name.setText(editConfig.getName());
		this.serviceName.setText(editConfig.getServiceName());
		this.port.setText(editConfig.getPort());
		this.localDbName.setText(editConfig.getLocalDbName());
		this.remoteDbName.setText(editConfig.getRemoteDbName());
	}

	@FXML
	protected void delete(ActionEvent event) {
		ConfigService.remove(editConfig);
		deploymentTabCtrl.refresh();
	}
}