package com.everspring.view;

import com.everspring.service.impl.GenerateAllSetServiceImpl;
import com.everspring.service.impl.GenerateChooseServiceImpl;
import com.everspring.service.impl.GetSetImpl;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Description： 设置set弹窗
 * Date： 2020/12/26 15:35
 *
 * @author changchun.xue
 */
public class SetDialogForm extends DialogWrapper {
    private AnActionEvent e;
    private SetSelectListView setSelectListView;
    public SetDialogForm(AnActionEvent e) {
        super(true);
        this.e = e;
        this.setTitle("请选择");
        this.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        setSelectListView = new SetSelectListView();
        GetSetImpl generateService = new GetSetImpl(setSelectListView);
        generateService.invoke(e);
        return setSelectListView.getMainPanel();
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();
        JButton allBtn = new JButton("生成所有(Enter)");
        GenerateAllSetServiceImpl generateAllSetListener = new GenerateAllSetServiceImpl(e, this);
        allBtn.addActionListener(generateAllSetListener);
        panel.registerKeyboardAction(generateAllSetListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        JButton chooseBtn = new JButton("生成...");
        chooseBtn.addActionListener(new GenerateChooseServiceImpl(e,this, setSelectListView));
        panel.add(allBtn);
        panel.add(chooseBtn);
        return panel;
    }
}
