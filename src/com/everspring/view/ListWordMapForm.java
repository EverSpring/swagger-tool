package com.everspring.view;

import com.everspring.data.DataList;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                int[] selectedRows = tbContent.getSelectedRows();
                if (selectedRows.length == 0) {
                    Messages.showErrorDialog("请先选择要删除的行", "提醒");
                    return;
                }
                for (int i = 0; i < selectedRows.length; i++) {
                    DataList.dataList.remove(i);
                    DataList.tableModel.removeRow(i);
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
        tbContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    int row =((JTable)e.getSource()).rowAtPoint(e.getPoint());
                    String en = (String)tbContent.getModel().getValueAt(row, 0);
                    String cn = (String)tbContent.getModel().getValueAt(row, 1);
                    AddWordMapDialog addWordMapDialog = new AddWordMapDialog();
                    addWordMapDialog.setEn(en);
                    addWordMapDialog.setCn(cn);
                    addWordMapDialog.setUpdateRow(row);
                    addWordMapDialog.show();
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTable getTbContent() {
        return tbContent;
    }

    public void setTbContent(JTable tbContent) {
        this.tbContent = tbContent;
    }
}
