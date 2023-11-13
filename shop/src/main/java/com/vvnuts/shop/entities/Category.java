package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int categoryId;

    @Column(name = "category_name")
    @Size(max = 40)
    @NotNull
    private String categoryName;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    @JoinTable(
            name = "parent_child",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Category> children;

    @ManyToMany(mappedBy = "children")
    @JsonIgnore
    private List<Category> parents;

    @ManyToMany(cascade = CascadeType.MERGE)
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
