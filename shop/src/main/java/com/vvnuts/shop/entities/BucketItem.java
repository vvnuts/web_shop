package com.vvnuts.shop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bucket_items")
public class BucketItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "bucketItems")
    private Bucket bucket;
}
