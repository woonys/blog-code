package domain.club;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.user.Gender;
import domain.user.User;

class ClubServiceTest {

    private Club club = new Club();

    private ClubService clubService = new ClubService(club);

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new User();
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 17, 18})
    public void cannotEnterClub_ifUserIsNotAdult(int age) {
        //given
        user.setGender(Gender.MALE);
        user.setAge(age);
        user.setGrade(Grade.SILVER);

        //when
        boolean result = clubService.isEnterClub(user);

        //then
        assertEquals(false, result);
    }

}