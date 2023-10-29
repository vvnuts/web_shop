package com.vvnuts.shop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class CharacterItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    private String value;

    @Column(name = "num_value")
    private Integer numValue;

    @ManyToOne
    @JoinColumn(name = "characteristic_id")
    private Characteristic characteristic;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

}
