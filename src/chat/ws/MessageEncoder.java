package chat.ws;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Dongfang on 2015/12/17.
 */
public class MessageEncoder implements Encoder.Text<Message> {

    public String encode(Message message) throws EncodeException {

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("sender", message.getSender())
                .add("content", message.getContent())
                .add("ctime", message.getCtime()).build();
        return jsonObject.toString();
    }

    public void init(EndpointConfig ec) {

    }

    public void destroy() {

    }

}