package net.malpiszon.fundallocator.models;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InvestmentType {
    SAFE(constructFundsTypePercentage(20, 75, 5)),
    BALANCED(constructFundsTypePercentage(30, 60, 10)),
    AGGRESSIVE(constructFundsTypePercentage(40, 20, 40));

    Map<FundType, Integer> fundTypePercentage;

    InvestmentType(Map<FundType, Integer> fundTypePercentage) {
        this.fundTypePercentage = fundTypePercentage;
    }

    public Map<FundType, Integer> getFundsTypePercentages() {
        return this.fundTypePercentage;
    }

    public static InvestmentType getInvestmentType(String type) {
        return InvestmentType.valueOf(type);
    }

    private static Map<FundType, Integer> constructFundsTypePercentage(Integer polish, Integer foreign,
                                                                       Integer financial) {
        return Collections.unmodifiableMap(Stream.of(
                entry(FundType.POLISH, polish),
                entry(FundType.FOREIGN, foreign),
                entry(FundType.FINANCIAL, financial))
                .collect(entriesToMap()));
    }

    private static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entriesToMap() {
        return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
    }
}
