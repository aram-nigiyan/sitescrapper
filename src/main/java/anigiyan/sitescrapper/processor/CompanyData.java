package anigiyan.sitescrapper.processor;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

import org.apache.commons.lang3.StringUtils;

/**
 * Intermediate class to collect company data before persistence phase
 *
 * @see anigiyan.sitescrapper.model.Company
 */
public class CompanyData {

    private String name;
    private String imageUrl;
    private byte[] image;
    private Long remoteId;
    private String address;

    public CompanyData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public CompanyData(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.contains("nologo_Small");
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean hasAddress() {
        return !StringUtils.isEmpty(address);
    }
}
