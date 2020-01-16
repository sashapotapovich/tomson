package example.tomson;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CustomListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		try {
			Context context = (Context)new InitialContext();
			context.rebind("java:jboss/exported/test", "value");
			System.out.println("lookup test: " + (String)context.lookup("java:jboss/exported/test"));;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Value saved in context.");
	}
	

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Value deleted from context.");
	}
}