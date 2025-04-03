package edu.log.models.user;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_value", nullable = false, unique = true)
    private String tokenValue;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    
    public Token() {}

    public Token(User user, String tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue;
    }

    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
        
    public String getTokenValue() { return tokenValue; }
    public void setTokenValue(String tokenValue) { this.tokenValue = tokenValue; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", tokenValue='" + tokenValue + '\'' +
                ", user=" + user +
                '}';
    }
}
