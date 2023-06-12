package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class CONorthglennEMSParser extends DispatchProQAParser {

  public CONorthglennEMSParser() {
    super("ADAMS COUNTY", "CO",
           "( ID ADDR CALL! CALL+ | ADDR APT CITY ZIP/Y CALL+? ID! INFO+ )", true);
  }

  @Override
  public String getFilter() {
    return "@northglennambulance.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }

  private class MyZipField extends ZipField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
}
