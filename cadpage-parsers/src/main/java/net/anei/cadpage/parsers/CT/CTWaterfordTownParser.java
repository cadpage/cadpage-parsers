package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

/**
 * Waterford Town, CT
 */
public class CTWaterfordTownParser extends DispatchA3Parser {
  
  public CTWaterfordTownParser() {
    super(2, "", "WATERFORD TWP", "CT");
  }
  
  @Override
  public String getFilter() {
    return "wecc@waterfordct.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }
}
