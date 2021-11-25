package jp.cron.template.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.Main;
import jp.cron.template.audio.AudioHandler;
import jp.cron.template.audio.GuildMusicManager;
import jp.cron.template.util.FormatUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PlayCommand {
    public static void invoke(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ", 2);
        GuildMusicManager musicManager = GuildMusicManager.getGuildAudioPlayer(event.getGuild());

        if (args.length < 2) {
            if (event.getMessage().getAttachments().isEmpty()){
                event.getMessage().reply("添付ファイル、URLまたはキーワードを指定してください。").queue();
            } else {
                event.getMessage().reply("添付ファイルから再生します...").queue(m -> {
                    Main.INSTANCE.audioManager.loadItemOrdered(event.getGuild(), event.getMessage().getAttachments().get(0).getUrl(), new ResultHandler(event, m, false));
                });
            }
        } else {
            event.getMessage().reply("**`"+args[1]+"`**を読み込みます...").queue(m -> {
                Main.INSTANCE.audioManager.loadItemOrdered(event.getGuild(), args[1], new ResultHandler(event, m, false));
            });
        }
    }

    private static class ResultHandler implements AudioLoadResultHandler {

        private MessageReceivedEvent event;
        private Message msg;
        private Boolean search;

        public ResultHandler(MessageReceivedEvent e, Message m, Boolean search) {
            event = e;
            msg = m;
            this.search = search;
        }

        private void loadSingle(AudioTrack track)
        {
            track.setUserData(event.getAuthor());
            GuildMusicManager musicManager = GuildMusicManager.getGuildAudioPlayer(event.getGuild());
            musicManager.scheduler.queue(track);
            msg.editMessage(":o: **`"+track.getInfo().title+"`**をキューに追加しました。").queue();
        }

        private void loadPlaylist(AudioPlaylist playlist)
        {
            GuildMusicManager musicManager = GuildMusicManager.getGuildAudioPlayer(event.getGuild());

            if (search) {
                loadSingle(playlist.getTracks().get(0));
            } else {
                Integer count = 0;
                if (playlist.getSelectedTrack()!=null){
                    playlist.getSelectedTrack().setUserData(event.getAuthor());
                    musicManager.scheduler.queue(playlist.getSelectedTrack());
                    count++;
                }
                for (AudioTrack track : playlist.getTracks()) {
                    track.setUserData(event.getAuthor());
                    musicManager.scheduler.queue(track);
                    count++;
                }
                msg.editMessage(":o: "+count+"曲をキューに追加しました。").queue();
            }
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            if (joinVCifNotJoined())
                loadSingle(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            if (joinVCifNotJoined())
                loadPlaylist(playlist);
        }

        @Override
        public void noMatches() {
            if (!search) {
                String[] args = event.getMessage().getContentRaw().split(" ", 2);
                Main.INSTANCE.audioManager.loadItemOrdered(event.getGuild(), "ytsearch:"+args[1], new ResultHandler(event, msg, true));
            } else {
                msg.editMessage("指定した曲にマッチするものが見つかりませんでした。").queue();
            }
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            if(exception.severity== FriendlyException.Severity.COMMON)
                msg.editMessage(":x: ローディング中にエラーが発生しました: "+exception.getMessage()).queue();
            else
                msg.editMessage(":x: ローディング中にエラーが発生しました。").queue();
        }

        private boolean joinVCifNotJoined() {
            Member member = event.getGuild().retrieveMemberById(event.getAuthor().getIdLong()).complete();

            if (member == null){
                msg.editMessage(":x: あなたが参加しているVCを取得中にエラーが発生しました。。").queue();
                return false;
            } else if (event.getGuild().getSelfMember().getVoiceState().getChannel()==null && member.getVoiceState().getChannel()==null){
                msg.editMessage(":x: VCに参加してください。").queue();
                return false;
            } else if (event.getGuild().getSelfMember().getVoiceState().getChannel()==null){
                event.getGuild().getAudioManager().openAudioConnection(
                        event.getGuild().getMember(event.getAuthor()).getVoiceState().getChannel()
                );
                return true;
            } else {
                return true;
            }
        }
    }
}
