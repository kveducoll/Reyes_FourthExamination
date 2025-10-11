package cpe211.fourthexam.reyes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

// KV UserNetwork API
public class UserNetwork {
    
    class User {
        private String username;
        private String UID;
        private Set<User> connections = new HashSet<>();

        public User(String username, String UID) {
            this.username = username;
            this.UID = UID;
        }

        public String getUsername()         { return username; }
        public String getUID()              { return UID; }
        public Set<User> getConnections()   { return connections; }
    }

    class KNetwork {

        public Map<String, User> userDB = new HashMap<>();
        
        public void registerUser(String name, String UID) {
            userDB.putIfAbsent(name, new User(name, UID));
        }
        
        public boolean checkDuplicateUID(String UID) {
            for (User user : userDB.values()) {
                if (user.getUID().equals(UID)) {
                    return true;
                }
            }
            return false;
        }

        public void addConnection(String senderUID, String recieverUID) { // (Stuff) REV 3 - possible nga issue (!)
            
            // (RVFIX) Init error prone variables
            User sender = null;
            User receiver = null;

            for (User user : userDB.values()) {

                if (user.getUID().equals(senderUID)) {
                    sender = user;
                }
                if (user.getUID().equals(recieverUID)) {
                    receiver = user;
                }
            }
            if (sender != null && receiver != null && sender != receiver) {

                sender.getConnections().add(receiver);
                receiver.getConnections().add(sender);

            }

        }

        public Set<User> getRecommendations(String userID) {

            Set<User> suggestedUser = new HashSet<>();

            User targetUser = null;
            
            for (User user : userDB.values()) {

                if (user.getUID().equals(userID)) {

                    targetUser = user;
                    break;
                }
            }
            
            if (targetUser == null) return suggestedUser;

            for (User connections : targetUser.getConnections()) {

                for (User currentUser : connections.getConnections()) {

                    if (currentUser == targetUser) continue; 
                    if (targetUser.getConnections().contains(currentUser)) continue;

                    suggestedUser.add(currentUser);
                
                }
            }
            
            return suggestedUser;
        }
    }
}