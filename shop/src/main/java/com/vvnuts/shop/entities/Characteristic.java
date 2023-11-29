package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vvnuts.shop.entities.enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "characteristics")
public class Characteristic {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @Size(max = 40)
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)  //TODO сделать типы характеристик
    @Column(name = "type")
    private Type type;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "characteristics")
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "characteristic", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CharacterItem> characterItems;
}
