package com.rollcall.web.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(columnDefinition = "TEXT")
    private String aboutMe;
    @Column(columnDefinition = "TEXT")
    private String interests;
    private String photoURL;
    private boolean darkMode;
    private int zip;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserEntity user;
}
