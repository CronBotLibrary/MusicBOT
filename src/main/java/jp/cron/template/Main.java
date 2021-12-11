package jp.cron.template;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.nico.NicoAudioSourceManager;
import jp.cron.jdalib.JDALib;
import jp.cron.template.commands.AdminCategory;
import jp.cron.template.commands.GeneralCategory;
import jp.cron.template.commands.MusicCategory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Main {
    public final static GatewayIntent[] INTENTS = {GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES};

    public static Main INSTANCE;
    public AudioPlayerManager audioManager;
    public JDA jda;
    public JDALib jdaLib;

    public static String defaultPrefix;

    public Main() {
        this.INSTANCE = this;
    }


    public static void main(String[] args) throws LoginException {
        new Main().run();
    }

    public void run() throws LoginException {
        jdaLib = new JDALib();
        jdaLib.configManager.read();
        String token = jdaLib.configManager.getString("TOKEN");

        if (token == null || token == "SET TOKEN HERE"){

            jdaLib.configManager.setValue("TOKEN", "SET TOKEN HERE");
            jdaLib.configManager.setValue("prefix", "!");
            jdaLib.configManager.setValue("NICO_EMAIL", "contact@example.com");
            jdaLib.configManager.setValue("NICO_PASSWORD", "password");
            System.out.println("Please set token in config.txt!");
            return;

        } else {
            defaultPrefix = jdaLib.configManager.getString("prefix");

            String nicoEmail = jdaLib.configManager.getString("NICO_EMAIL");
            String nicoPassword = jdaLib.configManager.getString("NICO_PASSWORD");

            audioManager = new DefaultAudioPlayerManager();
            audioManager.registerSourceManager(
                    new NicoAudioSourceManager(
                            nicoEmail,
                            nicoPassword
                    )
            );

            AudioSourceManagers.registerRemoteSources(audioManager);

            jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .build();

            jdaLib.setJDA(jda);
            jdaLib.setActivity(true, defaultPrefix+"help | {serverCount}サーバーで稼働中");
            jdaLib.commandManager.register(new GeneralCategory());
            jdaLib.commandManager.register(new MusicCategory());
            jdaLib.commandManager.register(new AdminCategory());
        }
    }
}
