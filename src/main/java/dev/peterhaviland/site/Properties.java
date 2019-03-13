package dev.peterhaviland.site;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blog")
public class Properties {

    private String connectionString;
    private Integer pageSize;
    private String allowRegistrations;
    
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    
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
