package jp.cron.template.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.util.FormatUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NowPlayingEvent {

    public static void invoke(MessageReceivedEvent event){
        GuildMusicManager manager = GuildMusicManager.getGuildAudioPlayer(event.getGuild());

        AudioTrack nowplaying = manager.player.getPlayingTrack();

        if (nowplaying != null) {
            event.getMessage().reply(FormatUtil.formatTrackToNowPlaying(event.getGuild(), nowplaying).build()).queue();
        } else {
            event.getMessage().reply("現在再生中の曲はありません。").queue();
        }

    }
}
