package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "bucket_items")
public class BucketItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Low.class)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "item_id")
    @JsonView(Views.Low.class)
    private Item item;

    @Column(name = "quantity")
    @JsonView(Views.Low.class)
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bucket_id")
    @JsonIgnore
    private Bucket bucket;
}
