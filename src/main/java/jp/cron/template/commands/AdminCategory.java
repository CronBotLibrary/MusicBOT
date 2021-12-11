package jp.cron.template.commands;

import jp.cron.jdalib.command.Command;
import jp.cron.jdalib.command.entity.Category;
import jp.cron.template.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Category.Declaration(name = "Admin Command", prefix = "", description = "サーバー管理者専用コマンド")
public class AdminCategory extends Category {

    @Command(value = "setprefix", description = "Comming soon... ( 開発中 )")
    public void setprefix(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "setdj", description = "Comming soon... ( 開発中 )")
    public void setdj(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Override
    public void init() {
        this.setPrefix(Main.defaultPrefix );
    }
}
