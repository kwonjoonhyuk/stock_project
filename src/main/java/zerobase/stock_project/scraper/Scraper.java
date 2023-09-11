package zerobase.stock_project.scraper;

import zerobase.stock_project.model.Company;
import zerobase.stock_project.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
