package net.shortninja.staffplus.core.domain.staff.mute.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentProcessor;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.time.TimeUnit;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.delayedactions.DelayArgumentExecutor;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean(conditionalOnProperty = "mute-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class TempMuteCmd extends AbstractCmd {

    private final MuteService muteService;
    private final SessionManagerImpl sessionManager;

    public TempMuteCmd(PermissionHandler permissionHandler, AuthenticationService authenticationService, Messages messages, MessageCoordinator message, PlayerManager playerManager, Options options, DelayArgumentExecutor delayArgumentExecutor, ArgumentProcessor argumentProcessor, MuteService muteService, SessionManagerImpl sessionManager) {
        super(options.muteConfiguration.getCommandTempMutePlayer(), permissionHandler, authenticationService, messages, message, playerManager, options, delayArgumentExecutor, argumentProcessor);
        this.muteService = muteService;
        this.sessionManager = sessionManager;
        setPermission(options.muteConfiguration.getPermissionMutePlayer());
        setDescription("Temporary mute a player");
        setUsage("[player] [amount] [unit] [reason]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!JavaUtils.isInteger(args[1])) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()));
        }

        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];
        String reason = JavaUtils.compileWords(args, 3);

        muteService.tempMute(sender, player, TimeUnit.getDuration(timeUnit, amount), reason);
        sessionManager.get(player.getId()).setMuted(true);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 4;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.muteConfiguration.getPermissionMuteByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Stream.of("5", "10", "15", "20")
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Stream.of(
                TimeUnit.YEAR.name(), TimeUnit.MONTH.name(),
                TimeUnit.WEEK.name(), TimeUnit.DAY.name(),
                TimeUnit.HOUR.name(), TimeUnit.MINUTE.name())
                .filter(s -> args[2].isEmpty() || s.contains(args[2]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}