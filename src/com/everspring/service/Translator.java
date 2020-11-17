package com.everspring.service;


/**
 * 翻译
 *
 * @author changchun.xue
 * @date 2020/11/17
 */
public interface Translator {

    /**
     * 英译中
     *
     * @param text 文本
     * @return {@link String}
     */
    String en2Ch(String text);

    /**
     * 中译英
     *
     * @param text 文本
     * @return {@link String}
     */
    String ch2En(String text);

    /**
     * 清除缓存
     */
    void clearCache();

}
