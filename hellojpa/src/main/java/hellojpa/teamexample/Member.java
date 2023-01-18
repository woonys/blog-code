package hellojpa.teamexample;

import javax.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

//    @ManyToOne // Member 입장에서 N(Many)이고 Team 입장에서 1(One)이니!
//    @JoinColumn(name = "TEAM_ID")
//    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID") // JoinColumn은 반드시 있어야 됨!
    private Locker locker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
}
