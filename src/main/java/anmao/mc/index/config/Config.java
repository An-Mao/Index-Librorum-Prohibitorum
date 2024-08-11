package anmao.mc.index.config;

import anmao.dev.core.json.JsonConfig;
import anmao.mc.index.Index;
import com.google.gson.reflect.TypeToken;

public class Config extends JsonConfig<ConfigData> {
    public static final String filePath = Index.ConfigDir + "config.json";
    public static final Config instance = new Config();
    public Config() {
        super(filePath, """
                {
                  "indexMpUse": 5
                }""", new TypeToken<>(){});
    }
}
