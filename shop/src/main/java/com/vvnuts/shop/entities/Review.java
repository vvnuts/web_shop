package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.utils.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Low.class)
    private Integer id;

    @Column(name = "mark")
    @JsonView(Views.Low.class)
    private Integer mark;

    @Column(name = "review_text", length = 1000)
    @JsonView(Views.Low.class)
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
//    @JsonView(Views.Low.class)
    @JsonIgnore
    private User user;
}
