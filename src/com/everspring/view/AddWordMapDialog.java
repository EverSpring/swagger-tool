package com.everspring.view;

import com.everspring.data.DataList;
import com.everspring.data.WordMapDataModel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Description： 添加映射关系的弹窗
 * Date： 2020/11/21 21:53
 *
 * @author changchun.xue
 */
public class AddWordMapDialog extends DialogWrapper {

    private EditorTextField fieldEn;
    private EditorTextField fieldCn;
    private String en = "";
    private String cn = "";
    private Integer updateRow;

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
        fieldEn = new EditorTextField("");
        fieldEn.setToolTipText("英文");
        fieldCn = new EditorTextField("");
        fieldCn.setToolTipText("中文");
        Dimension dimension = new Dimension(200, 100);
        fieldEn.setPreferredSize(dimension);
        fieldCn.setPreferredSize(dimension);
        panel.add(fieldEn, BorderLayout.NORTH);
        panel.add(fieldCn, BorderLayout.SOUTH);
        panel.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        return panel;
    }

    /**
     * 按钮区域
     * @return
     */
    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();
        JButton btn = new JButton("添加映射(Enter)");
        btn.addActionListener(event -> {
            save();
        });
        panel.add(btn);
        return panel;
    }

    private void save() {
        String enText = fieldEn.getText();
        String cnText = fieldCn.getText();
        if (StringUtils.isBlank(fieldCn.getText()) || StringUtils.isBlank(fieldEn.getText())) {
            Messages.showErrorDialog("内容不能为空", "提醒");
            return;
        }
        if (updateRow == null) {
            WordMapDataModel addModel = new WordMapDataModel(enText, cnText);
            DataList.dataList.add(addModel);
            DataList.tableModel.addRow(addModel.convert());
            fieldEn.setText("");
            fieldCn.setText("");
        } else {
            WordMapDataModel updateModel = DataList.dataList.get(updateRow);
            updateModel.setCh(cnText);
            updateModel.setEn(enText);
            DataList.tableModel.setValueAt(enText,updateRow,0);
            DataList.tableModel.setValueAt(cnText,updateRow,1);
            this.close(this.getExitCode());
        }
        DataList.wordMap.put(enText, cnText);
        DataList.isChange = true;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
        fieldEn.setText(en);
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
        fieldCn.setText(cn);
    }

    public Integer getUpdateRow() {
        return updateRow;
    }

    public void setUpdateRow(Integer updateRow) {
        this.updateRow = updateRow;
    }
}
