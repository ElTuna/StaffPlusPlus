package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RejectAppealAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().iocContainer.get(Messages.class);
    private final MessageCoordinator messageCoordinator = StaffPlus.get().iocContainer.get(MessageCoordinator.class);
    private final SessionManagerImpl sessionManager = StaffPlus.get().iocContainer.get(SessionManagerImpl.class);
    private final AppealService appealService = StaffPlus.get().iocContainer.get(AppealService.class);
    private final Options options = StaffPlus.get().iocContainer.get(Options.class);

    private final int appealId;

    public RejectAppealAction(int appealId) {
        this.appealId = appealId;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (options.appealConfiguration.isResolveReasonEnabled()) {
            messageCoordinator.send(player, "&1==================================================", messages.prefixWarnings);
            messageCoordinator.send(player, "&6        You have chosen to reject this appeal", messages.prefixWarnings);
            messageCoordinator.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixWarnings);
            messageCoordinator.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixWarnings);
            messageCoordinator.send(player, "&1==================================================", messages.prefixWarnings);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixWarnings);
                    return;
                }
                appealService.rejectAppeal(player, appealId, message);
            });
        } else {
            appealService.rejectAppeal(player, appealId);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}