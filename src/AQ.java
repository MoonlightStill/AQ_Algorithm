import java.util.*;

public class AQ {
    private static final int SCAN_PE = 001;
    private static final int SCAN_NE = 002;
    private static final int END_SOLUTION = 003;
    private static final int END_CONSISTENT = 004;
    private static final int END_OUT = 005;

    private int[][] dataset;
    private int nSOL;
    private int nCONS;
    private int m;

    private ArrayList<int[]> PE;
    private ArrayList<int[]> NE;

    Map<Set<Choice>,Integer> solution;
    Map<Set<Choice>,Integer> consistent;
    Set<Formula> rule;

    public AQ(int[][] dataset, int nSOL, int nCONS, int m) {
        this.dataset = dataset;
        this.nSOL = nSOL;
        this.nCONS = nCONS;
        this.m = m;
    }

    public void aq() {
        divideExamples();
        rule = new HashSet<>();

        int t = 0;

        while (PE.size() != 0) {
            int[] example = PE.get(t);
            solution = new HashMap<>();
            consistent = new HashMap<>();
            Set<Choice> choices = new HashSet<>();
            induce(example,choices);

            Iterator<Set<Choice>> iterator = solution.keySet().iterator();
            Formula formula = new Formula();
            int bestPerformance = 0;
//            遍历solution和consistent，获取表现最好的公式
            while (iterator.hasNext()) {
                Set<Choice> choose = iterator.next();
                int performance = solution.get(choose);
                if (performance > bestPerformance) {
                    formula = new Formula(choose,solution.get(choose));
                    bestPerformance = performance;
                }
            }
            iterator = consistent.keySet().iterator();
            while (iterator.hasNext()) {
                Set<Choice> choose = iterator.next();
                int performance = consistent.get(choose);
                if (performance > bestPerformance) {
                    formula = new Formula(choose,consistent.get(choose));
                    bestPerformance = performance;
                }
            }
            if (formula.choices != null) {
                rule.add(formula);
                PE.remove(example);
                t = 0;
            } else {
                t ++;
                if (t >= PE.size()) {
                    System.out.println("无法求出一致完备的规则，因为有两项属性值相同，但划分到不同的正反例");
                    rule = null;
                    break;
                }
            }

            modifyPE(formula);

        }

    }
    /*删除所有被formula覆盖的例子，减少PE*/
    private void modifyPE(Formula formula) {
        Set<Choice> choices = formula.choices;
        if(choices == null)
            return;
        boolean isMatch;
        Iterator<int[]> iterator = PE.iterator();
        while (iterator.hasNext()) {
            int[] posExample = iterator.next();
            isMatch = true;
            for (Choice choice : choices) {
                if (posExample[choice.attribute] != choice.value) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                iterator.remove();
            }
        }
    }
    /*将例子集划分为PE和NE*/
    public void divideExamples() {
        PE = new ArrayList<>();
        NE = new ArrayList<>();
        for(int[] example: dataset) {
            if (example[example.length - 1] == 1) {
                PE.add(example);
            } else {
                NE.add(example);
            }
        }

    }

    public void induce(int[] example,Set<Choice> choices) {
        ArrayList<Formula> candidates = new ArrayList<>();
        Set<Choice> tmpChoices;
        for(int attribute = 0;attribute < example.length - 1;attribute ++) {
            if (hasChecked(attribute, choices)) {//如果该选择子已经在choices中，则跳过
                continue;
            }
            int value = example[attribute];
            if(value == -1) continue;
            Choice choice = new Choice(attribute, value);
            tmpChoices = new HashSet<>(choices);
            tmpChoices.add(choice);

            int coverPE = scanExamples(tmpChoices,SCAN_PE);
            int coverNE = scanExamples(tmpChoices,SCAN_NE);

            if (coverNE == 0) {
                if (coverPE == PE.size() && solution.size() <= nSOL) {
                    solution.put(tmpChoices,coverPE);
                    if(solution.size() > nSOL) return;
                } else if (consistent.size() <= nCONS) {
                    consistent.put(tmpChoices,coverPE);
                    if(candidates.size() > nCONS) return;
                }
            } else {
                float performance = (float)coverPE / (float) coverNE;
                Formula formula = new Formula(tmpChoices,performance);
                candidates.add(formula);
            }
        }
        if (candidates.size() == 0) {
            return;
        }
        //按照表现情况，由优到劣排序
        candidates.sort(new Comparator<Formula>() {
            @Override
            public int compare(Formula o1, Formula o2) {
                return (int)(o2.perfomance - o1.perfomance);
            }
        });
        //选出最优的m个选择子集合进行下一轮induce递归
        for(int i = 0;i < m-1;i ++) {
            induce(example, candidates.get(i).choices);
        }

    }

    private boolean hasChecked(int attribute, Set<Choice> choices) {
        Iterator<Choice> iterator = choices.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().attribute == attribute)
                return true;
        }
        return false;
    }
    /*判断多个选择子的析取对PE和NE的覆盖情况*/
    private int scanExamples(Set<Choice> choices,int whichSet) {
        int count = 0;
        boolean isMatch;
        ArrayList<int[]> scanSet;
        switch (whichSet) {
            case SCAN_NE:
                scanSet = NE;
                break;
            case SCAN_PE:
                scanSet = PE;
                break;
            default:
                return 0;

        }
        for (int[] posExample : scanSet) {
            isMatch = true;
            for (Choice choice : choices) {
                if (posExample[choice.attribute] != choice.value) {
                    isMatch = false;
                   break;
                }
            }
            if(isMatch) count++;
        }
        return count;
    }

    /*Setter and Getter*/
    public int[][] getDataset() {
        return dataset;
    }

    public void setDataset(int[][] dataset) {
        this.dataset = dataset;
    }

    public int getnSOL() {
        return nSOL;
    }

    public void setnSOL(int nSOL) {
        this.nSOL = nSOL;
    }

    public int getnCONS() {
        return nCONS;
    }

    public void setnCONS(int nCONS) {
        this.nCONS = nCONS;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public Set<Formula> getRule() {
        return rule;
    }

    public ArrayList<int[]> getPE() {
        return PE;
    }

    public ArrayList<int[]> getNE() {
        return NE;
    }
}
