package com.vvnuts.shop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quality")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
