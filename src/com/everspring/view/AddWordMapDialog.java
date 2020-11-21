package com.everspring.view;

import com.everspring.data.DataList;
import com.everspring.data.WordMapDataModel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Description： 添加映射关系的弹窗
 * Date： 2020/11/21 21:53
 *
 * @author changchun.xue
 */
public class AddWordMapDialog extends DialogWrapper {

    private EditorTextField fieldEn;
    private EditorTextField fieldCn;

    public AddWordMapDialog() {
        super(true);
        this.setTitle("添加自定义映射");
        this.init();
    }

    /**
     * 输入区域
     * @return
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        fieldEn = new EditorTextField("英文");
        fieldCn = new EditorTextField("中文");
        Dimension dimension = new Dimension(200, 100);
        fieldEn.setPreferredSize(dimension);
        fieldCn.setPreferredSize(dimension);
        panel.add(fieldEn, BorderLayout.NORTH);
        panel.add(fieldCn, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * 按钮区域
     * @return
     */
    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();
        JButton btn = new JButton("添加映射");
        btn.addActionListener(event -> {
            String enText = fieldEn.getText();
            String cnText = fieldCn.getText();
            WordMapDataModel model = new WordMapDataModel(enText, cnText);
            DataList.dataList.add(model);
            DataList.tableModel.addRow(model.convert());
        });
        panel.add(btn);
        return panel;
    }
}
