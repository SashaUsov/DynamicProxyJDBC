package DynamicProxyJDBC.simple.interfaces;

import DynamicProxyJDBC.simple.domain.User;

public interface JdbcHandler {

    void saveUser(User entity);

    User getByUserName(String userName);
}
