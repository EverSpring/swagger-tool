package com.everspring.data;

import java.io.Serializable;
import java.util.List;

/**
 * Description： 存储持久化的映射数据
 * Date： 2020/12/15 9:25
 *
 * @author changchun.xue
 */
public class WordMapStoreModel implements Serializable {
    private List<WordMapDataModel> dataModelList;

    public WordMapStoreModel() {
    }

    public WordMapStoreModel(List<WordMapDataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    public List<WordMapDataModel> getDataModelList() {
        return dataModelList;
    }

    public void setDataModelList(List<WordMapDataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }
}
