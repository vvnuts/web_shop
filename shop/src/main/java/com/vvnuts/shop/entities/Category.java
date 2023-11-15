package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.utils.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "category_name")
        })
public class Category implements Comparable<Category>{
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Low.class, Views.CategoryCatalog.class})
    private int categoryId;

    @Column(name = "category_name")
    @Size(max = 40)
    @NotNull
    @JsonView({Views.Low.class, Views.CategoryCatalog.class})
    private String categoryName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "parent_child",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    @JsonView(Views.CategoryCatalog.class)
    private List<Category> children;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "children")
    private List<Category> parents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    private List<Characteristic> characteristics;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Item> items;

    @Override
    public int compareTo(Category o) {
        return this.getCategoryId() - o.getCategoryId();
    }
}
