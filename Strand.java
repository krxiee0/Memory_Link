package com.memorylink.model;

import jakarta.persistence.*;

@Entity
@Table(name = "strands")
public class Strand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 3000)
    private String description;

    @Column(length = 100)
    private String icon = "fa-book-open";

    @Column(nullable = false, length = 20)
    private String status = "active";

    public Strand() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
