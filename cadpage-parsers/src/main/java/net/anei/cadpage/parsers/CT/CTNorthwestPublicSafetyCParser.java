package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNorthwestPublicSafetyCParser extends HtmlProgramParser {
  
  public CTNorthwestPublicSafetyCParser() {
    super("", "CT", 
          "PREFIX ( INFO/G TRAIL " + 
                 "| ADDR CITY PLACE CALL UNIT DATETIME/d! ID? GPS? TRAIL " + 
                 ") END");
  }
  
  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new SkipField("Please click here.*", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d?:\\d\\d:\\d\\d");
    if (name.equals("ID")) return new IdField("[A-Z]{2,5}: \\d{10}", true);
    if (name.equals("GPS")) return new GPSField("\\d{2}\\.\\d{5} -\\d{2}\\.\\d{5}", true);
    if (name.equals("TRAIL")) return new SkipField("If you have .*", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(" Unit#");
      if (pt >= 0) {
        apt = field.substring(pt+6).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

}
