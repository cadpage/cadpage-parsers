package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMoniteauCountyParser extends HtmlProgramParser {

  public MOMoniteauCountyParser() {
    this("MONITEAU COUNTY", "MO");
  }

  public MOMoniteauCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/HTML Call:EMPTY! CALL! Sub:EMPTY! CALL/S Place:EMPTY! PLACE Address:EMPTY! ADDR City:EMPTY! CITY Apt:EMPTY! APT Cross_Streets:EMPTY! X Event#:EMPTY! ID! Reporting_Person:EMPTY! NAME Phone#:EMPTY! PHONE Initiated:EMPTY! SKIP! INFO/N+ Units%EMPTY! UNIT/C+?  COPYRIGHT " +
          "| Call:CALL! Sub:CALL/S! Place:PLACE! Address:ADDR! ( City:CITY! Apt:APT! | Apt:APT! City:CITY! State:ST! Zip:ZIP! ) Cross_Streets:X! ( Latitude:GPS1 Longitude:GPS2! | Map_Coords:GPS | MapCoordinates:GPS | ) Event#:ID! Reporting_Person:NAME! Phone#:PHONE! ( Unit:UNIT! Initiated:SKIP! Notes:INFO! INFO/N+ | ( Initiated:SKIP! | Initiated_Date:SKIP! Initiated_Time:SKIP! ) INFO/N+ Units%EMPTY! UNIT/C+ ) " +
          "| ADDR! Apt:APT! CITY! Cross_Streets:X! Event#:ID! Reporting_Person:NAME! Phone#:PHONE! INFO/N+ Units%EMPTY! UNIT/C+ )");
  }

  @Override
  public String getAliasCode() {
    return"MOMoniteauCounty";
  }

  @Override
  public String getFilter() {
    return "CAD@MONITEAU911.COM,CADMONITEAU911@OMNIGO.COM,DISPATCH@OMNIGO.COM,noreply@omnigo.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<html>")) {
      setSelectValue("HTML");
      int pt = body.indexOf("\n©");
      if (pt >= 0) body  = body.substring(0,pt).trim();
      return super.parseHtmlMsg(subject, body, data);
    } else {
      setSelectValue("");
      return parseMsg(body, data);
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("COPYRIGHT")) return new SkipField("©.*", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyStateField extends StateField {
    @Override
    public void doParse(String field, Data data) {
      if (field.equals("M")) field = "MO";
      super.doParse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Call Received on ")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile(".*\"([-+]?\\d{2,3}\\.\\d{3,})\".*\"([-+]?\\d{2,3}\\.\\d{3,})\".*");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }
    }
  }
}
