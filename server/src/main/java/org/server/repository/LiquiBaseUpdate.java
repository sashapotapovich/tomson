package org.server.repository;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
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
    private void init() throws SQLException {
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
            Path path = Paths.get(pathToChangelog.toURI());
            liquibase = new Liquibase(path.toFile().getAbsolutePath(),
                                      new FileSystemResourceAccessor(), database);
            liquibase.validate();
            liquibase.update(new Contexts("test"));
        } catch (URISyntaxException e) {
            log.info("ChangeSet not found:", e);
        } catch (LiquibaseException e) {
            c.rollback();
            log.info("Liquibase update failed:", e);
        } finally {
            if (c != null) {
                session.close();
                c.close();
            }
        }
    }

}
