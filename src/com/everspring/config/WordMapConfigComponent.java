package com.everspring.config;

import com.everspring.data.DataList;
import com.everspring.data.WordMapDataModel;
import com.everspring.data.WordMapStoreModel;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 映射配置组件
 *
 * @author changchun.xue
 * @date 2020/11/21
 */
@State(name = "wordMap", storages = {@Storage("$APP_CONFIG$/wordMap.xml")})
public class WordMapConfigComponent implements PersistentStateComponent<WordMapStoreModel> {

    public static WordMapConfigComponent getInstance() {
        return ServiceManager.getService(WordMapConfigComponent.class);
    }


    /**
     * IDEA关闭时，将内存中配置持久化到xml中
     * @return
     */
    @Nullable
    @Override
    public WordMapStoreModel getState() {
        List<WordMapDataModel> storeList = DataList.dataList.stream().map(d -> new WordMapDataModel(d.getEn(), d.getCh())).collect(Collectors.toList());
        return new WordMapStoreModel(storeList);
    }

    /**
     * IDEA打开时，从xml中读取配置
     * @param wordMapDataModel
     */
    @Override
    public void loadState(@NotNull WordMapStoreModel wordMapDataModel) {
//        XmlSerializerUtil.copyBean(wordMapDataModel, Objects.requireNonNull(getState()));
        List<WordMapDataModel> dataModelList = wordMapDataModel.getDataModelList();
        if (dataModelList != null && dataModelList.size()>0) {
            dataModelList.forEach(model -> {
                DataList.dataList.add(new WordMapDataModel(model.getEn(), model.getCh()));
                DataList.tableModel.addRow(model.convert());
            });
        }
    }

    @Override
    public void noStateLoaded() {

    }

    @Override
    public void initializeComponent() {

    }
}
