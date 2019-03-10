package dev.peterhaviland.site;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blog")
public class Properties {

    private String connectionString;
    private Integer pageSize;
    
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
    
}
