package dev.joncollins.mpapi.utils;

import com.google.gson.*;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface GsonTypeAdapter {
    default Gson returnAdaptedGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (value, type, context) ->
                                             new JsonPrimitive(value.format(DateTimeFormatter.ISO_DATE_TIME))
                                    )
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, context) ->
                                             LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_DATE_TIME)
                                    )
                .registerTypeAdapter(ObjectId.class, (JsonSerializer<ObjectId>) (value, type, context) ->
                                             new JsonPrimitive(value.toHexString())
                                    )
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create();
    }
}
