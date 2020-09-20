package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * San Bernardino County, CA
 */
public class CASanBernardinoCountyCParser extends FieldProgramParser {

  private static final Pattern SUBJ_SRC_PTN = Pattern.compile("[A-Z]{3,4}");
  private static final Pattern GPS_PTN = Pattern.compile("\\?q=(-?\\d+\\.\\d{6},-?\\d+.\\d{6})\\b");

  public CASanBernardinoCountyCParser() {
    super("SAN BERNARDINO COUNTY", "CA",
          "( SELECT/RR CLOSE:UNIT! TIMES! Location:ADDRCITY " +
          "| CALL PLACE? ADDRCITY/Z! CROSS:X! RA:MAP! MAP:MAP! UNIT INFO! )");
  }

  @Override
  public String getFilter() {
    return "bducad@FIRE.CA.GOV,messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (SUBJ_SRC_PTN.matcher(subject).matches()) {
      data.strSource = subject;
    }

    if (body.startsWith("CLOSE:")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
      return parseFields(body.split("\n"), data);
    }

    setSelectValue("");

    int pt = body.lastIndexOf(" <a");
    if (pt >= 0) {
      Matcher match = GPS_PTN.matcher(body.substring(pt));
      if (match.find()) {
        setGPSLoc(match.group(1), data);
      }
      body = body.substring(0,pt).trim();
    }
    body = body.replace(")- ", ") - ");
    body = body.replace("- CROSS:", " - CROSS:");
    return parseFields(body.split(" - "), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " GPS";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern ID_CALL_PTN = Pattern.compile("Inc#(.*);(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1).trim();
        field = match.group(2).trim();
      }

      String call = CASanBernardinoCountyAParser.CALL_CODES.getProperty(field);
      if (call != null) {
        data.strCode = field;
        field = call;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID CODE CALL";
    }
  }

  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.*?)\\((.*)\\)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", field.substring(0,pt).trim());
        field = field.substring(pt+1).trim();
      }

      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPlace = append(data.strPlace, " - ", match.group(2).trim());
      }

      super.parse(field, data);
      data.strCity = data.strCity.toUpperCase().replace('_', ' ');
      data.strCity = convertCodes(data.strCity, CITY_CODES);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strMap = stripFieldStart(p.getLastOptional(" Grd: "), ":");
      String ch2 = p.getLastOptional("; Tac: ");
      String ch1 = p.getLastOptional("; Cmd: ");
      data.strChannel = append(ch1, "/", ch2);
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "X CH MAP";
    }
  }

  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" *; *");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = TIMES_BRK_PTN.matcher(field).replaceAll("\n");
    }
  }

  @Override
  public String adjustMapCity(String city) {
    city = stripFieldEnd(city, " AREA");
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BIG BEAR CA",    "BIG BEAR LAKE",
      "MORONGO",        "MORONGO VALLEY",
      "DHSP",           "DESERT HOT SPRINGS"
  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ARROWBEAR",      "RUNNING SPRINGS"
  });
}
