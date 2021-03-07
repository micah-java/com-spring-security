package com.spring.security.entity;

public class Permission {

    private Integer id;
    private Integer customerId;
    private String enname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Permission{");
        sb.append("id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", enname='").append(enname).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
