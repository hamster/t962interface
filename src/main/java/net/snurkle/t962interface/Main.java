package net.snurkle.t962interface;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.snurkle.t962interface.service.FXMLLoaderService;

public class Main extends Application {

	Stage mainStage;

	private FXMLLoaderService fxmlLoaderService;
	private EventBus eventBus;

	@Override
	public void init(){
		// Setup the dependency injection
		final Injector injector = Guice.createInjector(new GuiceModule());
		fxmlLoaderService = injector.getInstance(FXMLLoaderService.class);
		eventBus = injector.getInstance(EventBus.class);
	}

    @Override
    public void start(Stage stage) {

		System.out.println("Startup");

		// Register to the event bus
		eventBus.register(this);

    	// Load the main stage.  Is not shown until the splash fires the event that it is done
		final Parent root = fxmlLoaderService.load("/net/snurkle/t962interface/fxml/Main.fxml");
		mainStage = new Stage(StageStyle.DECORATED);
		mainStage.setTitle("DC801 Badge Programmer - " + Defines.version);
		//mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/org/dc801/programmer/style/icon.png")));
		mainStage.setScene(new Scene(root));
		mainStage.setResizable(false);
		mainStage.setMaxWidth(1024);
		mainStage.setMinWidth(1024);
		mainStage.setMaxHeight(1200);
		mainStage.sizeToScene();
		mainStage.show();
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				System.out.println("Stage is closing");
				System.exit(0);
			}
		});

    }

    public static void main(String[] args) {
        launch(args);
    }

}
