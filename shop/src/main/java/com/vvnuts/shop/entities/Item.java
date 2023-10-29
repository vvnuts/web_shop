package com.vvnuts.shop.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;

    @Column(name = "sale")
    private float sale;

    @OneToMany(mappedBy = "item")
    private List<CharacterItem> characterItems;

    @OneToMany(mappedBy = "item")
    private List<Review> reviews;

    @OneToMany(mappedBy = "item")
    private List<Bucket> buckets;
}
