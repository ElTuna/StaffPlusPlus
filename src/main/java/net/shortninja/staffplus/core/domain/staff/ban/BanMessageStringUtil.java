package net.shortninja.staffplus.core.domain.staff.ban;

import net.shortninja.staffplus.core.common.JavaUtils;

public class BanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, String target, String issuerName, String reason, Long endTimestamp) {
        String result = message;
        if (target != null) result = result.replace("%target%", target);
        if (issuerName != null) result = result.replace("%issuer%", issuerName);
        if (reason != null) result = result.replace("%reason%", reason);
        if (endTimestamp != null) result = result.replace("%duration%", JavaUtils.toHumanReadableDuration(JavaUtils.getDuration(endTimestamp)));
        return result;
    }
}
