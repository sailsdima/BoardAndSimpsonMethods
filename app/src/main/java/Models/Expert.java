package Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sails on 25.11.2016.
 */

public class Expert {

    int id;
    List<String> resultsOrder;
    int importance;

    public Expert(int id, List<String> resultsOrder, int votersNumber) {
        this.id = id;
        this.resultsOrder = resultsOrder;
        this.importance = votersNumber;
    }

    public Expert(int id, String resultsOrder, int votersNumber) {
        this.id = id;
        this.importance = votersNumber;
        this.resultsOrder = new ArrayList();

        for(int i = 0; i < resultsOrder.split(",").length; i++){
            this.resultsOrder.add(resultsOrder.split(",")[i].trim());
        }
    }

    public int getId() {
        return id;
    }

    public List<String> getResultsOrder() {
        return resultsOrder;
    }

    public String getStringResultsOrder() {
        StringBuilder stringResultsOrder = new StringBuilder();
        for (String s: resultsOrder) {
            stringResultsOrder.append(s + ", ");
        }
        stringResultsOrder.delete(stringResultsOrder.length() - 2, stringResultsOrder.length());
        return stringResultsOrder.toString();
    }

    public int getImportance() {
        return importance;
    }

    @Override
    public String toString() {
        return String.format("ID: %d;\tRatings: %s\tImportance: %d", id, Arrays.toString(resultsOrder.toArray()), importance);
    }
}
