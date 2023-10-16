package net.snurkle.t962interface.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.snurkle.t962interface.DaemonThreadFactory;
import net.snurkle.t962interface.events.*;
import net.snurkle.t962interface.service.FXMLLoaderService;

/**
 * Main controller, all the UI is contained here
 */
public class MainController {

	private final FXMLLoaderService fxmlLoaderService;
	private final EventBus eventBus;

	@FXML
	private MenuItem menuitemClose;

	@FXML
	private MenuItem menuitemAbout;

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

		// Exit menu item
		menuitemClose.setOnAction(event -> eventBus.post(new ProgramExitEvent()));

//		// Redirect stdout stderr to the textarea output
//		OutputStream out = new OutputStream() {
//			@Override
//			public void write(int b) throws IOException {
//				textareaOutput.appendText(String.valueOf((char)b));
//			}
//		};
//
//		System.setOut(new PrintStream(out, true));
//		System.setErr(new PrintStream(out, true));



	}

	/**
	 * Exit the program.  It's heavy-handed in that it doesn't wait for any processes to finish up cleanly
	 */
	@Subscribe
	public void onEvent(ProgramExitEvent event){
		System.out.println("Exit!");
		Platform.exit();
		System.exit(0);
	}

}
