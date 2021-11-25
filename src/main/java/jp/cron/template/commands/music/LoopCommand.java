package jp.cron.template.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.audio.LoopStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoopCommand {
    public static void execute(MessageReceivedEvent e) {
        GuildMusicManager manager = GuildMusicManager.getGuildAudioPlayer(e.getGuild());
        AudioTrack nowplaying = manager.player.getPlayingTrack();
        LoopStatus status = manager.scheduler.getLoop();

        if (nowplaying == null) {
            e.getMessage().reply("現在再生中の曲はありません。").queue();
        } else if (status == LoopStatus.NONE) {
            manager.scheduler.setLoop(LoopStatus.SINGLE);
            e.getMessage().reply("現在再生中の曲をリピートします。").queue();
        } else if (status == LoopStatus.SINGLE) {
            manager.scheduler.setLoop(LoopStatus.QUEUE);
            e.getMessage().reply("現在のキューをリピートします。").queue();
        } else if (status == LoopStatus.QUEUE) {
            manager.scheduler.setLoop(LoopStatus.NONE);
            e.getMessage().reply("リピートしません。").queue();
        }
    }
}
