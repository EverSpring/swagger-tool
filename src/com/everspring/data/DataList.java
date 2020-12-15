package com.everspring.data;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Description： 数据公用
 * Date： 2020/11/21 22:40
 *
 * @author changchun.xue
 */
public class DataList {
    public static List<WordMapDataModel> dataList = new ArrayList<>();

    private static final String[] tableHeader = {"英文","中文"};

    public static DefaultTableModel tableModel = new DefaultTableModel(null, tableHeader);

    public static boolean isChange;
}
