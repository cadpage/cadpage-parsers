package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;


public class PAWestmorelandCountyAParser extends FieldProgramParser {

  private String cityCode;

  public PAWestmorelandCountyAParser() {
    super("WESTMORELAND COUNTY", "PA",
          "Loc:ADDR/S? X-ST:X? LL:GPS? Inc:ID! NATURE:CALL! CALLER:NAME? TOC:TIME Fire_TAC:CH? EMS_Tac:CH? Comments:INFO Response_text:SKIP Disp:UNIT");
  }

  @Override
  public String getFilter() {
    return "alert@emgcall.net,alert@emgcall.net,@ecm2.us,incident@wcvfd3.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    cityCode = null;

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace("X-sts:", "X-ST:").replace("Inc#:", "Inc:").replaceAll("\\s+", " ");

    if (!super.parseMsg(body, data)) return false;

    // Intersections go in the cross street and leave the Loc: field empty
    if (data.strAddress.length() == 0) {
      if (data.strCross.length() == 0) return false;
      data.strAddress = data.strCross;
      data.strCross = "";
    }

    // Look up possible call code
    if (!data.strCall.contains(" ")) {
      String call = CALL_CODES.getCodeDescription(data.strCall, true);
      if (call != null) {
        data.strCode = data.strCall;
        data.strCall = call;
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "ADDR " + super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  // Address field parser
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|UNIT) *(.*)|\\d+[A-Z]?|[A-Z]\\d*");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String fld, Data data) {
      boolean first = true;
      for (String part : fld.split(":")) {
        part = part.trim();
        if (first) {
          first = false;
          Parser p = new Parser(part);
          String apt = p.getLastOptional(';');
          if (apt.length() > 0) {
            Matcher match = ADDR_APT_PTN.matcher(apt);
            if (match.matches()) {
              String tmp = match.group(1);
              if (tmp != null) apt = tmp;
            }
          }
          cityCode = p.getLast(' ');
          data.strCity = convertCodes(cityCode, CITY_CODES);
          super.parse(p.get(), data);
          data.strApt = append(data.strApt, "-", apt);
        }

        else {
          if (part.startsWith("@")) {
            data.strPlace = append(data.strPlace, " - ", part.substring(1).trim());
          } else {
            Matcher match = ADDR_APT_PTN.matcher(part);
            if (match.matches()) {
              String apt = match.group(1);
              if (apt == null) apt = part;
              data.strApt = append(data.strApt, "-", apt);
            } else {
              data.strPlace = append(data.strPlace, " - ", part);
            }
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (cityCode != null) field = stripFieldEnd(field, cityCode);
      super.parse(field, data);
    }
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      data.strChannel = append(data.strChannel, " / ", field);
    }
  }

  private static final Pattern INFO_CITY_GPS_PTN = Pattern.compile("([A-Z ]+) ([-+]\\d{3}\\.\\d{5,} [-+]\\d{3}\\.\\d{5,})\\b\\s*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CITY_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        if (data.strCity.length() == 0) data.strCity = match.group(1).trim();
        if (data.strGPSLoc.length() == 0) setGPSLoc(match.group(2), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY GPS " + super.getFieldNames();
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MONESSEN_C",  "MONESSEN",

      "ADAMSBURG_B", "ADAMSBURG",
      "IRWIN_B",     "IRWIN",
      "MADISON_B",   "MADISON",
      "MANOR_B",     "MANOR",
      "MT_PLEAS_B",  "MT PLEASANT",
      "N_B_VERN_B",  "NORTH BELLE VERNON",
      "N_FLOR_B",    "NEW FLORENCE",
      "N_IRWIN_B",   "NORTH IRWIN",
      "N_STANTN_B",  "NEW STANTON",
      "PENN_B",      "PENN",
      "SCOTTDL_B",   "SCOTTDALE",
      "SMITHTON_B",  "SMITHTON",
      "SUTERSVL_B",  "SUTERSVILLE",
      "SW_GBG_B",    "SW GREENSBURG",
      "TRAFFORD_B",  "TRAFFORD",
      "W_NEWTON_B",  "WEST NEWTON",
      "YNGWD_B",     "YOUNGWOOD",

      "HEMP_T",      "HEMPFIELD TWP",
      "MT_PLEAS_T",  "MT PLEASANT TWP",
      "N_HUNT_T",    "N HUNTINGDON TWP",
      "PENN_T",      "PENN TWP",
      "ROST_T",      "ROSTRAVER TWP",
      "S_HUNT_T",    "S HUNTINGDON TWP",
      "ST_CLAIR_T",  "ST CLAIR TWP",
      "SEWICK_T",    "SEWICKELY TWP",
      "WASH_T",      "WASHINGTON TWP"
  });
}
