package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFrederickCountyParser extends DispatchOSSIParser {

  public VAFrederickCountyParser() {
    super("FREDERICK COUNTY", "VA",
           "ADDR CALL! X? X? ID? INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@co.frederick.va.us,CAD@psb.net,cad@fcva.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }
}