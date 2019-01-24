package DynamicProxyJDBC.simple;

import DynamicProxyJDBC.simple.domain.User;
import DynamicProxyJDBC.simple.interfaces.JdbcHandler;
import DynamicProxyJDBC.simple.proxy.JdbcConnectionAspect;

import java.lang.reflect.Proxy;

public class SimpleApplication {

	public static void main(String[] args) {

		JdbcHandler jdbcHandler = (JdbcHandler) Proxy.newProxyInstance(JdbcHandler.class.getClassLoader(),
				new Class[] { JdbcHandler.class },
				new JdbcConnectionAspect());

        User user = new User();

        user.setUserName("dEn");
        user.setSex("male");
        user.setAge(23);

        jdbcHandler.saveUser(user);

    }

}

