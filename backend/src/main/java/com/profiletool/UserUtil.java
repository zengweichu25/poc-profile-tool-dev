package com.profiletool;

public class UserUtil {
    /**
     * Gets the length of a user's name.
     * 
     * @param user The user object (can be null).
     */
    public int getUserNameLength(User user) {
        // The AI should flag the next line
        return user.getName().length();
    }

    // --- Supporting class ---
    class User {
        private String name;

        public String getName() {
            return this.name;
        }
    }
}
