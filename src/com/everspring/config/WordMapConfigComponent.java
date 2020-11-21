package com.everspring.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * 映射配置组件
 *
 * @author changchun.xue
 * @date 2020/11/21
 */
@State(name = "wordMap", storages = {@Storage("wordMap.xml")})
public class WordMapConfigComponent implements PersistentStateComponent<TranslatorPersistentConfig> {

    private TranslatorPersistentConfig configuration;

    @Nullable
    @Override
    public TranslatorPersistentConfig getState() {
        if (configuration == null) {
            configuration = new TranslatorPersistentConfig();
        }
        return configuration;
    }

    @Override
    public void loadState(@NotNull TranslatorPersistentConfig translatorPersistentConfig) {
        XmlSerializerUtil.copyBean(translatorPersistentConfig, Objects.requireNonNull(getState()));
    }

    @Override
    public void noStateLoaded() {

    }

    @Override
    public void initializeComponent() {

    }
}
