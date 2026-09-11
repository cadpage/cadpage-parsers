package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHFranklinCountyBParser extends DispatchH05Parser {

  public OHFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "OH",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYST! DATE:DATETIME! RADIO:CH! Units:UNIT! INFO:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "nwps@grovecityohio.gov";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
