import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// A classe UserManager gerencia operações relacionadas aos utilizadores, como adição, autenticação e persistência em arquivo.
public class UserManager {
    private static final String USER_FILE = "users.json";
    private List<User> userList;

    public UserManager() {
        this.userList = loadUsers();
    }

    // Método para adicionar um utilizador à lista de utilizadores.
    public void addUser(User user) {
        // Criptografar a senha antes de adicionar ao utilizador
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        userList.add(user);
        saveUsers();
        System.out.println("Utilizador criado com sucesso: " + user.getUsername());
    }

    // Método para obter um usuário com base no nome de utilizadores.
    public User getUserByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Método para obter uma cópia da lista de utilizadores.
    public List<User> getUsers() {
        return new ArrayList<>(userList);
    }

    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);

        // Verificar se o utilizador existe e comparar a senha fornecida com a senha
        // criptografada armazenada
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }

    // Método privado para salvar a lista de utilizadores no arquivo.
    private void saveUsers() {
        try (Writer writer = new FileWriter(USER_FILE)) {
            Gson gson = new Gson();
            gson.toJson(userList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método privado para carregar a lista de utilizadores do arquivo
    private List<User> loadUsers() {
        try (Reader reader = new FileReader(USER_FILE)) {
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {
            }.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
