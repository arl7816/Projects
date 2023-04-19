/**
 * Holds the class Doll which extends the abstract class Toy
 * Holds all the attributes and methods a doll should have such as but not limited to hair color, age, and ability to play
 * @author Alex Lee
 * */

package toys;

public class Doll extends Toy{
    /** The starting product number (goes up by 1 upon a Dolls initialization) */
    private static int baseProductCode = 200;
    /** the hair color the doll EX. RED, BLUE, ORANGE */
    private Color hairColor;
    /** the age of the doll */
    private int age;
    /** whatever the doll says */
    private String speak;

    /**
     * Class constructor (product code handled, via static variable baseProductCode)
     * @param name: name of the Doll
     * @param hairColor: the color of the dolls hair
     * @param age: the age of the Doll
     * @param speak: what the doll says
     * */
    protected Doll(String name, Color hairColor, int age, String speak){
        super(baseProductCode, name);
        baseProductCode++;
        this.hairColor = hairColor;
        this.age = age;
        this.speak = speak;
    }

    /**
     * Class constructor when given a product code (Used mostly by the Action Figure subclass)
     * @param productCode: the product code of the Doll
     * @param name: name of the Doll
     * @param hairColor: the color of the dolls hair
     * @param age: the age of the Doll
     * @param speak: what the doll says
     * */
    protected Doll(int productCode, String name, Color hairColor, int age, String speak){
        super(productCode, name);
        this.hairColor = hairColor;
        this.age = age;
        this.speak = speak;
    }

    /**
     * Gets the hair color of the Doll Ex. BLUE, RED, ORANGE
     * @return the hair color of the doll
     * */
    public Color getHairColor(){
        return this.hairColor;
    }

    /**
     * Gets the age of the doll
     * @return age
     * */
    public int getAge(){
        return this.age;
    }

    /**
     * Gets what the doll say's when it speaks
     * @return what the doll says
     * */
    public String getSpeak(){
        return this.speak;
    }

    /**
     * The special play function of Doll
     * Generates the message: {name} brushes their {hair color} hair and says, "{speak}", followed by the wear increasing
     * by the age of the Doll
     * @param time: the time the Doll is played with
     * */
    @Override
    protected void specialPlay(int time){
        System.out.println("\t" + this.getName() + " brushes their " + this.getHairColor() + " hair and says, \"" + this.getSpeak() + "\"");
        this.increaseWear(this.getAge());
    }

    /**
     * Gets a string representation of the Doll in the form of...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, Doll{HC:?, A:?, S:?}
     * HC -> Hair color
     * A -> age
     * S -> speak
     * @return representation of a Doll
     * */
    @Override
    public String toString() {
        return super.toString() + ", Doll{HC:" + this.getHairColor() + ", A:" + this.getAge() + ", S:" + this.getSpeak() + "}";
    }
}
