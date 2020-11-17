package com.everspring.config;


/**
 * 持久化配置文件
 *
 * @author changchun.xue
 * @date 2020/11/17
 */
public class TranslatorConfiguration {

    /**
     * 是否翻译
     */
    private Boolean translatorFlag;

    public Boolean getTranslatorFlag() {
        return translatorFlag;
    }

    public void setTranslatorFlag(Boolean translatorFlag) {
        this.translatorFlag = translatorFlag;
    }
}
