package cpe211.fourthexam.reyes;

import static kvx.jcandyexamedition.Std.*;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// KV Quick Command Line Interface API
public class QCLI {
    
    private UserNetwork userNetwork = new UserNetwork();
    private UserNetwork.KNetwork kn = userNetwork.new KNetwork();
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    
    public void run() {

        loadExamSample();
        
        for (;;) {

            loadMenu();
            print("Enter your choice: ");
            String choice = readInput().trim();
            
            switch (choice) {
                case "1":
                    register();
                    break;
                case "2":
                    add();
                    break;
                case "3":
                    recommendations();
                    break;
                case "4":
                    viewAllUsers(false);
                    break;
                case "5":
                    clear();
                    println("App closed sucessfully.");
                    println("My Github: https://github.com/karlwizkrafte");
                    newl();
                    return;
                default:
                    println("Invalid choice.");
                    pause();
            }
        }
    }

    public void loadMenu() {

        clear();
        println("KVX OCULUS SOCIAL ALGORITHM");
        newl();
        println("1. Register New User");
        println("2. Add Friendship");
        println("3. Get Friend Recommendations");
        println("4. View All Users");
        println("5. Exit");
        newl();
    }
    
    private void loadExamSample() {

        // Users 1,2,3,4,5,6
        // Friendships: [(1,2), (1,3), (2,4), (3,5), (5,6)]

        kn.registerUser("Max Payne", "1");
        kn.registerUser("Lara Croft", "2");
        kn.registerUser("Max Verstappen", "3");
        kn.registerUser("Zuck Markerberg", "4");
        kn.registerUser("Arthur Morgan", "5");
        kn.registerUser("Christiano Ronaldo", "6");
        
        kn.addConnection("1", "2");
        kn.addConnection("1", "3");
        kn.addConnection("2", "4");
        kn.addConnection("3", "5");
        kn.addConnection("5", "6");
    }
    
    private void register() {

        clear();
        println("REGISTER");
        newl();
        print("Enter username: ");
        String username = readInput().trim();
        print("Enter user ID: ");
        String uid = readInput().trim();
        
        if (username.isEmpty() || uid.isEmpty()) {
            println("Username and ID cannot be empty.");
        } else if (kn.checkDuplicateUID(uid)) {
            println("Error: User ID '%s' already exists.", uid);
        } else {
            kn.registerUser(username, uid);
            printf("User %s [UID: %s] registered successfully.", username, uid);
            newl();
        }
        pause();
    }
    
    private void add() {

        clear();
        

        println("ADD FRIENDSHIP");
        newl();

        viewAllUsers(true);
        newl();

        print("Enter first user ID: ");
        String uid1 = readInput().trim();
        print("Enter second user ID: ");
        String uid2 = readInput().trim();
        
        if (!uid1.isEmpty() && !uid2.isEmpty()) {
            kn.addConnection(uid1, uid2);
            println("Friendship between %s and %s is added.", uid1, uid2);
        } else {
            println("User IDs cannot be empty.");
        }
        pause();
    }
    
    private void recommendations() {

        clear();
        println("RECOMMENDATIONS");
        newl();
        print("Enter user ID: ");
        String uid = readInput().trim();
        
        if (!uid.isEmpty()) {
            Set<UserNetwork.User> recommendations = kn.getRecommendations(uid);
            
            if (recommendations.isEmpty()) {
                println("No friend recommendations found for user %s", uid);
            } else {
                println("Friend recommendations for user %s:", uid);
                newl();
                for (UserNetwork.User user : recommendations) {
                    println("- %s (ID: %s)", user.getUsername(), user.getUID());
                }
            }
        } else {
            println("User ID cannot be empty.");
        }
        pause();
    }
    
    private void viewAllUsers(boolean inline) {

        if (!inline) {
            clear();
            println("ALL USERS");
            newl();
        } else {
            println("Available Users:");
            newl();
        }
        
        if (kn.userDB.isEmpty()) {
            println("No users registered yet.");
        } else {
            int count = 1;
            for (UserNetwork.User user : kn.userDB.values()) {
                printf("(%d) [UID: %s] %s - %d friends", 
                       count, 
                       user.getUID(), 
                       user.getUsername(),
                       user.getConnections().size());
                
                newl();
                count++;
            }
        }

        if (!inline) { pause(); }
    }
    
    // Util
    private void pause() {

        newl();
        print("Press ENTER to continue...");
        readInput();
    }
    
    private String readInput() {

        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            println("Error reading input: " + e.getMessage());
            return "";
        }
    }
}