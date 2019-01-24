package DynamicProxyJDBC.simple.domain;

import DynamicProxyJDBC.simple.interf.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@UserEntity
@Entity
@Getter
@Setter
@Table(name = "usr")
public class User {

    public User() {
    }

    public User(long id, String userName, String sex, int age) {
        this.id = id;
        this.userName = userName;
        this.sex = sex;
        this.age = age;
    }

    @Column(name = "user_name")
    @NotNull
    private String userName;

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age")
    private int age;

}

