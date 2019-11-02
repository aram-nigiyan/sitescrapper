package anigiyan.sitescrapper.processor;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

class CompanySearchPageData {

    private String name;
    private String imageUrl;

    public CompanySearchPageData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
