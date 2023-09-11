package zerobase.stock_project.persist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.stock_project.model.Company;

import javax.persistence.*;

@Entity(name = "COMPANY")
@Getter
@ToString
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker; //증권 시세 표시기

    private String name;

    public CompanyEntity(Company company){
        this.ticker = company.getTicker();
        this.name = company.getName();
    }
}
