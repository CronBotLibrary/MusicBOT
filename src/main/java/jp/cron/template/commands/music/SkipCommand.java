package jp.cron.template.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.audio.LoopStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCommand {
    public static void execute(MessageReceivedEvent e){
        GuildMusicManager manager = GuildMusicManager.getGuildAudioPlayer(e.getGuild());
        AudioTrack nowplaying = manager.player.getPlayingTrack();

        if (manager.scheduler.getLoop()== LoopStatus.NONE) {
            manager.scheduler.nextTrack();
            e.getMessage().reply("現在再生中の曲をスキップしました。").queue();
        } else if (manager.scheduler.getLoop() == LoopStatus.QUEUE ){
            manager.scheduler.queue(nowplaying.makeClone());
            manager.scheduler.nextTrack();
            e.getMessage().reply("次の曲にスキップしました。").queue();
        } else if (manager.scheduler.getLoop() == LoopStatus.SINGLE ){
            manager.player.startTrack(nowplaying.makeClone(), false);
            e.getMessage().reply("曲の最初に戻ります。").queue();
        }
    }
}
