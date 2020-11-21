package com.everspring.view;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Description： 映射列表配置菜单
 * Date： 2020/11/21 22:06
 *
 * @author changchun.xue
 */
public class WordMapConfigItem implements SearchableConfigurable {

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "swagger-tool";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel();
        ListWordMapForm form = new ListWordMapForm();
        panel.add(form.getMainPanel());
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @NotNull
    @Override
    public String getId() {
        return "view.wordMapConfig";
    }
}
