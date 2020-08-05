package com.asiainfo.iot.bean;


public class Page {

//    总数
    private Integer totalRecord;
//    总页码
    private Integer totalPages;
//    当前页码
    private Integer currNum;
//    每页数量
    private Integer countNum;
//    数据集合
    private Object object;

    private Integer offsetNum;

    public Page() {
    }
    public Page(Integer totalRecord, Integer currNum, Integer countNum, Integer offsetNum, Object object) {
        this.totalRecord = totalRecord;
        this.totalPages =  (totalRecord  +  countNum  - 1) / countNum;
        this.currNum = currNum;
        this.countNum = countNum;
        this.object = object;
        this.offsetNum= offsetNum;
    }
    public Page(Integer totalRecord, Integer currNum, Integer countNum, Object object) {
        this.totalRecord = totalRecord;
        this.totalPages =  (totalRecord  +  countNum  - 1) / countNum;
        this.currNum = currNum;
        this.countNum = countNum;
        this.object = object;
    }

    public Integer getOffsetNum() {
        return offsetNum;
    }

    public void setOffsetNum(Integer offsetNum) {
        this.offsetNum = offsetNum;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrNum() {
        return currNum;
    }

    public void setCurrNum(Integer currNum) {
        this.currNum = currNum;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalRecord=" + totalRecord +
                ", totalPages=" + totalPages +
                ", currNum=" + currNum +
                ", countNum=" + countNum +
                ", object=" + object +
                ", offsetNum=" + offsetNum +
                '}';
    }
}
