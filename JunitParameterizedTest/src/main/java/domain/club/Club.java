package domain.club;

import java.util.ArrayList;
import java.util.List;

import domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Club {
    private List<User> lounge = new ArrayList<>();
    private List<User> room = new ArrayList<>();

    public void enterClub(User user) {
            lounge.add(user);
            System.out.println("클럽에 입장하셨습니다.");
    }

    public void enterRoom(User user) {
        room.add(user);
        System.out.println("룸에 입장하셨습니다.");
    }

    StringBuilder builder = new StringBuilder();
}
