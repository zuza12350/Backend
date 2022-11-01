package pl.edu.pjwstk.project.firstAid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class firstAidService implements firstAidRepository{

    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setFirstAidFromFile() {
        byte[] bytes = ipfsService.loadFile("firstAid");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public JsonObject getFirstAidContent() {
        return jsonObject;
    }




}
