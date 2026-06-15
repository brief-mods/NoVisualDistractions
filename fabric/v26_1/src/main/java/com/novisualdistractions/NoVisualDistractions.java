package com.novisualdistractions;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoVisualDistractions implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("NoVisualDistractions");

    @Override
    public void onInitializeClient() {
        LOGGER.info("NoVisualDistractions mod initialized!");
    }
}
