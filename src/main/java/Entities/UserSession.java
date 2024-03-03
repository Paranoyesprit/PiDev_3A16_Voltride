package Entities;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    // Constructeur privé pour empêcher l'instanciation directe depuis l'extérieur
    public UserSession() {
    }

    // Méthode pour obtenir l'instance unique de la classe UserSession
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Méthode pour définir l'utilisateur courant
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Méthode pour obtenir l'utilisateur courant
    public User getCurrentUser() {
        return currentUser;
    }
}

