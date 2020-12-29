package com.everspring.action;

import com.everspring.view.SetDialogForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Description： 生成set的实现
 * Date： 2020/12/22 14:38
 *
 * @author changchun.xue
 */
public class SetAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        SetDialogForm setDialogForm = new SetDialogForm(e);
        setDialogForm.show();
    }
}
