package DynamicProxyJDBC.simple.proxy;

import DynamicProxyJDBC.simple.domain.User;
import DynamicProxyJDBC.simple.interf.UserEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Properties;

public class JdbcConnectionAspect implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try {

            System.out.println("Loading Class org.postgresql.Driver");

            Class.forName("org.postgresql.Driver");

            System.out.println("Loading org.postgresql.Driver Successful");

            String url = "jdbc:postgresql://localhost:5432/useproxydb";

            Properties props = new Properties();

            props.setProperty("user", "sasha_vosu");

            props.setProperty("password", "1151855");

            props.setProperty("ssl", "false");

            try (Connection conn = DriverManager.getConnection(url, props)) {

                System.out.println("Test Connection Successful");

                final Class<?> aClass = args[0].getClass();

                final String name = method.getName();

                if (annotationDetection(aClass)) {

                    if ("saveUser".equals(name)) {

                        final Object so = args[0];



//                        "insert into tbl (so.getClass().getFields()[0].get) values so.getClass().getFields()[0].get(so)"
//                        so.getClass().getFields()[0].get(so)

                        saveUser(so, conn);

                    } else if ("getByUserName".equals(name)) {

                        return getByUserName(args[0].toString(), conn);

                    }
                }
            }

        } catch (SQLException ex) {

            System.out.println("SQLException: " + ex.getMessage());

            System.out.println("SQLState: " + ex.getSQLState());

            System.out.println("VendorError: " + ex.getErrorCode());

        } catch (ClassNotFoundException ex) {

            System.out.println("Class Not Found: " + ex.getMessage());

        }
        return null;
    }

    private static void saveUser(Object o, Connection conn) throws IllegalAccessException {

        String userName = "";

        String sex = "";

        int age = 0;

        final Class<?> aClass = o.getClass();

        final Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {

            final String fieldName = field.getName();

            final Class<?> type = field.getType();

            if (type == String.class) {

                field.setAccessible(true);

                final String s = (String)field.get(o);

                if ("userName".equals(fieldName)) {

                  userName = userName.concat(s);

                } else if ("sex".equals(fieldName)) {

                    sex = sex.concat(s);

                }

            } else if (type == int.class || type == Integer.class) {

                field.setAccessible(true);

                age = age + field.getInt(o);

            }
        }

        try {

            PreparedStatement st = conn.prepareStatement("INSERT INTO usr (user_name, sex, age) VALUES (?, ?, ?)");

            st.setString(1, userName);
            st.setString(2, sex);
            st.setInt(3, age);
            st.executeUpdate();
            st.close();

            System.out.println("User saved successfully");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    private static User getByUserName(String userName, Connection conn) {

        User user = null;

        try {

            PreparedStatement st = conn.prepareStatement("SELECT * FROM usr WHERE usr.user_name = ?");
            st.setString(1, userName);

            if (st.execute()) {

                ResultSet rs = st.getResultSet();

                rs.next();

                final String user_name = rs.getString("user_name");

                final long id = rs.getLong("id");

                final String sex = rs.getString("sex");

                final int age = rs.getInt("age");

                user = new User(id, user_name, sex, age);
            }

            st.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return user;
    }

    private static boolean annotationDetection(Class<?> c) {

        return c.isAnnotationPresent(UserEntity.class);
    }
}

