package jp.cron.template.db;

import jp.cron.template.Main;
import jp.cron.template.audio.LoopStatus;
import net.dv8tion.jda.api.entities.Guild;

import java.io.Serializable;

public class ServerOption {
    final Long guild_id;
//    public String prefix = Main.defaultPrefix;
    public LoopStatus loop;

    public ServerOption(Long id){
        this.guild_id = id;
        this.loop = LoopStatus.NONE;
    }

    public void setLoop(LoopStatus status){
        this.loop = status;
    }

    public LoopStatus getLoop() {
        return loop;
    }

}
