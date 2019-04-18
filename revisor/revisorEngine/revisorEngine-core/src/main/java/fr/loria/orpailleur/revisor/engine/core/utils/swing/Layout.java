package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * @author William Philbert.
 */
public class Layout {
	
	// Constants :
	
	public static final int CENTER = GridBagConstraints.CENTER;
	public static final int NORTH = GridBagConstraints.NORTH;
	public static final int SOUTH = GridBagConstraints.SOUTH;
	public static final int EAST = GridBagConstraints.EAST;
	public static final int WEST = GridBagConstraints.WEST;
	public static final int NORTHEAST = GridBagConstraints.NORTHEAST;
	public static final int NORTHWEST = GridBagConstraints.NORTHWEST;
	public static final int SOUTHEAST = GridBagConstraints.SOUTHEAST;
	public static final int SOUTHWEST = GridBagConstraints.SOUTHWEST;
	
	public static final int NONE = GridBagConstraints.NONE;
	public static final int HORIZONTAL = GridBagConstraints.HORIZONTAL;
	public static final int VERTICAL = GridBagConstraints.VERTICAL;
	public static final int BOTH = GridBagConstraints.BOTH;
	
	public static final int RELATIVE = GridBagConstraints.RELATIVE;
	
	// Metods :
	
	/**
	 * Adds the given component into the given container using the given GridBagConstraints.
	 * @param container - the container. (!= component)
	 * @param component - the component. (!= container)
	 * @param xPos - the horizontal position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param yPos - the vertical position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param xWeight - the priority of this component when distributing extra horizontal space. (double, >= 0, default 0)
	 * @param yWeight - the priority of this component when distributing extra vertical space. (double, >= 0, default 0)
	 * @param position - the position of the component in its cell. (one of CENTER, NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST)
	 * @param extension - how the component extends to fill the place it has in its cell. (one of NONE, HORIZONTAL, VERTICAL, BOTH)
	 * @param margin - the margin of this component, for all sides.
	 */
	public static void add(Container container, Component component, int xPos, int yPos, double xWeight, double yWeight, int position, int extension, int margin) {
		container.add(component, new GridBagConstraints(xPos, yPos, 1, 1, xWeight, yWeight, position, extension, new Insets(margin, margin, margin, margin), 0, 0));
	}
	
	/**
	 * Adds the given component into the given container using the given GridBagConstraints.
	 * @param container - the container. (!= component)
	 * @param component - the component. (!= container)
	 * @param xPos - the horizontal position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param yPos - the vertical position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param xGrid - how much columns this components will fill. (int, >= 0, default 1)
	 * @param yGrid - how much rows this components will fill. (int, >= 0, default 1)
	 * @param xWeight - the priority of this component when distributing extra horizontal space. (double, >= 0, default 0)
	 * @param yWeight - the priority of this component when distributing extra vertical space. (double, >= 0, default 0)
	 * @param position - the position of the component in its cell. (one of CENTER, NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST)
	 * @param extension - how the component extends to fill the place it has in its cell. (one of NONE, HORIZONTAL, VERTICAL, BOTH)
	 * @param margin - the margin of this component, for all sides.
	 */
	public static void add(Container container, Component component, int xPos, int yPos, int xGrid, int yGrid, double xWeight, double yWeight, int position, int extension, int margin) {
		container.add(component, new GridBagConstraints(xPos, yPos, xGrid, yGrid, xWeight, yWeight, position, extension, new Insets(margin, margin, margin, margin), 0, 0));
	}
	
	/**
	 * Adds the given component into the given container using the given GridBagConstraints.
	 * @param container - the container. (!= component)
	 * @param component - the component. (!= container)
	 * @param xPos - the horizontal position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param yPos - the vertical position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param xWeight - the priority of this component when distributing extra horizontal space. (double, >= 0, default 0)
	 * @param yWeight - the priority of this component when distributing extra vertical space. (double, >= 0, default 0)
	 * @param position - the position of the component in its cell. (one of CENTER, NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST)
	 * @param extension - how the component extends to fill the place it has in its cell. (one of NONE, HORIZONTAL, VERTICAL, BOTH)
	 * @param top - the margin on the top of this component.
	 * @param left - the margin on the left of this component.
	 * @param bottom - the margin on the bottom of this component.
	 * @param right - the margin on the right of this component.
	 */
	public static void add(Container container, Component component, int xPos, int yPos, double xWeight, double yWeight, int position, int extension, int top, int left, int bottom, int right) {
		container.add(component, new GridBagConstraints(xPos, yPos, 1, 1, xWeight, yWeight, position, extension, new Insets(top, left, bottom, right), 0, 0));
	}
	
	/**
	 * Adds the given component into the given container using the given GridBagConstraints.
	 * @param container - the container. (!= component)
	 * @param component - the component. (!= container)
	 * @param xPos - the horizontal position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param yPos - the vertical position where the component have to be added. (int, >= 0 or RELATIVE)
	 * @param xGrid - how much columns this components will fill. (int, >= 0, default 1)
	 * @param yGrid - how much rows this components will fill. (int, >= 0, default 1)
	 * @param xWeight - the priority of this component when distributing extra horizontal space. (double, >= 0, default 0)
	 * @param yWeight - the priority of this component when distributing extra vertical space. (double, >= 0, default 0)
	 * @param position - the position of the component in its cell. (one of CENTER, NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST)
	 * @param extension - how the component extends to fill the place it has in its cell. (one of NONE, HORIZONTAL, VERTICAL, BOTH)
	 * @param top - the margin on the top of this component.
	 * @param left - the margin on the left of this component.
	 * @param bottom - the margin on the bottom of this component.
	 * @param right - the margin on the right of this component.
	 */
	public static void add(Container container, Component component, int xPos, int yPos, int xGrid, int yGrid, double xWeight, double yWeight, int position, int extension, int top, int left, int bottom, int right) {
		container.add(component, new GridBagConstraints(xPos, yPos, xGrid, yGrid, xWeight, yWeight, position, extension, new Insets(top, left, bottom, right), 0, 0));
	}
	
}
