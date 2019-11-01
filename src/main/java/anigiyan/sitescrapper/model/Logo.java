package anigiyan.sitescrapper.model;

import javax.persistence.*;

/**
 * Developer: nigiyan
 * Date: 30/10/2019
 */

@Entity
@Table(name = "LOGO")
public class Logo {

    public Logo() {
    }

    public Logo(byte[] data) {
        this.data = data;
    }

    private long id;
    private byte[] data;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Lob
    @Column(columnDefinition = "BLOB")
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
