package jp.cron.template.commands;

import jp.cron.jdalib.command.Alias;
import jp.cron.jdalib.command.Command;
import jp.cron.jdalib.command.entity.Category;
import jp.cron.template.Main;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.commands.music.*;
import jp.cron.template.db.ServerOption;
import jp.cron.template.db.ServerOptionDB;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Category.Declaration(name = "Music Command", prefix = "$", description = "音楽を操作するためのコマンド")
public class MusicCategory extends Category {

    public boolean precall(MessageReceivedEvent e) {
        GuildMusicManager musicManager = GuildMusicManager.getGuildAudioPlayer(e.getGuild());
        musicManager.scheduler.setChannel(e.getTextChannel());

        ServerOptionDB.get(e.getGuild());

        return e.getAuthor().getIdLong() != 759399718919864341L;
    }


    @Command(value = "play", description = "キーワード・URLまたは添付ファイルから音楽を再生します。")
    @Alias({"p"})
    public void play(MessageReceivedEvent e){
        PlayCommand.invoke(e);
    }

    @Command(value = "nowplaying", description = "現在再生中の音楽を表示します。")
    @Alias({"np"})
    public void nowplaying(MessageReceivedEvent e){
        NowPlayingEvent.invoke(e);
    }

    @Command(value = "queue", description = "再生待ちの音楽を一覧表示します。")
    @Alias({"q"})
    public void queue(MessageReceivedEvent e){
        QueueCommand.invoke(e);
    }

    @Command(value = "skip", description = "現在再生中の曲を飛ばします。")
    @Alias({"s"})
    public void skip(MessageReceivedEvent e){
        SkipCommand.execute(e);
    }

    @Command(value = "loop", description = "再生中の曲のみを、または再生待ちの音楽全てを繰り返します。")
    @Alias({"repeat", "l"})
    public void loop(MessageReceivedEvent e){
        LoopCommand.execute(e);
    }

    @Command(value = "volume", description = "再生する音量を設定します。")
    @Alias({"vol"})
    public void volume(MessageReceivedEvent e){
        VolumeCommand.execute(e);
    }

    @Command(value = "jump", description = "Comming soon... ( 開発中 )")
    public void jump(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "remove", description = "Comming soon... ( 開発中 )")
    @Alias({"delete"})
    public void remove(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "search", description = "Comming soon... ( 開発中 )")
    @Alias({"sh"})
    public void search(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "shuffle", description = "Comming soon... ( 開発中 )")
    public void shuffle(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "pause", description = "Comming soon... ( 開発中 )")
    public void pause(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "stop", description = "Comming soon... ( 開発中 )")
    public void stop(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "join", description = "Comming soon... ( 開発中 )")
    @Alias({"connect"})
    public void join(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(value = "disconnect", description = "Comming soon... ( 開発中 )")
    public void disconnect(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }


    @Override
    public void init() {
        this.setPrefix(Main.defaultPrefix );
    }
}
