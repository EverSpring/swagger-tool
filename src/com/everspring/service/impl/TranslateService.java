package com.everspring.service.impl;

import com.everspring.config.Consts;
import com.everspring.service.Translator;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.components.ServiceManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description： 翻译
 * Date： 2020/11/17 10:22
 *
 * @author changchun.xue
 */
public class TranslateService {

    private Map<String, Translator> translatorMap = ImmutableMap.<String, Translator>builder()
            .put(Consts.YOUDAO_TRANSLATOR, new YoudaoTranslator())
            .build();

    /**
     * 英译中。翻译开关打开翻译
     *
     * @param source 源
     * @return {@link String}
     */
    public String translate(String source) {
        List<String> words = split(source);
        return getFromOthers(StringUtils.join(words, StringUtils.SPACE));
    }

    private List<String> split(String word) {
        word = word.replaceAll("(?<=[^A-Z])[A-Z][^A-Z]", "_$0");
        word = word.replaceAll("[A-Z]{2,}", "_$0");
        word = word.replaceAll("_+", "_");
        return Arrays.stream(word.split("_")).map(String::toLowerCase).collect(Collectors.toList());
    }

    private String getFromOthers(String word) {
        Translator translator = translatorMap.get(Consts.YOUDAO_TRANSLATOR);
        if (Objects.isNull(translator)) {
            return StringUtils.EMPTY;
        }
        return translator.en2Ch(word);
    }
}
