
package com.rollcall.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

//bgg csv fields - name,game_id,type,rating,weight,year_published,min_players,max_players,min_play_time,max_play_time,min_age,owned_by,categories,mechanics,designers,artists,publishers

@Entity
@Table(name = "categories")
public class Categories {
@Id
Long id;

    CREATE TABLE category (
            category_id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255) UNIQUE NOT NULL
);


    Join Table
    CREATE TABLE game_category (
            game_id INT,
            category_id INT,
            PRIMARY KEY (game_id, category_id),
    FOREIGN KEY (game_id) REFERENCES game(game_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
            );
}
