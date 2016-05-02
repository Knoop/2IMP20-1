package gui;

import regex.RegexBuilder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 02-05-16.
 */
public class RegexBuilderForm {
    private RegexBuilder regexBuilder;
    private RegexTableModel tableModel;

    private JTable tblRegex;
    private JPanel pnlContent;

    private JFrame container;

    public RegexBuilderForm() {
        this.regexBuilder = new RegexBuilder();
        this.tableModel = new RegexTableModel(this.regexBuilder);
        this.tblRegex.setModel(this.tableModel);

        this.container = new JFrame("Regex Tester");
        this.container.setContentPane(pnlContent);
        this.container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.container.pack();
        this.container.setSize(500, 500);
        this.container.setVisible(true);
    }
}
