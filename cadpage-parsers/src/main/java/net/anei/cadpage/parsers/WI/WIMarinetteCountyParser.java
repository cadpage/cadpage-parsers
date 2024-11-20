package net.anei.cadpage.parsers.WI;

import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIMarinetteCountyParser extends FieldProgramParser {

  public WIMarinetteCountyParser() {
    super("MARINETTE COUNTY", "WI",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY? DATE:DATE! TIME:TIME! GPS:GPS! AUDIO:URL! INFO:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "pager@fastmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd MMM yyyy");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("EMS: CADPage Data"))  return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField(DATE_FMT, true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d");
    return super.getField(name);
  }
}
