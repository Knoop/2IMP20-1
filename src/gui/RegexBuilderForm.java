package gui;

import regex.RegexBuilder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Guus on 02-05-16.
 */
public class RegexBuilderForm {
    private RegexBuilder regexBuilder;
    private RegexTableModel tableModel;

    private JTable tblRegex;
    private JPanel pnlContent;
    private JTextArea txtTestInput;
    private JLabel lblRegexTable;
    private JLabel lblInput;
    private JLabel lblMatches;
    private JTextArea txtMatches;

    private JFrame container;

    private String selectedRegex;

    public RegexBuilderForm(RegexBuilder regexBuilder) {
        this.regexBuilder = regexBuilder;
        this.tableModel = new RegexTableModel(this.regexBuilder);
        this.tblRegex.setModel(this.tableModel);

        this.container = new JFrame("Regex Tester");
        this.container.setContentPane(pnlContent);
        this.container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.container.pack();
        this.container.setSize(500, 500);
        this.selectedRegex = "";
        this.initEvents();
        this.container.setVisible(true);
    }

    private void initEvents() {
        this.txtTestInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                txtTestInput_update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                txtTestInput_update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                txtTestInput_update();
            }
        });

        this.tblRegex.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedRegex = (String)tblRegex.getValueAt(tblRegex.getSelectedRow(), 1);
                txtTestInput_update();
            }
        });
    }

    private void txtTestInput_update() {
        Pattern pattern = Pattern.compile(this.selectedRegex);
        String text = this.txtTestInput.getText();
        Matcher matcher = pattern.matcher(text);
        txtMatches.setText("");
        if (matcher.find()) {
            txtMatches.append(matcher.group());
            txtMatches.append("\n\n");
        }
        while(matcher.find()) {
            txtMatches.append(matcher.group());
            txtMatches.append("\n\n");
        }
    }
}
