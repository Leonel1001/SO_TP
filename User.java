import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;

    // Construtor da classe User que inicializa o nome de utilizador e senha.
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Método para obter o nome de utilizador.
    public String getUsername() {
        return username;
    }

    // Método para obter a senha do utilizador.
    public String getPassword() {
        return password;
    }

    // Método para definir a senha do usuário.
    public void setPassword(String password) {
        this.password = password;
    }
}