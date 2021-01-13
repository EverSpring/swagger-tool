package com.everspring.action;

import com.everspring.view.SetDialogForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        FileType fileType = psiFile.getViewProvider().getVirtualFile().getFileType();
        if (!"java".equals(fileType.getDefaultExtension())) {
            e.getPresentation().setEnabledAndVisible(false);
        } else {
            e.getPresentation().setEnabledAndVisible(true);
        }
    }
}
