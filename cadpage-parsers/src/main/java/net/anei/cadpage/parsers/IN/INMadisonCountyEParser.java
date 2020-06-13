package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class INMadisonCountyEParser extends DispatchH05Parser {
  
  public INMadisonCountyEParser() {
    super("MADISON COUNTY", "IN", 
          "DATE:DATETIME! CFS#:SKIP? STARS? PLACE:PLACE! ADDR:ADDRCITY! CROSS_STREETS:X! STARS! CALL:CALL! STARS! UNIT:UNIT! STARS! INFO:INFO! STARS! FIRE_RD:CH! ( GOOGLE_MAP:EMPTY SKIP | ) STARS! ( CAD_#:ID! STARS! | Incident_Numbers_#:ID STARS! | ) GPS_LAT:GPS1! GPS_LON:GPS2! HASHES! NARRATIVE:SKIP INFO_BLK+? TIMES:SKIP TIMES+");
  }
  
  @Override
  public String getFilter() {
    return "@madisoncounty.in.gov,cfs@madisoncountypaging.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("STARS")) return new SkipField("\\*{3,}", true);
    if (name.equals("HASHES")) return new SkipField("#{3,}", true);
    return super.getField(name);
  }
}
