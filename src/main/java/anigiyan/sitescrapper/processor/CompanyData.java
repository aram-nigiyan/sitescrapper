package anigiyan.sitescrapper.processor;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

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

    public CompanyData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
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
}
