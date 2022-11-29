package cc.truecredits.truecreditlms.domain.acsreport.practice;

import lombok.Builder;

@Builder
public class User {

    private Long id;
    private String userLoginId;
    private String password;
    private String address;

    public static User of(Long id, String userLoginId) {
        return new User.builder().id(id).userLoginId(userLoginId).build();
    }

    public User(String password, String address) {
        this.password = password;
        this.address = address;
    }

    public User(Long id, String password, String address) {
        this.id = id;
        this.password = password;
        this.address = address;
    }

    public User() {

    }
}
