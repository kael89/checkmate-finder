package org.kkarvounis.checkmatefinder.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RawJsonSerializer extends StdSerializer<RawJson> {
    public RawJsonSerializer() {
        super(RawJson.class);
    }

    @Override
    public void serialize(RawJson json, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (json.value == null) {
            generator.writeNull();
        } else {
            generator.writeRawValue(json.value);
        }
    }
}
