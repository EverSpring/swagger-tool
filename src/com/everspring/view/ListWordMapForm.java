package com.everspring.view;

import com.everspring.data.DataList;

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
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
