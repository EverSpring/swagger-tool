package com.everspring.view;

import com.everspring.data.DataList;
import com.everspring.data.WordMapDataModel;
import com.intellij.openapi.ui.MessageDialogBuilder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description： 映射配置显示
 * Date： 2020/11/21 23:40
 *
 * @author changchun.xue
 */
public class ListWordMapForm {
    private JPanel mainPanel;
    private JPanel btnPanel;
    private JScrollPane panelContent;
    private JTable tbContent;
    private JButton btnAdd;
    private JButton btnDel;
    private JButton resetBtn;

    public ListWordMapForm() {
        tbContent.setModel(DataList.tableModel);
        tbContent.setEnabled(true);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddWordMapDialog addWordMapDialog = new AddWordMapDialog();
                addWordMapDialog.show();
            }
        });
        btnDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tbContent.getSelectedRow();
                WordMapDataModel dataModel = DataList.dataList.get(selectedRow);
                MessageDialogBuilder.YesNo warnDialog = MessageDialogBuilder.yesNo("删除警告", "你确定删除：" + dataModel.getEn() + "->" + dataModel.getCh());
                if (warnDialog.isYes()) {
                    DataList.dataList.remove(selectedRow);
                    DataList.tableModel.removeRow(selectedRow);
                }
            }
        });
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageDialogBuilder.YesNo warnDialog = MessageDialogBuilder.yesNo("清除警告", "真的全部清除？");
                if (warnDialog.isYes()) {
                    DataList.dataList.clear();
                    DataList.tableModel.setRowCount(0);
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
