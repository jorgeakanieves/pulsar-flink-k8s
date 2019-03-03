package com.jene.cognitive.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @uthor Jorge Nieves
 */

public class AccountResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountId;
    private List<String> cities = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    private int count;
    private long minTimestamp;
    private long maxTimestamp;
    private String minTimestampAsString;
    private String maxTimestampAsString;
    private HashMap<String,Integer> cityCounts = new HashMap<String,Integer>();
    private HashMap<String,Double> cityAmounts = new HashMap<String,Double>();
    private HashMap<String,Integer> countryCounts = new HashMap<String,Integer>();
    private String result;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getMinTimestamp() {
        return minTimestamp;
    }

    public void setMinTimestamp(long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public String getMinTimestampAsString() {
        return minTimestampAsString;
    }

    public void setMinTimestampAsString(String minTimestampAsString) {
        this.minTimestampAsString = minTimestampAsString;
    }

    public String getMaxTimestampAsString() {
        return maxTimestampAsString;
    }

    public void setMaxTimestampAsString(String maxTimestampAsString) {
        this.maxTimestampAsString = maxTimestampAsString;
    }

    public HashMap<String, Integer> getCityCounts() {
        return cityCounts;
    }

    public void setCityCounts(HashMap<String, Integer> cityCounts) {
        this.cityCounts = cityCounts;
    }

    public HashMap<String, Double> getCityAmounts() {
        return cityAmounts;
    }

    public void setCityAmounts(HashMap<String, Double> cityAmounts) {
        this.cityAmounts = cityAmounts;
    }

    public HashMap<String, Integer> getCountryCounts() {
        return countryCounts;
    }

    public void setCountryCounts(HashMap<String, Integer> countryCounts) {
        this.countryCounts = countryCounts;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getResult() {
        return this.toString();
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AccountResult{" +
                "accountId='" + accountId + '\'' +
                ", cities=" + cities +
                ", countries=" + countries +
                ", count=" + count +
                ", minTimestamp=" + minTimestamp +
                ", maxTimestamp=" + maxTimestamp +
                ", minTimestampAsString='" + minTimestampAsString + '\'' +
                ", maxTimestampAsString='" + maxTimestampAsString + '\'' +
                ", cityCounts=" + cityCounts +
                ", cityAmounts=" + cityAmounts +
                ", countryCounts=" + countryCounts +
                '}';
    }
}
