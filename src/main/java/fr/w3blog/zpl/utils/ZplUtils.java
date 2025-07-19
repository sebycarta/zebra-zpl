package fr.w3blog.zpl.utils;

import fr.w3blog.zpl.constant.ZebraFont;
import fr.w3blog.zpl.constant.ZebraPPP;

/**
 * Common method used to manipulate ZPL
 * 
 * @author ttropard
 * 
 */
public class ZplUtils {

	/**
	 * Function called by zplCommand to cast variable object and ajust for zpl code
	 * 
	 * @param object
	 */
	private static String variableObjectToZplCode(Object object) {
		if (object != null) {
			if (object instanceof Integer) {
				return (Integer.toString((Integer) object));
			} else if (object instanceof Boolean) {
				if (((Boolean) object).booleanValue()) {
					return "Y";
				} else {
					return "N";
				}
			} else {
				return object.toString();
			}
		} else {
			return "";
		}
	}

	/**
	 * Method to quickly generate zpl code with command and variable
	 * 
	 * @param command
	 *                  Command (without ^)
	 * @param variables
	 *                  list variable
	 * @return
	 */
	public static StringBuilder zplCommand(String command) {
		StringBuilder zpl = new StringBuilder();
		zpl.append("^");
		zpl.append(command);
		return zpl;
	}

	/**
	 * Method to quickly generate zpl code with command and variable
	 * 
	 * @param command
	 *                  Command (without ^)
	 * @param variables
	 *                  list variable
	 * @return
	 */
	public static StringBuilder zplCommandSautLigne(String command) {
		StringBuilder zpl = zplCommand(command);
		zpl.append("\n");
		return zpl;
	}

	/**
	 * Method to quickly generate zpl code with command and variable
	 * 
	 * @param command
	 *                  Command (without ^)
	 * @param variables
	 *                  list variable
	 * @return
	 */
	public static StringBuilder zplCommand(String command, Object... variables) {
		StringBuilder zpl = new StringBuilder();
		zpl.append("^");
		zpl.append(command);
		if (variables.length > 1) {
			zpl.append(variableObjectToZplCode(variables[0]));
			for (int i = 1; i < variables.length; i++) {
				zpl.append(",");
				zpl.append(variableObjectToZplCode(variables[i]));
			}
		} else if (variables.length == 1) {
			// Only one element in variables
			zpl.append(variableObjectToZplCode(variables[0]));
		}
		return zpl;
	}

	/**
	 * Method to quickly generate zpl code with command and variable
	 * 
	 * @param command
	 *                  Command (without ^)
	 * @param variables
	 *                  list variable
	 * @return
	 */
	public static StringBuilder zplCommandSautLigne(String command, Object... variables) {
		StringBuilder zpl = zplCommand(command, variables);
		zpl.append("\n");
		return zpl;
	}

	/**
	 * Extract from font, fontSize and PPP the height and width in dots.
	 * 
	 * Fonts and PPP are not all supported.
	 * Please complete this method or use dot in yous params
	 * 
	 * @param zebraFont
	 * @param fontSize
	 * @param zebraPPP
	 * @return array[height,width] in dots
	 */
	public static Integer[] extractDotsFromFont(ZebraFont zebraFont, int fontSize, ZebraPPP zebraPPP) {
		Integer[] array = new Integer[2];
		// Default ratios for ZEBRA_ZERO (you can add other mappings based on empirical tests or Zebra documentation)
		float ratioHeight = 0f;
		float ratioWidth = 0f;

		if (ZebraFont.ZEBRA_ZERO.equals(zebraFont)) {
			switch (zebraPPP) {
				case DPI_203:
				// These values should be obtained from tests or official documentation (these are "typical" values)
					ratioHeight = 2.83f;
					ratioWidth = 2.74f;
					break;
				case DPI_300:
					ratioHeight = 4.16f;
					ratioWidth = 4.06f;
					break;
				case DPI_600:
					ratioHeight = 8.33f;
					ratioWidth = 8.13f;
					break;
				default:
					throw new UnsupportedOperationException("PPP not supported for ZEBRA_ZERO.");
			}
		} else if (ZebraFont.ZEBRA_A.equals(zebraFont)) {
			switch (zebraPPP) {
				case DPI_203:
					ratioHeight = 2.83f;
					ratioWidth = 2.12f;
					break;
				case DPI_300:
					ratioHeight = 4.16f;
					ratioWidth = 3.12f;
					break;
				case DPI_600:
					ratioHeight = 8.33f;
					ratioWidth = 6.25f;
					break;
				default:
					throw new UnsupportedOperationException("PPP not supported for ZEBRA_A.");
			}
		} else {
			throw new UnsupportedOperationException("This font is not supported.");
		}

		array[0] = Math.round(fontSize * ratioHeight); // Height
		array[1] = Math.round(fontSize * ratioWidth); // Width

		return array;
	}

	/**
	 * Convert point(pt) in pixel(px)
	 * 
	 * @param point
	 * @return
	 */
	public static Integer convertPointInPixel(int point) {
		return Math.round(point * 1.33F);
	}

	/**
	 * Function used to converted ASCII >127 in \hexaCode accepted by ZPL language
	 * 
	 * @param str
	 *            str
	 * @return string with charactere remove
	 */
	public static String convertAccentToZplAsciiHexa(String str) {
		if (str != null) {
			str = str.replace("é", "\\82");
			str = str.replace("à", "\\85");
			str = str.replace("è", "\\8A");
		}
		return str;
	}
}
