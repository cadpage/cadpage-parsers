package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAShenandoahCountyParser extends HtmlProgramParser {
  
  public VAShenandoahCountyParser() {
    super("SHENANDOAH COUNTY", "VA", 
          "DATETIME CALL ADDRCITY! Cross_Street:X! GPS UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@shentel.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(',').replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      data.strCity = p.get(',');
      if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
      String apt = p.get();
      if (!apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("Lat: *(.*) Long: *(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+','+match.group(2), data);
    }
  }
}
