//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

public class Skeleton implements Monster
{
    private String name;
    private int size;
    public Skeleton(String name, int size) {
        this.name=name;
        this.size=size;
    }
    public int getHowBig() {
        return size;
    }
	public String getName() {
	    return name;
    }
    public boolean isBigger(Monster other) {
        return getHowBig()>other.getHowBig();
    }
    public boolean isSmaller(Monster other) {
        return getHowBig()<other.getHowBig();
    }
    public boolean namesTheSame(Monster other) {
        return getName().equals(other.getName());
    }
    @Override
    public String toString() {
        return "{name="+name+";size="+size+"}";
    }
}