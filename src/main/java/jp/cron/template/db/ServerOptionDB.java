package jp.cron.template.db;

import jp.cron.jdalib.db.BaseDB;
import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerOptionDB extends BaseDB {
    public static ServerOption get(Guild g) {
        if (!Files.isDirectory(Paths.get("servers"))){
            try {
                Files.createDirectory(Paths.get("servers"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (Files.exists(getFilePath(g.getIdLong()))) {
            return (ServerOption) load(getFileName(g.getIdLong()), ServerOption.class);
        } else {
            ServerOption obj = new ServerOption(g.getIdLong());
            save(getFileName(g.getIdLong()), obj);
            return obj;
        }
    }

    public static void save(ServerOption obj){
        save(
                getFileName(obj.guild_id),
                obj
        );
    }

    public static Path getFilePath(Long guild_id) {
        return Paths.get(
                getFileName(guild_id)
        );
    }

    public static String getFileName(Long guild_id){
        return "servers/"+guild_id+".json";
    }
}
