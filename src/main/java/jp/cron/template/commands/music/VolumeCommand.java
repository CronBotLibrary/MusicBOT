package jp.cron.template.commands.music;

import jp.cron.template.audio.GuildMusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class VolumeCommand {
    public static void execute(MessageReceivedEvent e) {
        String[] args = e.getMessage().getContentRaw().split(" ", 2);
        Integer vol = 100;

        if (args.length > 1) {
            try {
                vol = Integer.parseInt(args[1]);

                if (vol > 100 || vol < 0) {
                    e.getMessage().reply("0から100の範囲で設定してください。").queue();
                } else {
                    GuildMusicManager.getGuildAudioPlayer(e.getGuild()).player.setVolume(vol);
                    e.getMessage().reply("音量を" + vol + "に設定しました。").queue();
                }
            } catch (NumberFormatException ex) {
                e.getMessage().reply("数字を指定してください。").queue();
                return;
            }
        } else {
            e.getMessage().reply("現在のボリューム: "+GuildMusicManager.getGuildAudioPlayer(e.getGuild()).player.getVolume()).queue();
        }
    }
}
