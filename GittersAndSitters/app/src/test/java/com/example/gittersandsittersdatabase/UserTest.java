package com.example.gittersandsittersdatabase;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;

public class UserTest {
    private User mockUser() {
        User user = new User("testUserId", "testUser", "testEmail");
        return user;
    }

    @Test
    void testUserId() {
        User user = mockUser();
        assertEquals("testUserId", user.getUserID());
        user.setUserID("1337gamer");
        assertEquals("1337gamer", user.getUserID());
    }

    @Test
    void testUsername() {
        User user = mockUser();
        assertEquals("testUser", user.getUsername());
        user.setUsername("joe");
        assertEquals("joe", user.getUsername());
    }
    @Test
    void testEmail() {
        User user = mockUser();
        assertEquals("testEmail", user.getEmail());
        user.setEmail("joebob@gmail.com");
        assertEquals("joebob@gmail.com", user.getEmail());
    }
    @Test
    public void testAddFollowing() {
        User user = mockUser();
        user.addFollowing("joe");
        ArrayList<String> following = new ArrayList<>();
        following.add("joe");
        assertEquals(following, user.getFollowing());
    }
    @Test
    public void testRemoveFollowing() {
        User user = mockUser();
        user.addFollowing("joe");
        user.removeFollowing("joe");
        ArrayList<String> following = new ArrayList<>();
        assertEquals(following, user.getFollowing());
    }
    @Test
    public void testRemoveFollowingException() {
        User user = mockUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.removeFollowing("joe");
        });
    }
    @Test
    public void testAddRequests() {
        User user = mockUser();
        user.addRequests("joe");
        ArrayList<String> requests = new ArrayList<>();
        requests.add("joe");
        assertEquals(requests, user.getRequests());
    }
    @Test
    public void testRemoveRequests() {
        User user = mockUser();
        user.addRequests("joe");
        user.removeRequests("joe");
        ArrayList<String> requests = new ArrayList<>();
        assertEquals(requests, user.getRequests());
    }
    @Test
    public void testRemoveRequestsException() {
        User user = mockUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.removeFollowing("joe");
        });
    }
    @Test
    public void testAddUserHabit() {
        User user = mockUser();
        Habit habit = new Habit("habitName", new ArrayList<>(), Calendar.getInstance(), "habitReason", true);
        user.addUserHabit(habit);
        assertEquals(habit, user.getUserHabit(0));
    }
    @Test
    public void testDeleteUserHabit() {
        User user = mockUser();
        Habit habit = new Habit("habitName", new ArrayList<>(), Calendar.getInstance(), "habitReason", true);
        user.addUserHabit(habit);
        user.deleteUserHabit(habit);
        ArrayList<Habit> habitList = new ArrayList<>();
        assertEquals(habitList, user.getAllUserHabits());
    }
    @Test
    public void testSetUserHabit() {
        User user = mockUser();
        Habit habit = new Habit("habitName", new ArrayList<>(), Calendar.getInstance(), "habitReason", true);
        user.addUserHabit(habit);
        user.setUserHabit(0, habit);
        assertEquals(habit, user.getUserHabit(0));
    }
    @Test
    public void testGetTodayUserHabits() {
        User user = mockUser();
        ArrayList<Integer> weekdays = new ArrayList<>();
        weekdays.add(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        Habit habit = new Habit("habitName", weekdays, Calendar.getInstance(), "habitReason", true);
        user.addUserHabit(habit);
        ArrayList<Habit> habitList = new ArrayList<>();
        habitList.add(habit);
        assertEquals(habitList, user.getTodayUserHabits());
    }
    @Test
    public void testGetPublicHabits() {
        User user = mockUser();
        Habit habit = new Habit("habitName", new ArrayList<>(), Calendar.getInstance(), "habitReason", true);
        Habit habit2 = new Habit("habitName", new ArrayList<>(), Calendar.getInstance(), "habitReason", false);
        user.addUserHabit(habit);
        user.addUserHabit(habit2);
        ArrayList<Habit> habitList = new ArrayList<>();
        habitList.add(habit);
        assertEquals(habitList, user.getPublicHabits());
    }




}
