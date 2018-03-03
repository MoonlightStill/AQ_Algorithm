import java.util.Random;

public class DatasetsUtil {
    private int nExamples;
    private int nAttributes;
    private int nAttributeValue;
    private int[][] dataset;

    public DatasetsUtil(int n_examples, int n_Attributes, int nAttributeValue) {
        if (n_examples > 2*(Math.pow(nAttributeValue,n_Attributes))) {
            System.out.println("需求例子数目过多，无法生成");
            return;
        }
        nExamples = n_examples;
        nAttributes = n_Attributes;
        this.nAttributeValue = nAttributeValue;
        generateDatasets();
    }

    private boolean generateDatasets() {
        if (nExamples == 0 || nAttributes == 0 || nAttributeValue == 0) {
            return false;
        }
        Random random = new Random();
        dataset = new int[nExamples][nAttributes+1];
        int i = 0;
        while (i < nExamples) {
            for(int j = 0;j < nAttributes;j ++) {

                dataset[i][j] = random.nextInt(nAttributeValue);
            }
            dataset[i][nAttributes] = Math.random() > 0.4 ? 1 : 0;
            if (i != 0 && repeated(i)) {

//                for(int k = 0;k < nAttributes;k++) {
//                    System.out.print(dataset[i][k]+" ");
//                }
//                System.out.println("重复了"+(i));

            } else {
                i++;
            }
//            System.out.println(repeated(dataset[i]));
//            i++;
        }

        return true;
    }

    private boolean repeated(int index) {
        for(int i = 0;i < dataset.length;i++) {
            if (i == index) {
                continue;
            }
            for(int j = 0;j < nAttributes;j++) {
                if (dataset[i][j] != dataset[index][j]) {
                    break;
                } else if (j == nAttributes - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getnExamples() {
        return nExamples;
    }

    public void setnExamples(int nExamples) {
        this.nExamples = nExamples;
    }

    public int getnAttributes() {
        return nAttributes;
    }

    public void setnAttributes(int nAttributes) {
        this.nAttributes = nAttributes;
    }

    public int getnAttributeValue() {
        return nAttributeValue;
    }

    public void setnAttributeValue(int nAttributeValue) {
        this.nAttributeValue = nAttributeValue;
    }

    public int[][] getDataset() {
        return dataset;
    }

    public void standardOutput() {
        System.out.println("-------------------------------------------------------------------");
        System.out.print("序号\t ");
        for(int i = 0;i < nAttributes;i++) {
            System.out.print((char)(i+65)+ "\t ");
        }
        System.out.println("类型");
        for(int i = 0;i < nExamples;i ++) {
            System.out.print((i+1)+"\t ");
            for(int j = 0;j < nAttributes;j ++) {
                System.out.print(dataset[i][j]+ "\t ");
            }
            if (dataset[i][nAttributes] == 1) {
                System.out.println("+");
            } else {
                System.out.println("-");
            }
        }
        System.out.println("-------------------------------------------------------------------");
    }
}
