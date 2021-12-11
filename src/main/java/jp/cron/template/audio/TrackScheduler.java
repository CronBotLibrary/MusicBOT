package jp.cron.template.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import jp.cron.template.db.ServerOption;
import jp.cron.template.db.ServerOptionDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    private LoopStatus loop = LoopStatus.NONE;
    public TextChannel ch;
    public long lastTime;
    public Guild guild;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.lastTime = 0;
        this.guild = guild;
    }

    public void setChannel(TextChannel ch){
        this.ch = ch;
    }

    public void setLoop(LoopStatus st) {
        this.loop = st;

        ServerOption opt = ServerOptionDB.get(guild);
        opt.setLoop(st);
        ServerOptionDB.save(opt);
    }

    public LoopStatus getLoop() {
        return this.loop;
    }


    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public AudioTrack nextTrack() {
        AudioTrack track = queue.poll();
        if (track == null){
            Long time = new Date().getTime();
            lastTime = time;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            AudioTrack nowplaying = player.getPlayingTrack();
                            if (time == lastTime && nowplaying==null){
                                ch.sendMessage("最後の曲から10分経過したため、自動で脱退します。");
                                guild.getAudioManager().closeAudioConnection();
                            }
                        }
                    },
                    10000
            );
            return null;
        } else {
            player.startTrack(track, false);
            return track;
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (loop == LoopStatus.NONE) {
                nextTrack();
            } else if (loop == LoopStatus.QUEUE ){
                queue(track);
                nextTrack();
            } else if (loop == LoopStatus.SINGLE ){
                player.startTrack(track, false);
            }
        } else if (endReason == AudioTrackEndReason.LOAD_FAILED ){
            ch.sendMessage(":x: エラーが発生したため、現在再生中の曲を終了します。");
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        ch.sendMessage(":o: 次の曲: **`"+track.getInfo().title+"`**");
    }
}