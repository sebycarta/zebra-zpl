package fr.w3blog.zpl.model.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.w3blog.zpl.model.PrinterOptions;
import fr.w3blog.zpl.model.ZebraElement;
import fr.w3blog.zpl.utils.ZplUtils;

/**
 * Abstract Zebra element to represent a bar code instruction
 * 
 * Command ZPL : All instruction starting ^B
 * 
 * @author ttropard
 * 
 */
public abstract class ZebraBarCode<T extends ZebraBarCode<T>> extends ZebraElement<T> {

	Integer barCodeHeigth;

	Integer moduleWidth;
	Integer wideBarRatio;

	/**
	 * Parameters used to print text( default on bellow)
	 * 
	 */
	boolean showTextInterpretation = true;

	/**
	 * Parameters to set to true if you want textInterpretation above code (top)
	 */
	boolean showTextInterpretationAbove = false;

	String text;

	public ZebraBarCode(int positionX, int positionY, String text) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.text = text;
	}

	/**
	 * Default Constructeur width position and text
	 * 
	 * @param positionX
	 *                      left margin (explain in dots)
	 * @param positionY
	 *                      top margin (explain in dots)
	 * @param text
	 *                      code to write
	 * @param barCodeHeigth
	 *                      height of code bar
	 */
	public ZebraBarCode(int positionX, int positionY, String text, int barCodeHeigth) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.barCodeHeigth = barCodeHeigth;
		this.text = text;
	}

	/**
	 * Default Constructeur width position and text
	 * 
	 * @param positionX
	 *                      left margin (explain in dots)
	 * @param positionY
	 *                      top margin (explain in dots)
	 * @param text
	 *                      code to write
	 * @param barCodeHeigth
	 *                      height of code bar
	 * @param barCodeWidth
	 *                      width(optionnal) of code bar
	 * @param wideBarRatio
	 *                      wide bar to narrow bar width ratio
	 */
	public ZebraBarCode(int positionX, int positionY, String text, int barCodeHeigth, int moduleWidth,
			int wideBarRatio) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.barCodeHeigth = barCodeHeigth;
		this.text = text;
		this.moduleWidth = moduleWidth;
		this.wideBarRatio = wideBarRatio;
	}

	/**
	 * Default Constructor width position and text
	 * 
	 * @param positionX
	 *                               left margin (explain in dots)
	 * @param positionY
	 *                               top margin (explain in dots)
	 * @param text
	 *                               code to write
	 * @param barCodeHeigth
	 *                               height of code bar
	 * @param showTextInterpretation
	 *                               true to print interpretation line
	 * @param moduleWidth
	 *                               width(optionnal) of code bar
	 * @param wideBarRatio
	 *                               wide bar to narrow bar width ratio
	 */
	public ZebraBarCode(int positionX, int positionY, String text, int barCodeHeigth, boolean showTextInterpretation,
			int moduleWidth, int wideBarRatio) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.barCodeHeigth = barCodeHeigth;
		this.showTextInterpretation = showTextInterpretation;
		this.text = text;
		this.moduleWidth = moduleWidth;
		this.wideBarRatio = wideBarRatio;
	}

	/**
	 * Constructeur used to print text (above or below) with code
	 * 
	 * @param positionX
	 *                                    left margin (explain in dots)
	 * @param positionY
	 *                                    top margin (explain in dots)
	 * @param text
	 *                                    code to write
	 * @param barCodeHeigth
	 *                                    height of code bar
	 * @param showTextInterpretation
	 *                                    true to print interpretation line
	 * @param showTextInterpretationAbove
	 *                                    true to add above, false to add below
	 */
	public ZebraBarCode(int positionX, int positionY, String text, int barCodeHeigth, boolean showTextInterpretation,
			boolean showTextInterpretationAbove) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.barCodeHeigth = barCodeHeigth;
		this.text = text;
		this.showTextInterpretation = showTextInterpretation;
		this.showTextInterpretationAbove = showTextInterpretationAbove;
	}

	protected abstract T getThis();

	public StringBuilder getStartZplCodeBuilder(PrinterOptions printerOptions) {
		StringBuilder zpl = new StringBuilder();
		// On précise la position
		zpl.append(printerOptions!=null && printerOptions.isScalingEnabled() ? getZplCodePosition(printerOptions.getZebraPPP())
				: getZplCodePosition());
		zpl.append("\n");
		if (moduleWidth != null) {
			zpl.append(ZplUtils.zplCommandSautLigne("BY", moduleWidth, wideBarRatio, barCodeHeigth));
		}
		return zpl;
	}

	// public StringBuilder getStartZplCodeBuilder() {
	// 	StringBuilder zpl = new StringBuilder();
	// 	// On précise la position
	// 	zpl.append(getZplCodePosition());
	// 	zpl.append("\n");
	// 	if (moduleWidth != null) {
	// 		zpl.append(ZplUtils.zplCommandSautLigne("BY", moduleWidth, wideBarRatio, barCodeHeigth));
	// 	}
	// 	return zpl;
	// }

	/**
	 * Used to draw label preview.
	 * This method should be overloader by child class.
	 * 
	 * Default draw a rectangle
	 * 
	 * @param graphic
	 */
	public void drawPreviewGraphic(PrinterOptions printerOptions, Graphics2D graphic) {
		int top = 0;
		int left = 0;
		if (positionX != null) {
			left = ZplUtils.convertPointInPixel(positionX);
		}
		if (positionY != null) {
			top = ZplUtils.convertPointInPixel(positionY);
		}
		graphic.setColor(Color.BLACK);

		Font font = new Font("Arial", Font.BOLD, barCodeHeigth / 2);

		graphic.drawRect(left, top,
				ZplUtils.convertPointInPixel(Math.round(moduleWidth * wideBarRatio * 9 * text.length())),
				ZplUtils.convertPointInPixel(barCodeHeigth));

		drawTopString(graphic, font, text, left, top);
	}

	public Integer getBarCodeWidth() {
		return moduleWidth;
	}

	public Integer getBarCodeHeigth() {
		return barCodeHeigth;
	}

	public Integer getWideBarRatio() {
		return wideBarRatio;
	}

	public boolean isShowTextInterpretation() {
		return showTextInterpretation;
	}

	public boolean isShowTextInterpretationAbove() {
		return showTextInterpretationAbove;
	}

	public String getText() {
		return text;
	}

	public T withBarCodeWidth(Integer barCodeWidth) {
		this.moduleWidth = barCodeWidth;
		return getThis();
	}

	public T withBarCodeHeigth(Integer barCodeHeigth) {
		this.barCodeHeigth = barCodeHeigth;
		return getThis();
	}

	public T withWideBarRatio(Integer wideBarRatio) {
		this.wideBarRatio = wideBarRatio;
		return getThis();
	}

	public T withShowTextInterpretation(boolean showTextInterpretation) {
		this.showTextInterpretation = showTextInterpretation;
		return getThis();
	}

	public T withShowTextInterpretationAbove(boolean showTextInterpretationAbove) {
		this.showTextInterpretationAbove = showTextInterpretationAbove;
		return getThis();
	}

	public T withText(String text) {
		this.text = text;
		return getThis();
	}

}
