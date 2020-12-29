package com.everspring.service.impl;

import com.everspring.view.SetDialogForm;
import com.everspring.view.SetSelectListView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Description： 生成选中的set
 * Date： 2020/12/28 16:27
 *
 * @author changchun.xue
 */
public class GenerateChooseServiceImpl extends AbstractSetService implements ActionListener {
    private SetDialogForm form;
    private AnActionEvent e;
    private SetSelectListView setSelectListView;

    public GenerateChooseServiceImpl(AnActionEvent e, SetDialogForm form, SetSelectListView setSelectListView) {
        this.e = e;
        this.form = form;
        this.setSelectListView = setSelectListView;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.invoke(e);
        List<String> valuesList = setSelectListView.getNameList().getSelectedValuesList();
        if (valuesList != null && valuesList.size() > 0) {
            form.close(form.getExitCode());
        }
    }

    @Override
    public List<String> getGenerateSetName(PsiClass psiClass) {
        List<String> valuesList = setSelectListView.getNameList().getSelectedValuesList();
        if (valuesList == null || valuesList.size() == 0) {
            Messages.showErrorDialog("请选择要生成的内容", "提醒");
            return null;
        }
        return valuesList;
    }
}
