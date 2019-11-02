package anigiyan.sitescrapper.processor;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

public class CompanySearhPageDataPersistRunner implements Runnable {

    CompanySearchPageData companySearchPageData;

    public CompanySearhPageDataPersistRunner(CompanySearchPageData companySearchPageData) {
        this.companySearchPageData = companySearchPageData;
    }

    @Override
    public void run() {
        // save/update company name,logo
    }
}
