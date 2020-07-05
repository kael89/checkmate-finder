package org.kkarvounis.checkmatefinder.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RawJsonSerializer.class)
@JsonDeserialize(using = RawJsonDeserializer.class)
public class RawJson {
    public final String value;

    RawJson(String value) {
        this.value = value;
    }
}
