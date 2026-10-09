package com.myproject.snipurl.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actualUrl;

    private String snipUrl;

    private int clickCount = 0;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SnipUser user;

    @OneToMany
    @JoinColumn(name = "url_mapping")
    private List<ClickEvent> clickEvents;

}
