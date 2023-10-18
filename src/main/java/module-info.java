module t962interface {
    requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
    requires com.google.common;
	requires com.google.guice;
	requires com.fazecast.jSerialComm;
	requires java.logging;

	opens net.snurkle.t962interface.controllers to javafx.fxml;

	exports net.snurkle.t962interface;
	exports net.snurkle.t962interface.controllers;
	exports net.snurkle.t962interface.service;
	exports net.snurkle.t962interface.service.impl;
	exports net.snurkle.t962interface.events;
}
