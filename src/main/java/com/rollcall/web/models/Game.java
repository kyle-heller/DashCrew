
package com.rollcall.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

//bgg csv fields - name,game_id,type,rating,weight,year_published,min_players,max_players,min_play_time,max_play_time,min_age,owned_by,categories,mechanics,designers,artists,publishers

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long bggId;
    private String title;
    private int yearPublished;
    private String type;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private double averageRating;
    private String photoURL;
    private String bggLink;
    @Column(columnDefinition = "TEXT")
    private String description;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @ManyToMany
    @JoinTable(
            name = "game_category",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @Override
    public String toString() {
        // Adjusted to not directly call toString on categories
        return "Game{" +
                "id=" + id +
                ", bggId=" + bggId +
                ", title='" + title + '\'' +
                ", ... other fields here ..." +
                ", categories=[...]" + // Indicate presence of categories without iterating
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return yearPublished == game.yearPublished &&
                minPlayers == game.minPlayers &&
                maxPlayers == game.maxPlayers &&
                playingTime == game.playingTime &&
                Double.compare(averageRating, game.averageRating) == 0 &&
                Objects.equals(id, game.id) &&
                Objects.equals(bggId, game.bggId) &&
                Objects.equals(title, game.title) &&
                Objects.equals(type, game.type) &&
                Objects.equals(photoURL, game.photoURL) &&
                Objects.equals(bggLink, game.bggLink) &&
                Objects.equals(description, game.description) &&
                Objects.equals(createdOn, game.createdOn) &&
                Objects.equals(updatedOn, game.updatedOn) &&
                Objects.equals(createdBy, game.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bggId, title, yearPublished, type, minPlayers, maxPlayers, playingTime, averageRating, photoURL, bggLink, description, createdOn, updatedOn, createdBy);
    }
}
