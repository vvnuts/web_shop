package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "sale")
    private Float sale;

    @Column(name = "mark")
    private Float mark;

    @OneToMany(mappedBy = "item")
    private List<CharacterItem> characterItems;

    @OneToMany(mappedBy = "item")
    private List<Review> reviews;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<BucketItem> bucketItems;
}
