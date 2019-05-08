package org.kkarvounis.chasemate.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class RawJsonDeserializer extends StdDeserializer<RawJson> {
    public RawJsonDeserializer() {
        super(RawJson.class);
    }

    @Override
    public RawJson deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return new RawJson(parser.getCodec().readTree(parser).toString());
    }
}
