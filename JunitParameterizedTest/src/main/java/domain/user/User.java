package domain.user;

import domain.club.Grade;
import lombok.Data;

@Data
public class User {

    private static int AGE_OF_MAJORITY = 18;

    private Long id;

    private String name;

    private int age;

    private Gender gender;

    private Grade grade;

    public boolean isGenderMale() {
        return gender == Gender.MALE;
    }

    public boolean isAdult() {
        return age > AGE_OF_MAJORITY;
    }

    public boolean isVipMale() {
        return gender == Gender.MALE && grade == Grade.VIP;
    }

    public boolean isAboveGoldFemale() {
        if (gender == Gender.FEMALE && Grade.isAboveGold(grade)) {
            return true;
        }
        return false;
    }



}
