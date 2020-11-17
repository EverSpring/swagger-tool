package com.everspring.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Description： 右键按钮
 * Date： 2020/11/16 10:02
 *
 * @author changchun.xue
 */
public class NoTranslatorTransformAction extends AbstractTransformAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        super.actionPerformed(e,false);
    }

    /**
     * 是否显示右键
     * @param e
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
