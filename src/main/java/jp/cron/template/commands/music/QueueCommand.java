package jp.cron.template.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.audio.LoopStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.BlockingQueue;

public class QueueCommand {
    public static void invoke(MessageReceivedEvent e){
        GuildMusicManager manager = GuildMusicManager.getGuildAudioPlayer(e.getGuild());
        BlockingQueue<AudioTrack> queue = manager.scheduler.queue;
        AudioTrack nowplaying = manager.player.getPlayingTrack();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (nowplaying == null) {
            embedBuilder.setTitle("キューはありません。");
        } else if (manager.scheduler.getLoop() == LoopStatus.NONE && queue.isEmpty()){
            embedBuilder.setTitle("キューはありません。");
        } else if (manager.scheduler.getLoop() == LoopStatus.SINGLE) {
            embedBuilder.setTitle("１曲をリピートしています。");
            embedBuilder.addField(nowplaying.getInfo().title, "", false);
        } else if (manager.scheduler.getLoop() == LoopStatus.QUEUE){
            embedBuilder.setTitle("以下の曲をリピートしています。");
            if (manager.player.isPaused()){
                embedBuilder.addField("現在一時停止中:  "+nowplaying.getInfo().title, "", false);
            } else {
                embedBuilder.addField("現在再生中: ▶ "+nowplaying.getInfo().title, "", false);
            }
            Integer count = 1;
            for (AudioTrack audioTrack : queue) {
                embedBuilder.addField(count+". "+audioTrack.getInfo().title, "", false);
            }
        } else {
            embedBuilder.setTitle("現在のキュー");
            if (queue.isEmpty()){
                embedBuilder.addField("キューはありません。", "", false);
            } else {
                if (manager.player.isPaused()){
                    embedBuilder.addField("現在一時停止中:  "+nowplaying.getInfo().title, "", false);
                } else {
                    embedBuilder.addField("現在再生中: ▶ "+nowplaying.getInfo().title, "", false);
                }
                Integer count = 1;
                for (AudioTrack audioTrack : queue) {
                    embedBuilder.addField(count+". "+audioTrack.getInfo().title, "", false);
                }
            }
        }
        e.getMessage().replyEmbeds(embedBuilder.build()).queue();
    }
}
