package zerobase.stock_project.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stock_project.model.Company;
import zerobase.stock_project.model.ScrapedResult;
import zerobase.stock_project.persist.CompanyRepository;
import zerobase.stock_project.persist.DividendRepository;
import zerobase.stock_project.persist.entity.CompanyEntity;
import zerobase.stock_project.persist.entity.DividendEntity;
import zerobase.stock_project.scraper.Scraper;

import java.util.List;

import static zerobase.stock_project.model.constants.CacheKey.KEY_FINANCE;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;


    //일정 주기마다 수행
    @CacheEvict(value = KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {

        log.info("scraping scheduler is started");
        // 1. 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 2. 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scraping scheduler is started 회사명 : " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                    Company.builder()
                            .name(company.getName())
                            .ticker(company.getTicker())
                            .build());

            // 3. 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    //디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    //엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) {
                            this.dividendRepository.save(e);
                            log.info("insert new Dividend -> "+e.toString());
                        }
                    });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("인터럽트 발생으로 쓰레드 종료");
                Thread.currentThread().interrupt();
            }
        }


    }
}
