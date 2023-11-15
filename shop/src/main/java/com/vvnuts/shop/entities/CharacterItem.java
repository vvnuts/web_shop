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
@Table(name = "character_item")
public class CharacterItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    @JsonView(Views.Low.class)
    private String value;

    @Column(name = "num_value")
    @JsonView(Views.Low.class)
    private Integer numValue;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "characteristic_id")
    @JsonView(Views.Low.class)
    private Characteristic characteristic;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

}
