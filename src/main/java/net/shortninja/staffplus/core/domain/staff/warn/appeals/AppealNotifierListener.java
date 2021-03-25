package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.IocBean;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.database.AppealRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
public class AppealNotifierListener implements Listener {

    private final AppealRepository appealRepository;
    private final Options options;
    private final PermissionHandler permission;
    private final Messages messages;

    public AppealNotifierListener(AppealRepository appealRepository, Options options, PermissionHandler permission, Messages messages) {
        this.appealRepository = appealRepository;
        this.options = options;
        this.permission = permission;
        this.messages = messages;
        if (this.options.appealConfiguration.isEnabled()) {
            Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(PlayerJoinEvent event) {
        if (!permission.has(event.getPlayer(), options.appealConfiguration.getPermissionNotifications())) {
            return;
        }


        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            int openAppeals = appealRepository.getCountOpenAppeals();
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(PlayerJoinEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.openAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved appeals!",
            "Click to view unresolved appeals",
            options.manageWarningsConfiguration.getCommandManageAppealedWarningsGui(),
            canManageAppeal(event));
        message.send(event.getPlayer());
    }

    private boolean canManageAppeal(PlayerJoinEvent event) {
        return permission.has(event.getPlayer(), options.appealConfiguration.getApproveAppealPermission())
            || permission.has(event.getPlayer(), options.appealConfiguration.getRejectAppealPermission());
    }
}