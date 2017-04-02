package com.github.aelmod.ssn2.seed;

import com.github.aelmod.ssn2.chat.ConversationService;
import com.github.aelmod.ssn2.city.City;
import com.github.aelmod.ssn2.city.CityService;
import com.github.aelmod.ssn2.country.Country;
import com.github.aelmod.ssn2.country.CountryService;
import com.github.aelmod.ssn2.microblog.Microblog;
import com.github.aelmod.ssn2.microblog.MicroblogService;
import com.github.aelmod.ssn2.user.User;
import com.github.aelmod.ssn2.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FirstSeed {

    private static final int USER_COUNT = 5;

    private static final int COUNTRY_COUNT = 3;

    private static final int CITY_COUNT = 3;

    private static final int MICROBLOG_COUNT = 20;

    private final UserService userService;

    private final CountryService countryService;

    private final CityService cityService;

    private final MicroblogService microblogService;

    private final ConversationService conversationService;

    @Autowired
    public FirstSeed(UserService userService, CountryService countryService, CityService cityService, MicroblogService microblogService, ConversationService conversationService) {
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.microblogService = microblogService;
        this.conversationService = conversationService;
    }

    @PostConstruct
    public void initData() {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < COUNTRY_COUNT; i++) {
            Country country = new Country("country" + i);
            countries.add(country);
            countryService.create(country);
        }

        List<City> cities = new ArrayList<>();
        for (int i = 0; i < CITY_COUNT; i++) {
            City city = new City("city" + i);
            cities.add(city);
            cityService.create(city);
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < USER_COUNT; i++) {
            User user = new User();
            user.setName("user" + i);
            user.setUsername("user" + i);
            user.setPassword("pass1");
            user.setCountry(countries.get((int) (Math.random() * COUNTRY_COUNT)));
            user.setCity(cities.get((int) (Math.random() * CITY_COUNT)));
//            user.getMicroblogs().add(microblogs.get((int) (Math.random() * MICROBLOG_COUNT)));
            users.add(user);
            userService.save(user);
        }

//        List<Microblog> microblogs = new ArrayList<>();
        for (int i = 0; i < MICROBLOG_COUNT; i++) {
            Microblog microblog = new Microblog();
            microblog.setText("Hi" + i);
//            microblogs.add(microblog);
            microblog.setUser(users.get((int) (Math.random() * USER_COUNT)));
            microblogService.create(microblog);
        }

        for (int i = 0; i < USER_COUNT; i++) {
            User user = users.get(i);
            User friend = users.get((int) (Math.random() * USER_COUNT));
            if (Objects.equals(user.getId(), friend.getId())) continue;
            userService.makeFriends(user, friend);
        }

        User user1 = new User();
        user1.setId(3);
        userService.ignore(user1, 2);


        User user2 = new User();
        user2.setId(2);
        userService.requestFriendship(user2, 3);

//        conversationService.startConversations(user2, user1.getId());
    }
}
