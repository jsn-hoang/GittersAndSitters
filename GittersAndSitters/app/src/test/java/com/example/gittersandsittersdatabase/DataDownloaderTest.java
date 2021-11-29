package com.example.gittersandsittersdatabase;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DataDownloaderTest {

    DataDownloader testDataDownloader;
    User testUser;
    ArrayList<Habit> testHabitList;


    /** This method generates a test User
     * Test user is an actual user in the database
     * @return - the testUser
     */
        private User testUser() {
            User user = new User("nCdjCK67qKVBDGxiVbYsieoueWL2", "askiba", "aarontheguy3000@hotmail.com");
            return user;
        }

    /**
     * This method generates a DataDownloader object for testing
     * @param userID - string corresponding to a user
     * @return - a DataDownloader for downloading from Firestore
     */
    private DataDownloader testDataDownloader(String userID) {
        testDataDownloader = new DataDownloader(userID);
        return testDataDownloader;
    }


    /**
     * This method simply attempts to generate a new DataDownloader object
     */
    @Test
    public void createDataDownloaderObjectTest() {
        testUser = testUser();
        testDataDownloader = testDataDownloader(testUser().getUserID());
    }



    //TODO
    /**
     * This test attempts to fetch Habits for an existing User in the Firestore database
     * ***This specific testUser has Habits in the database
     */
//    @Test
//    public void downloadUserHabitsTest() {
//        testUser = testUser();
//        testDataDownloader = testDataDownloader(testUser().getUserID());
//        testDataDownloader.getUserHabits(new FirestoreHabitListCallback() {
//            @Override
//            public void onHabitListCallback(ArrayList<Habit> habitList) {
//                testHabitList = habitList;
//                // This test user should have at least one Habit stored in firestore
//                if (habitList.isEmpty())
//                    throw new IllegalArgumentException("Error: habitList should not be empty.");
//            }
//        });
//    }

}
