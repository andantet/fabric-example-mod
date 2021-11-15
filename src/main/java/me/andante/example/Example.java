package me.andante.example;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Example implements ModInitializer {
    public static final String MOD_ID   = "example";
    public static final String MOD_NAME = "Example";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing {}", MOD_NAME);

        //

		LOGGER.info("Initialized {}", MOD_NAME);
	}
}
