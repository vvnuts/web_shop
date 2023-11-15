package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.utils.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonView(Views.Low.class)
    private User user;

    @Column(name = "total_price")
    @JsonView(Views.Low.class)
    private BigDecimal totalPrice;

    @Column(name = "total_quantity")
    @JsonView(Views.Low.class)
    private Integer totalQuantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bucket")
    private List<BucketItem> bucketItems;
}
