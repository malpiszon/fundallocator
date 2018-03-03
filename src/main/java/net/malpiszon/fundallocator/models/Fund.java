package net.malpiszon.fundallocator.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FundType fundType;

    @NotNull
    private String name;

    protected Fund() {
        // The explicit empty constructor needed.
    }

    public Fund(Long id, FundType fundType, String name) {
        this.id = id;
        this.fundType = fundType;
        this.name = name;
    }

    public FundType getFundType() {
        return fundType;
    }

    public String getName() {
        return name;
    }
}
