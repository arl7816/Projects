/**
 * Holds the ActionFigure class which in itself is an extension of the Doll toy.
 * @author Alex Lee
 * */

package toys;

public class ActionFigure extends Doll{
    /** The minimum energy */
    public final static int MIN_ENERGY_LEVEL = 1;
    /** The hair color of every Action figure */
    public final static Color HAIR_COLOR = Color.ORANGE;

    /** the starting product code of Action figure (increases by one, upon instillation) */
    private static int baseProductCode = 300;

    /** the energy level of an Action Figure */
    private int energyLevel;

    /**
     * The constructor of the Action Figure
     * @param name: the name of the action figure
     * @param age: the age of the action figure
     * @param speak: what the action figure says
     * */
    protected ActionFigure(String name, int age, String speak){
        super(baseProductCode,name, HAIR_COLOR, age, speak);
        baseProductCode++;
        this.energyLevel = MIN_ENERGY_LEVEL;
    }

    /**
     * Gets the energy level of an action figure
     * @return the energy level
     * */
    public int getEnergyLevel(){
        return this.energyLevel;
    }

    /**
     * Plays with the action figure
     * Causes the energy level to increase by 1 than calls the special play in Doll (parent class)
     * @param time: the time played with
     * */
    @Override
    protected void specialPlay(int time) {
        System.out.println("\t" + this.getName() + " kung foo chops with " + this.getEnergyLevel() * time + " energy!");
        this.energyLevel += 1;
        super.specialPlay(time);
    }

    /**
     * Gets a string representation of the action figure in the form of...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, Doll{HC:?, A:?, S:?}, ActionFigure{E:?}
     * E -> energy level
     * @return string representation of the action figure
     * */
    @Override
    public String toString() {
        return super.toString() + ", ActionFigure{E:" + this.getEnergyLevel() + "}";
    }
}
