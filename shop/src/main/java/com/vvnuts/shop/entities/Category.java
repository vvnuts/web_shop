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
@Table(name = "Categories")
public class Category implements Comparable<Category>{
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToMany
    @JoinTable(
            name = "child_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Category> children;

    @ManyToMany(mappedBy = "children")
    @JsonIgnore
    private List<Category> parents;

    @ManyToMany
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    @JsonIgnore
    private List<Characteristic> characteristics;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Item> items;

    @Override
    public int compareTo(Category o) {
        return this.getCategoryId() - o.getCategoryId();
    }
}
