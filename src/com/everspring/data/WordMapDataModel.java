package com.everspring.data;

/**
 * Description： 映射数据model
 * Date： 2020/11/21 22:30
 *
 * @author changchun.xue
 */
public class WordMapDataModel {
    private String en;
    private String ch;

    public WordMapDataModel(String en, String ch) {
        this.en = en;
        this.ch = ch;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String[] convert() {
        String[] raw = new String[2];
        raw[0] = getEn();
        raw[1] = getCh();
        return raw;
    }
}
