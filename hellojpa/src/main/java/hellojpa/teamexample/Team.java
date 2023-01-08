package hellojpa.teamexample;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @OneToMany(mappedBy = "team") // 일대다 매핑에서 무엇과 연결되어 있지? -> 내 반대편 사이드(Member)에는 team(Team team)이 걸려있다는 걸 표시해주기 위해!
    // 객체는 가급적 단방향이 좋다!
    private List<Member> memberList = new ArrayList<>(); // 생성할 때 ArrayList로 초기화하는 게 관례 -> 그래야 add할 때 NPE가 뜨지 않는다!
}
