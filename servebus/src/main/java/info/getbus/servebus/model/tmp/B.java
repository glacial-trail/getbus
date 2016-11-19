package info.getbus.servebus.model.tmp;


import info.getbus.servebus.model.AbstractEntity;

public class B extends AbstractEntity<Long> {
    private String a;
    private String b;


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }


    @Override
    public String toString() {
        return "B{" +
                "id=" + getId() +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
