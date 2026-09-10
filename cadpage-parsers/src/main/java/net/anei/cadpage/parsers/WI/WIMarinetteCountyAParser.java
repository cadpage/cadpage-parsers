package net.anei.cadpage.parsers.WI;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIMarinetteCountyAParser extends FieldProgramParser {

  public WIMarinetteCountyAParser() {
    super("MARINETTE COUNTY", "WI",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY? DATE:DATE! TIME:TIME! GPS:GPS? CROSS_ST:X! ( PAGE:URL! | AUDIO:URL! | AUDIO_1:URL! ) INFO:SKIP! MAP:GPS? END");
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

  private static final Pattern DIR_ADDR_PTN = Pattern.compile("([NSEW])(\\d.*)");

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = DIR_ADDR_PTN.matcher(addr);
    if (match.matches()) addr = match.group(2)+' '+match.group(1);
    return addr;
  }
}
