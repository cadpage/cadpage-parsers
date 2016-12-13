package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCPittCountyBParser extends DispatchOSSIParser {
  
  public NCPittCountyBParser() {
    super(CITY_CODES, "PITT COUNTY", "NC", 
          "CALL PLACE? ( ADDRCITY ID PRI DATETIME! | ADDR/Z CITY ID PRI DATETIME! | ADDR/Z ID PRI DATETIME! | ADDR/Z PRI DATETIME! | ADDR/Z DATETIME! ) SRC UNIT Radio_Channel:CH INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@pittcountync.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{11}", true);
    if (name.equals("PRI")) return new PriorityField("[P1-9]", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
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
