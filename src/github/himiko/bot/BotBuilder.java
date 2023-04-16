package github.himiko.bot;

import github.himiko.bot.button.ButtonManager;
import github.himiko.bot.command.CommandManager;
import github.himiko.bot.events.EventHandler;
import github.himiko.system.logger.LoggerBuilder;
import github.himiko.system.scrim.channel.ChannelManager;
import github.himiko.system.scrim.logger.ScrimLogger;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class BotBuilder {

    private final Dotenv config;

    private DefaultShardManagerBuilder builder = null;

    private ShardManager shardManager;

    private final EventHandler eventHandler;

    public ScrimLogger scrimLogger;

    public final LoggerBuilder loggerBuilder;

    public static CommandManager commandManager = new CommandManager();

    public static ButtonManager buttonManager = new ButtonManager();

    public static ChannelManager channelManager = new ChannelManager();

    public BotBuilder() throws LoginException
    {
        //LoggerBuilder
        this.scrimLogger = new ScrimLogger();
        this.loggerBuilder = LoggerBuilder.createLogger(this.scrimLogger.getClass());
        //Loading config
        this.config = Dotenv.configure().load();
        //setting up bot
        this.builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        this.builder.setStatus(OnlineStatus.ONLINE);
        this.builder.setActivity(Activity.of(Activity.ActivityType.PLAYING,"Scrims | Developer himiko"));
        this.builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        this.builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        this.builder.setChunkingFilter(ChunkingFilter.ALL);
        this.builder.enableCache(CacheFlag.ONLINE_STATUS);
        //Login
        this.shardManager = builder.build();
        this.eventHandler = new EventHandler(this.shardManager);
    }


}
