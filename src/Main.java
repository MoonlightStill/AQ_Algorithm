import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //在这里输入生成例子集的参数
        DatasetsUtil datasets = new DatasetsUtil(10000, 26, 10);
        int[][] set = datasets.getDataset();

        if(set == null) return;
        datasets.standardOutput();
        //生成AQ算法实例
        AQ aq = new AQ(set, 10, 4, 2);
        aq.aq();
        Set<Formula> rule = aq.getRule();

        Iterator<Formula> iterator = rule.iterator();
        while (iterator.hasNext()) {
            System.out.print("(");
            System.out.print(iterator.next());
            System.out.print(")");
            if (iterator.hasNext()) {
                System.out.print(" ∨ ");
            }
        }
        System.out.println();
        long endTime = System.currentTimeMillis();
        System.out.println("运行时间为"+(endTime-startTime)/1000.0+"s");

        for (int[] example : set) {
            if (!jugge(example, rule)){
                System.out.println("错了！");
                for (int i : example) {
                    System.out.print(i+" ");
                }
                break;
            }
        }
    }

    /*判断规则是否是一致且完备*/
    private static boolean jugge(int[] example, Set<Formula> rule) {
        Iterator<Formula> iterator = rule.iterator();
        boolean isMatch = true;
        while (iterator.hasNext()) {
            Formula formula = iterator.next();
            isMatch = true;
            for (Choice choice : formula.choices) {
                if (example[choice.attribute] != choice.value) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                break;
            }
        }

        if (example[example.length - 1] == 1) {
            return isMatch;
        } else {
            return !isMatch;
        }
    }
}
