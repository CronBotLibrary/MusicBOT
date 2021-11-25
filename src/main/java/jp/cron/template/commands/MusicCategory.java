package jp.cron.template.commands;

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


    @Command(name = "play", description = "キーワード・URLまたは添付ファイルから音楽を再生します。")
    public void play(MessageReceivedEvent e){
        PlayCommand.invoke(e);
    }

    @Command(name = "nowplaying", description = "現在再生中の音楽を表示します。")
    public void nowplaying(MessageReceivedEvent e){
        NowPlayingEvent.invoke(e);
    }

    @Command(name = "queue", description = "再生待ちの音楽を一覧表示します。")
    public void queue(MessageReceivedEvent e){
        QueueCommand.invoke(e);
    }

    @Command(name = "skip", description = "現在再生中の曲を飛ばします。")
    public void skip(MessageReceivedEvent e){
        SkipCommand.execute(e);
    }

    @Command(name = "loop", description = "再生中の曲のみを、または再生待ちの音楽全てを繰り返します。")
    public void loop(MessageReceivedEvent e){
        LoopCommand.execute(e);
    }

    @Command(name = "volume", description = "再生する音量を設定します。")
    public void volume(MessageReceivedEvent e){
        VolumeCommand.execute(e);
    }

    @Command(name = "jump", description = "Comming soon... ( 開発中 )")
    public void jump(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "remove", description = "Comming soon... ( 開発中 )")
    public void remove(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "search", description = "Comming soon... ( 開発中 )")
    public void search(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "shuffle", description = "Comming soon... ( 開発中 )")
    public void shuffle(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "pause", description = "Comming soon... ( 開発中 )")
    public void pause(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "stop", description = "Comming soon... ( 開発中 )")
    public void stop(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "join", description = "Comming soon... ( 開発中 )")
    public void join(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }

    @Command(name = "disconnect", description = "Comming soon... ( 開発中 )")
    public void disconnect(MessageReceivedEvent e){
        e.getMessage().reply("Comming soon...").queue();
        // TODO: jump command
    }


    @Override
    public void init() {
        this.setPrefix(Main.defaultPrefix );
    }
}
