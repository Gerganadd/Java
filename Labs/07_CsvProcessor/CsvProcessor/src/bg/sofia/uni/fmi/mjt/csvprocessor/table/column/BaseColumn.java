package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseColumn implements Column {
    private Set<String> values;

    public BaseColumn() {
        this(new LinkedHashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }

    @Override
    public void addData(String data) {
        if (data == null || data.isBlank()) {
            throw new IllegalArgumentException("Data can't be null or empty");
        }

        values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return Collections.unmodifiableSet(values);
    }
}
