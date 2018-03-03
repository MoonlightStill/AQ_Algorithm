import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class Formula {
    public Set<Choice> choices;
    public float perfomance;

    public Formula(Set<Choice> choices,float perfomance) {
        this.choices = choices;
        this.perfomance = perfomance;
    }

    public Formula(Set<Choice> choices) {
        this.choices = choices;
    }

    public Formula() {

    }


    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder("");
        if(choices == null)
            return expression.toString();
        ArrayList<Choice> list = new ArrayList<>(choices);
        list.sort(new Comparator<Choice>() {
            @Override
            public int compare(Choice o1, Choice o2) {
                return o1.attribute - o2.attribute;
            }
        });
        Iterator<Choice> iterator = list.iterator();
        while (iterator.hasNext()) {
            Choice choice = iterator.next();
            expression.append(choice.toString());
            if (iterator.hasNext()) {
                expression.append("∧");
            }
        }
        return expression.toString();
    }
}
