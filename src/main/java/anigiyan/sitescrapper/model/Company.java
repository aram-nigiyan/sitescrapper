package anigiyan.sitescrapper.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COMPANY", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME", name = "COMPANY_NAME_IDX")})
public class Company {
    private Long id;
    private String name;
    private String address;
    private Logo logo;
    private Long remoteId;

    public Company() {
    }

    public Company(String name, String address, Logo logo) {
        this.name = name;
        this.address = address;
        this.logo = logo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique = true)
    public @NotEmpty @Length(max = 1000) String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotEmpty @Length(max = 2000) String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JoinTable(
            name = "COMPANY_LOGO",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "logo_id"),
            indexes = {
                    @Index(name = "COMPANY_LOGO_LOGO_ID_IDX", columnList = "logo_id"),
                    @Index(name = "COMPANY_LOGO_CMP_ID_IDX", columnList = "company_id")
            }
    )
    @ManyToOne(fetch = FetchType.LAZY, optional = false) /*lazy on query company*/ /*can be replaced to OneToOne, preferably removing also join table*/
    @Cascade(CascadeType.ALL)
    public @NotNull Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }
}
