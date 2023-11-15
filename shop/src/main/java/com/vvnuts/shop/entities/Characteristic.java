package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.utils.Views;
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
    @JsonView(Views.Low.class)
    private Integer id;

    @Column(name = "name")
    @Size(max = 40)
    @NotNull
    @JsonView(Views.Low.class)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "characteristics")
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "characteristic")
    @JsonIgnore
    private List<CharacterItem> characterItems;
}
