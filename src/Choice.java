class Choice {
    public int attribute;
    public int value;

    public Choice(int attribute, int value) {
        this.attribute = attribute;
        this.value = value;
    }
    /*将数值转化为字符*/
    private char toChar() {
        return (char) (attribute + 65);
    }
    @Override
    public String toString() {
        return toChar() +"="+ this.value;
    }
}