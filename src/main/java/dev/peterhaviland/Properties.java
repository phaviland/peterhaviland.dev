package dev.peterhaviland;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blog")
public class Properties {
    
    private Integer pageSize;
    private String allowRegistrations;
    
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAllowRegistrations() {
        return allowRegistrations;
    }

    public void setAllowRegistrations(String allowRegistrations) {
        this.allowRegistrations = allowRegistrations;
    }
    
}
