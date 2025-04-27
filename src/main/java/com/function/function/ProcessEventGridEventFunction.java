package com.function.function;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.google.gson.JsonElement;
import java.util.logging.Logger;

public class ProcessEventGridEventFunction {

    @FunctionName("ProcessEventGridEvent")
    public void run(
        @EventGridTrigger(name = "eventGridEvent") String content,
        final ExecutionContext context) {

        Logger logger = context.getLogger();
        logger.info("Función consumidora ejecutada — mensaje bruto: " + content);

        Gson gson = new Gson();
        JsonObject eventGridEvent = gson.fromJson(content, JsonObject.class);

        String eventType = eventGridEvent.get("eventType").getAsString();

        // ⚡ Aquí corregimos: no asumimos que data es siempre objeto
        JsonElement dataElement = eventGridEvent.get("data");
        
        if (dataElement.isJsonObject()) {
            JsonObject data = dataElement.getAsJsonObject();
            logger.info("Data recibida como objeto: " + data.toString());
        } else {
            String data = dataElement.getAsString();
            logger.info("Data recibida como string: " + data);
        }

        logger.info("Tipo de evento: " + eventType);
    }
}