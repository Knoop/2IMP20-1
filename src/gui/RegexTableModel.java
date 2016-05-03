package gui;

import regex.RegexBuilder;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;


public class RegexTableModel implements TableModel
{
    private RegexBuilder regexBuilder;

    private Set<TableModelListener> listeners;

    public RegexTableModel(RegexBuilder regexBuilder) {
        this.regexBuilder = regexBuilder;
        this.listeners = new HashSet<>();
    }

    private List<String> getIdentifiersAsList() {
        List<String> keyList = new ArrayList<>(regexBuilder.getRegexes().keySet());
        Collections.sort(keyList);
        return keyList;
    }

    private String getIdentifier(int index) {
        List<String> identifiers = getIdentifiersAsList();
        return identifiers.get(index);
    }

    @Override
    public int getRowCount() {
        return this.regexBuilder.getRegexes().keySet().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Identifier";
        }
        else if (columnIndex == 1){
            return "Regex";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String identifier = getIdentifier(rowIndex);
        if (columnIndex == 0) {
            return identifier;
        }
        else if (columnIndex ==1) {
            return this.regexBuilder.combineByIdentifier(identifier);
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) throws IllegalArgumentException {
        String identifier = getIdentifier(rowIndex);
        if (columnIndex == 0) {
            if (!(aValue instanceof String)) {
                throw new IllegalArgumentException("Identifier must be a string");
            }
            List<String> value = this.regexBuilder.getRegexes().get(identifier);
            String newIdentifier = (String)aValue;
            this.regexBuilder.getRegexes().remove(identifier);
            this.regexBuilder.getRegexes().put(newIdentifier, value);
        }
        else if (columnIndex == 1) {
            if (!(aValue instanceof Collection<?>)) {
                throw new IllegalArgumentException("Value must be a list");
            }
            Collection<?> values = (Collection<?>) aValue;
            List<String> newValue = new ArrayList<>();
            for (Object o : values) {
                if (!(o instanceof String)) {
                    throw new IllegalArgumentException("Value must be a list of only strings");
                }
                newValue.add((String) aValue);
            }
            this.regexBuilder.getRegexes().put(identifier,  newValue);
        }

        this.listeners.forEach(TableModelListener::notify);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }
}
