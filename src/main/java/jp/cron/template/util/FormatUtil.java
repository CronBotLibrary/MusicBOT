package jp.cron.template.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jp.cron.template.audio.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class FormatUtil {

    public static MessageBuilder formatTrackToNowPlaying(Guild guild, AudioTrack track) {
        MessageBuilder mb = new MessageBuilder();
        mb.append(FormatUtil.noMentionFilter("**現在再生中: in <#"+guild.getSelfMember().getVoiceState().getChannel().getId()+">**"));
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(guild.getSelfMember().getColor());
        AudioPlayer audioPlayer = GuildMusicManager.getGuildAudioPlayer(guild).player;
        User requester = (User) track.getUserData();
        eb.setAuthor(requester.getName() + "#" + requester.getDiscriminator(), null, requester.getEffectiveAvatarUrl());
        try
        {
            eb.setTitle(track.getInfo().title, track.getInfo().uri);
        }
        catch(Exception e)
        {
            eb.setTitle(track.getInfo().title);
        }

        if(track instanceof YoutubeAudioTrack)
        {
            eb.setThumbnail("https://img.youtube.com/vi/"+track.getIdentifier()+"/mqdefault.jpg");
        }

        if(track.getInfo().author != null && !track.getInfo().author.isEmpty())
            eb.setFooter("Source: " + track.getInfo().author, null);

        double progress = (double)track.getPosition()/track.getDuration();
        eb.setDescription((audioPlayer.isPaused() ? "⏸" : "▶")
                + " "+FormatUtil.progressBar(progress)
                + " `[" + FormatUtil.formatTime(track.getPosition()) + "/" + FormatUtil.formatTime(track.getDuration()) + "]` "
                + FormatUtil.volumeIcon(audioPlayer.getVolume()));

        return mb.setEmbeds(eb.build());
    }

    public static String noMentionFilter(String input)
    {
        return input.replace("\u202E","")
                .replace("@everyone", "@\u0435veryone") // cyrillic letter e
                .replace("@here", "@h\u0435re") // cyrillic letter e
                .trim();
    }

    public static String progressBar(double percent)
    {
        String str = "";
        for(int i=0; i<12; i++)
            if(i == (int)(percent*12))
                str+="\uD83D\uDD18"; // 🔘
            else
                str+="▬";
        return str;
    }

    public static String formatTime(long duration)
    {
        if(duration == Long.MAX_VALUE)
            return "LIVE";
        long seconds = Math.round(duration/1000.0);
        long hours = seconds/(60*60);
        seconds %= 60*60;
        long minutes = seconds/60;
        seconds %= 60;
        return (hours>0 ? hours+":" : "") + (minutes<10 ? "0"+minutes : minutes) + ":" + (seconds<10 ? "0"+seconds : seconds);
    }
    public static String volumeIcon(int volume)
    {
        if(volume == 0)
            return "\uD83D\uDD07"; // 🔇
        if(volume < 30)
            return "\uD83D\uDD08"; // 🔈
        if(volume < 70)
            return "\uD83D\uDD09"; // 🔉
        return "\uD83D\uDD0A";     // 🔊
    }

}
