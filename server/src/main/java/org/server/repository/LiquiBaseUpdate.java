package org.server.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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
            File file = null;
            String resource = "/db/changelog.xml";
            URL res = getClass().getResource(resource);
            if (res.getProtocol().equals("jar")) {
                try {
                    InputStream input = getClass().getResourceAsStream(resource);
                    file = File.createTempFile("tempfile", ".xml");
                    OutputStream out = new FileOutputStream(file);
                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = input.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    out.close();
                    file.deleteOnExit();
                } catch (IOException ex) {
                    log.error("Temp file exception",ex);
                }
            } else {
                //this will probably work in your IDE, but not from a JAR
                file = new File(res.getFile());
            }
            if (file != null && !file.exists()) {
                log.error("Error: File " + file + " not found!");
            }
            liquibase = new Liquibase(file.getAbsolutePath(),
                                      new FileSystemResourceAccessor(), database);
            liquibase.validate();
            liquibase.update(new Contexts("test"));
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
