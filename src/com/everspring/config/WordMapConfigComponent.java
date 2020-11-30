package com.everspring.config;

import com.everspring.data.WordMapDataModel;
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
public class WordMapConfigComponent implements PersistentStateComponent<WordMapDataModel> {

    private WordMapDataModel configuration;

    @Nullable
    @Override
    public WordMapDataModel getState() {
        if (configuration == null) {
            configuration = new WordMapDataModel();
        }
        return configuration;
    }

    @Override
    public void loadState(@NotNull WordMapDataModel wordMapDataModel) {
        XmlSerializerUtil.copyBean(wordMapDataModel, Objects.requireNonNull(getState()));
    }

    @Override
    public void noStateLoaded() {

    }

    @Override
    public void initializeComponent() {

    }
}
