package com.vvnuts.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
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
    private int categoryId;

    @Column(name = "category_name")
    @Size(max = 40)
    @NotNull
    private String categoryName;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "parent_child",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Category> children;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "children")
    @JsonIgnore
    private List<Category> parents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    @JsonIgnore
    private List<Characteristic> characteristics;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items;

    @Override
    public int compareTo(Category o) {
        return this.getCategoryId() - o.getCategoryId();
    }
}
