package org.server.repository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Component
public class LiquiBaseUpdate {
    
    @Autowired
    private ConnectionProvider connectionProvider;
    
    @PostConstruct
    private void init() throws DatabaseException {
        Session session = connectionProvider.getSession();
        List<Connection> connectionForLiquibase = new ArrayList<>();
        session.doWork(connectionForLiquibase::add);
        Connection c = connectionForLiquibase.get(0);
        Liquibase liquibase = null;
        try {
            Database database = DatabaseFactory.getInstance()
                                               .findCorrectDatabaseImplementation(
                                                       new JdbcConnection(c)
                                               );
            URL pathToChangelog = LiquiBaseUpdate.class.getResource("/db/changelog.xml");
            DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog(pathToChangelog.getPath());
            liquibase = new Liquibase(databaseChangeLog,
                                      new FileSystemResourceAccessor(), database);
            liquibase.update("");
        } catch (LiquibaseException e) {
            throw new DatabaseException(e);
        } finally {
            if (c != null) {
                try {
                    c.rollback();
                    c.close();
                } catch (SQLException e) {
                    log.error("Liquibase rollback failed", e);
                }
            }
        }
    }
    
}
