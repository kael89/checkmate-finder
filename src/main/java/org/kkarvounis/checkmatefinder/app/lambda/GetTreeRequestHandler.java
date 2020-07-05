package org.kkarvounis.checkmatefinder.app.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.kkarvounis.checkmatefinder.app.api.GetTreeApi;

import java.util.HashMap;

public class GetTreeRequestHandler implements RequestHandler<GetTreeRequest, GetTreeResponse> {
    public GetTreeResponse handleRequest(GetTreeRequest request, Context context) {
        HashMap<String, String> input = new HashMap<String, String>() {{
            put("type", request.getType());
            put("board", request.getBoard());
            put("startingColor", request.getStartingColor());
            put("depth", request.getDepth());
        }};

        HashMap<String, Object> output = (new GetTreeApi()).run(input);
        return new GetTreeResponse(output.get("data"), (String) output.get("error"));
    }
}
