package net.snurkle.t962interface;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import net.snurkle.t962interface.service.FXMLLoaderService;
import net.snurkle.t962interface.service.impl.FXMLLoaderServiceImpl;

/**
 *  Guice dependency injection to inject the eventbus in all the modules
 */
public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).asEagerSingleton();
        bind(FXMLLoaderService.class).to(FXMLLoaderServiceImpl.class).asEagerSingleton();
    }

}
