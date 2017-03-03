package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCPittCountyBParser extends DispatchOSSIParser {
  
  public NCPittCountyBParser() {
    super(CITY_CODES, "PITT COUNTY", "NC", 
          "( CANCEL ADDR CITY! " +
          "| CALL PLACE? ( ADDRCITY ( DATETIME! | ID ( EMPTY/Z PRI | PRI EMPTY? ) DATETIME! ) | ADDR/Z CITY ID PRI DATETIME! | ADDR/Z ID CITY? PRI DATETIME! | ADDR/Z PRI DATETIME! | ADDR/Z DATETIME! ) EMPTY? SRC UNIT Radio_Channel:CH? X+? ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@pittcountync.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{11}", false);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PRI")) return new PriorityField("[P1-9]", false);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AYDE", "AYDEN",
      "BELL", "BELL ARTHUR",
      "BETH", "BETHEL",
      "CHOC", "CHOCOWINITY",
      "FALK", "FALKLAND",
      "FARM", "FARMVILLE",
      "FOUN", "FOUNTAIN",
      "GREE", "GREENVILLE",
      "GRIF", "GRIFTON",
      "GRIM", "GRIMESLAND",
      "KINS", "KINSTON",
      "MACC", "MACCELSFIELD",
      "ROBE", "ROBERSONVILLE",
      "SIMP", "SIMPSON",
      "SNHL", "SNOW HILL",
      "STOK", "STOKES",
      "TARB", "TARBORO",
      "VANC", "VANCEBORO",
      "WALS", "WALSTONBURG",
      "WASH", "WASHINGTON",
      "WINT", "WINTERVILLE"
  });
}
