package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqlitePlayerIpRepository extends AbstractSqlPlayerIpRepository {

    public SqlitePlayerIpRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}