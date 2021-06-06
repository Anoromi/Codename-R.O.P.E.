package Properties;

import Objects.GameObject;

/**
 * Defines a property for interaction of main (caller) objects with their surroundings.
 * File: Interaction.java
 * @author Andrii Zahorulko
 */
public abstract class Interaction extends Property {

  public abstract void interact(GameObject object);
}
