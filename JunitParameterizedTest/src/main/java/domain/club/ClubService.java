package domain.club;

import domain.user.User;

public class ClubService {
    private Club club;

    public ClubService(Club club) {
        this.club = club;
    }

    public boolean isEnterClub(User user) {
        if (!user.isAdult()) {
            System.out.println("미성년자는 못 들어갑니다.");
            return false;
        }

        if (user.isGenderMale() && Grade.isUnderBronze(user.getGrade())) {
            System.out.println("자리가 만석입니다. 암튼 만석임");
            return false;
        }



        club.enterClub(user);
        return true;
    }

    public boolean isEnterRoom(User user) {

        if (user.isVipMale() || user.isAboveGoldFemale()) {
            club.enterRoom(user);
            return true;
        }
        return false;
    }
}
