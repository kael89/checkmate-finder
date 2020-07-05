package org.kkarvounis.checkmatefinder.app.api;

import java.util.HashMap;

abstract class AbstractApi {
    abstract public HashMap<String, Object> run(HashMap<String, String> input);

    HashMap<String, Object> buildOutput(Object data, String error) {
        return new HashMap<String, Object>() {{
            put("data", data);
            put("error", error);
        }};
    }
}
