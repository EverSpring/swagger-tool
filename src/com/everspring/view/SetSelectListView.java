package com.everspring.view;

import javax.swing.*;

/**
 * Description： 生成set的界面
 * Date： 2020/12/26 14:06
 *
 * @author changchun.xue
 */
public class SetSelectListView {
    private JPanel mainPanel;
    private JScrollPane listScroll;
    private JList nameList;

    public SetSelectListView() {

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JList getNameList() {
        return nameList;
    }
}
