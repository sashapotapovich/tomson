package org.web;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class UserAuthentication extends Authenticator {

    @Autowired
    private RegistryHolder registryHolder;
    
    @Override
    public Result authenticate(HttpExchange exch) {
        return null;
    }

    public boolean authenticate(String user, String pwd) {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ldap:10389/dc=springframework,dc=org");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
        env.put(Context.SECURITY_CREDENTIALS, "secret");
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            ctx.setRequestControls(null);
            NamingEnumeration<SearchResult> search = ctx.search("uid=" + user
                                                                        + ",ou=people",
                                                                "(objectclass=person)",
                                                                getSimpleSearchControls());
            if (search.hasMore()) {
                SearchResult next = search.next();
                Attributes attrs = next.getAttributes();
                log.info("{}", attrs.get("cn"));
                Attribute userPassword = attrs.get("userPassword");
                String pass = new String((byte[]) userPassword.get());
                if (pwd.equals(pass)) {
                    return true;
                }
            }
            search.close();
            ctx.close();
        } catch (Exception e) {
            log.error("Exception - ", e);
        }
        return false;
    }
    
    private SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
        return searchControls;
    }
}
