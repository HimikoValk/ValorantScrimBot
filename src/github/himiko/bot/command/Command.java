package github.himiko.bot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public abstract class Command {
    private final String commandName;
    private final String commandDescription;

    private final Permission permissionLevel;

    private final OptionType optionType;

    private String[] alias;
    private String[] args;

    public boolean isAlias = false;

    private CommandCategory category;

    public Command(String commandName, String commandDescription,Permission permissionLevel)
    {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.permissionLevel = permissionLevel;
        this.optionType = null;
    }

    public Command(String commandName, String commandDescription, OptionType optionType,String... args)
    {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.permissionLevel = null;
        this.args = args;
        this.optionType = optionType;
    }

    public Command(String commandName, String commandDescription)
    {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.permissionLevel = null;
        this.args = null;
        this.optionType = OptionType.STRING;
    }

    public Command(String commandName, String commandDescription, OptionType optionType, Permission permissionLevel, String... args)
    {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.permissionLevel = permissionLevel;
        this.args = args;
        this.optionType = optionType;
    }

    public Command(String commandName, String commandDescription, Permission permissionLevel, String... alias)
    {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.permissionLevel = permissionLevel;
        this.alias = alias;
        this.optionType = null;
    }

    public abstract void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author);

    public boolean match(String name)
    {
        for(String alias : alias)
        {
            if(alias.equalsIgnoreCase(name)) return true;
        }
        return this.commandName.equalsIgnoreCase(name);
    }

    public boolean hasArguments()
    {
        if(args == null)
        {
            return false;
        }

        return true;
    }

    public String[] getAlias()
    {return  alias;}


    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setCategory(CommandCategory category) {
        this.category = category;
    }


    public Permission getPermissionLevel() {
        return permissionLevel;
    }
}
