package net.snurkle.t962interface.controllers;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.snurkle.t962interface.DaemonThreadFactory;
import net.snurkle.t962interface.Log;
import net.snurkle.t962interface.events.*;
import net.snurkle.t962interface.service.FXMLLoaderService;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Main controller, all the UI is contained here
 */
public class MainController {

	private final FXMLLoaderService fxmlLoaderService;
	private final EventBus eventBus;

	@FXML private MenuItem menuitemClose;
	@FXML private MenuItem menuitemAbout;
	@FXML private Menu menuSerial;
	@FXML private Label statusbar;
	@FXML private TextArea textReceived;

	@FXML private Button buttonBake;
	@FXML private Button buttonStop;

	private SerialPort serialPort;

	private DaemonThreadFactory tf = new DaemonThreadFactory();;

	@Inject
	public MainController(final FXMLLoaderService fxmlLoaderService,
							final EventBus eventBus){
		this.fxmlLoaderService = fxmlLoaderService;
		this.eventBus = eventBus;
	}

	/**
	 * Initialize the UI, run at startup
	 */
	@FXML
	public void initialize() {

		menuitemClose.setOnAction(event -> eventBus.post(new ProgramExitEvent()));
		menuSerial.setOnAction(populateSerialMenu());

		statusbar.setText("Select a serial port to open");

		buttonBake.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(serialPort.isOpen()){
					String command = "bake 50";
					serialPort.writeBytes(command.getBytes(), command.length());
				}
			}
		});

		buttonStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(serialPort.isOpen()){
					String command = "stop";
					serialPort.writeBytes(command.getBytes(), command.length());
				}
			}
		});

	}

	private void addToLog(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);

		Platform.runLater(() -> textReceived.appendText(strDate.toString() + " - " + s));
		Log.info(MainController.class, strDate.toString() + " - " + s);
	}

	public EventHandler<ActionEvent> populateSerialMenu(){

		SerialPort[] ports = SerialPort.getCommPorts();

		ToggleGroup serialSelection = new ToggleGroup();
		for(SerialPort port: ports){
			RadioMenuItem item = new RadioMenuItem(port.getSystemPortName());
			item.setToggleGroup(serialSelection);
			item.setOnAction(event -> {
				MenuItem source = (MenuItem) event.getSource();
				if(serialPort != null){
					if(serialPort.isOpen()){
						serialPort.closePort();
					}
				}
				serialPort = SerialPort.getCommPort(source.getText());
				if(serialPort.openPort()){
					addToLog("Serial port " + source.getText() + " opened OK");
					statusbar.setText("Serial port " + source.getText() + " opened OK");
					serialPort.addDataListener(new SerialPortMessageListener() {
						@Override
						public int getListeningEvents() {
							return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
						}
						@Override
						public byte[] getMessageDelimiter() { return new byte[] { (byte)'\n' }; }
						@Override
						public boolean delimiterIndicatesEndOfMessage() { return true; }

						@Override
						public void serialEvent(SerialPortEvent event) {
							byte[] delimitedMessage = event.getReceivedData();
							String s = new String(delimitedMessage, StandardCharsets.UTF_8);
							addToLog(s);
						}
					});
				}
				else{
					statusbar.setText("Serial port " + source.getText() + " failed to open");
					addToLog("Serial port " + source.getText() + " failed to open");
				}
			});
			menuSerial.getItems().add(item);
		}

		return null;
	}



	/**
	 * Exit the program.  It's heavy-handed in that it doesn't wait for any processes to finish up cleanly
	 */
	@Subscribe
	public void onEvent(ProgramExitEvent event){
		System.out.println("Exit!");

		if(serialPort.isOpen()){
			serialPort.closePort();
		}

		Platform.exit();
		System.exit(0);
	}

}
