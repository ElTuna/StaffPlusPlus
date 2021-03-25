package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@IocBean
public class PermissionHandler {
    private Options options;

    public PermissionHandler(Options options) {
        this.options = options;
    }

    public boolean has(Player player, String permission) {
        boolean hasPermission = false;

        if (player != null) {
            hasPermission = player.hasPermission(permission) || isOp(player);
        }

        return hasPermission;
    }

    public boolean hasAny(CommandSender player, String... permissions) {
        return Arrays.stream(permissions).anyMatch(permission -> this.has(player, permission));
    }

    public boolean hasAny(CommandSender player, List<String> permissions) {
        return permissions.stream().anyMatch(permission -> this.has(player, permission));
    }

    public void validate(CommandSender player, String permission) {
        if (!has(player, permission)) {
            throw new NoPermissionException();
        }
    }

    public boolean hasOnly(Player player, String permission) {
        boolean hasPermission = false;

        if (player != null) {
            hasPermission = player.hasPermission(permission) && !player.isOp();
        }

        return hasPermission;
    }

    public boolean has(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || isOp(sender);
    }

    public boolean isOp(Player player) {
        return player.isOp();
    }

    public boolean isOp(CommandSender sender) {
        return sender.isOp();
    }

    public int getStaffCount() {
        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (has(player, options.permissionMember)) {
                count++;
            }
        }

        return count;
    }
}