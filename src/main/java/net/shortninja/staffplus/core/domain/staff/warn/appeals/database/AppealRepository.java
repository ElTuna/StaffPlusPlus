package net.shortninja.staffplus.core.domain.staff.warn.appeals.database;

import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppealRepository {

    List<Appeal> getAppeals(int warningId, int offset, int amount);

    void updateAppealStatus(int appealId, UUID resolverUuid, String resolveReason, AppealStatus status, AppealableType appealableType);

    Optional<Appeal> findAppeal(int appealId);

    void addAppeal(Appeal appeal, AppealableType appealableType);

    List<Appeal> getAppeals(int appealableId, AppealableType appealableType);

    int getCountOpenAppeals();

    void deleteAppeals(int appealableId, AppealableType appealableType);
}
