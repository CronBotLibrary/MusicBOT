package jp.cron.template.commands;

import jp.cron.jdalib.command.Alias;
import jp.cron.jdalib.command.Command;
import jp.cron.jdalib.command.entity.Category;
import jp.cron.template.Main;
import jp.cron.template.db.ServerOption;
import jp.cron.template.db.ServerOptionDB;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;

@Category.Declaration(name = "General Command", prefix = "", description = "全般的なコマンド")
public class GeneralCategory extends Category {

    @Command(name = "ping", description = "応答速度を計測します。")
    public void ping(MessageReceivedEvent e){
        e.getMessage().reply("Pong!").queue((Message message) -> { // the type for "result" is the T in RestAction<T>
            long ping = e.getMessage().getTimeCreated().until(message.getTimeCreated(), ChronoUnit.MILLIS);
            message.editMessage("Ping: " + ping/2  + "ms | Websocket: " + e.getJDA().getGatewayPing() + "ms").queue();
        });
    }

    @Command(name = "help", description = "ヘルプを表示します。")
    public void help(MessageReceivedEvent e){
        MessageBuilder messageBuilder = new MessageBuilder();

        for (Category category : Main.INSTANCE.jdaLib.commandManager.categories) {
            messageBuilder
                    .append(
                            "__**"+category.getName()+"**: "+category.getDescription()+"__"+"\n"
                    );
            for (Method method : category.getMethods()) {
                Command anno = method.getAnnotation(Command.class);
                Alias alias = method.getAnnotation(Alias.class);
                if (anno != null) {
                    messageBuilder.append("`" + Main.defaultPrefix + anno.name() + "` - ");
                    messageBuilder.append(anno.description());
                    messageBuilder.append("\n");
                }
            }
            messageBuilder.append("\n");
        }

        e.getMessage().getChannel().sendMessage(messageBuilder.build()).queue();
    }

    @Override
    public void init() {
        this.setPrefix(Main.defaultPrefix);
    }
}
