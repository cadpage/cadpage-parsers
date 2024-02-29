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
    return "alert@emgcall.net,@ecm2.us,incident@wcvfd3.org";
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

    body = body.replace("X-sts:", "X-ST:").replace("Inc#:", "Inc:").replace(" CTLL:", " LL:").replaceAll("\\s+", " ");

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
      if (fld.startsWith("LL(")) {
        int pt = fld.indexOf(')');
        if (pt >= 0) {
          data.strAddress = fld.substring(0,pt+1);
          fld = fld.substring(pt+1).trim();
          first = false;
        }
      }
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
        if (data.strCity.isEmpty()) data.strCity = match.group(1).trim();
        if (data.strGPSLoc.isEmpty()) setGPSLoc(match.group(2), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY GPS " + super.getFieldNames();
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable(
      "ACCNI",              "VEHICLE ACCIDENT - NO INJURIES",
      "AFA",                "AUTOMATIC FIRE ALARM",
      "AFA-COMM",           "AUTOMATIC FIRE ALARM COMMERCIAL",
      "AFA-RESD",           "AUTOMATIC FIRE ALARM RESIDENTIAL",
      "AFAC",               "AUTOMATIC FIRE ALARM CANCEL",
      "AFAC-COMM",          "AUTOMATIC FIRE ALARM COMMERCIAL - CANCEL",
      "AFAC-RESD",          "AUTOMATIC FIRE ALARM RESIDENTIAL - CANCEL",
      "AMBAS",              "AMBULANCE ASSIST",
      "AMBAS-EMERG",        "AMBULANCE ASSIST EMERGENCY",
      "AMBAS-NON",          "AMBULANCE ASSIST NON EMERGENCY",
      "AMBASEMERG",         "AMBULANCE ASSIST EMERGENCY",
      "AMBASNON",           "AMBULANCE ASSIST NON EMERGENCY",
      "BOMB",               "BOMB THREAT",
      "BRUSH",              "BRUSH FIRE",
      "BRUSH-ENDG",         "BRUSH FIRE ENDANGERING A STRUCTURE",
      "BRUSH-NOT ENDG",     "BRUSH FIRE ",
      "CHIMN",              "CHIMNEY FIRE",
      "CHIMN-COMM",         "CHIMNEY FIRE COMMERCIAL",
      "CHIMN-RESD",         "CHIMNEY FIRE RESIDENTIAL",
      "DUMPSTER",           "DUMPSTER FIRE",
      "DUMPSTER-ENDG",      "DUMPSTER FIRE ENDANGERING A STRUCTURE",
      "DUMPSTER-NOT ENDG",  "DUMPSTER FIRE",
      "FIRE",               "STRUCTURE FIRE",
      "FIRE-COMM",          "COMMERCIAL STRUCTURE FIRE",
      "FIRE-RESD",          "RESIDENTIAL STRUCTURE FIRE",
      "FIRET",              "STRUCTURE FIRE - ENTRAPMENT",
      "FIRET-COMM",         "COMMERCIAL STRUCTURE FIRE - ENTRAPMENT",
      "FIRET-RESD",         "RESIDENTIAL STRUCTURE FIRE - ENTRAPMENT",
      "FLOOD",              "FLOODING",
      "FLOOD-COMM",         "FLOODING - COMMERCIAL",
      "FLOOD-RESD",         "FLOODING - RESIDENTIAL",
      "FLOOD-ROADWAY",      "FLOODED ROADWAY",
      "FTC",                "TRAFFIC CONTROL - FIRE",
      "FUEL",               "FUEL SPILL",
      "FUEL-COMM",          "FUEL SPILL, COMMERCIAL",
      "FUEL-RESIDENTIAL",   "FUEL SPILL, RESIDENTIAL",
      "FUEL-ROADWAY",       "FUEL SPILL, ROADWAY",
      "GASWELL",            "GAS WELL FIRE/EXPLOSION",
      "HAZ",                "GAS LEAK",
      "HAZ-HAZ1",           "GAS LEAK WITH EXPLOSION",
      "HAZ-HAZ2",           "GAS LEAK IN A STRUCTURE",
      "HAZ-HAZ3",           "GAS LEAK OUTSIDE",
      "LZ",                 "LANDING ZONE",
      "PS",                 "PUBLIC SERVICE DETAIL",
      "SMOKE",              "SMOKE INVESTIGATION",
      "TREE",               "TREE DOWN",
      "VF",                 "VEHICLE FIRE",
      "VF-CAR",             "VEHICLE FIRE PASSENGER CAR",
      "VF-TRUCK",           "VEHICLE FIRE COMMERCIAL VEHICLE",
      "VFE",                "VEHICLE FIRE ENDANGERING",
      "WIRES",              "WIRES DOWN"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARNOLD_C",    "ARNOLD",
      "GREENSBG_C",  "GREENSBURG",
      "JNT_C",       "JEANNETTE",
      "LATROBE_C",   "LATROBE",
      "L_BURL_C",    "LOWER BURRELL",
      "MONESSEN_C",  "MONESSEN",
      "NEW_KEN_C",   "NEW KENSINGTON",

      "MURRYSVL_M",   "MURRYSVILLE",

      "ADAMSBURG_B", "ADAMSBURG",
      "ARONA_B",     "ARONA",
      "AVONMORE_B",  "AVONMORE",
      "BOLIVAR_B",   "BOLIVAR",
      "DELMONT_B",   "DELMONT",
      "DERRY_B",     "DERRY",
      "DONEGAL_B",   "DONEGAL",
      "E_VAND_B",    "EAST VANDERGRIFT",
      "EXPORT_B",    "EXPORT",
      "HUNKER_B",    "HUNKER",
      "IRWIN_B",     "IRWIN",
      "MADISON_B",   "MADISON",
      "MANOR_B",     "MANOR",
      "MT_PLEAS_B",  "MT PLEASANT",
      "N_ALEX_B",    "NEW ALEXANDRIA",
      "N_B_VERN_B",  "NORTH BELLE VERNON",
      "N_FLOR_B",    "NEW FLORENCE",
      "N_IRWIN_B",   "NORTH IRWIN",
      "N_STANTN_B",  "NEW STANTON",
      "OKLAHOMA_B",  "OKLAHOMA",
      "PENN_B",      "PENN",
      "PLUM_B",      "PLUM",
      "S_GBG_B",     "SOUTH GREENSBURG",
      "SCOTTDL_B",   "SCOTTDALE",
      "SEWARD_B",    "SEWARD",
      "SMITHTON_B",  "SMITHTON",
      "SUTERSVL_B",  "SUTERSVILLE",
      "SW_GBG_B",    "SW GREENSBURG",
      "TRAFFORD_B",  "TRAFFORD",
      "VAND_B",      "VANDERGRIFT",
      "W_LEECHB_B",  "WEST LEECHBURG", //?
      "W_NEWTON_B",  "WEST NEWTON",
      "YNGSTWN_B",   "YOUNGSTOWN",
      "YNGWD_B",     "YOUNGWOOD",

      "ALLEGHNY_T",  "ALLEGHENY TWP",
      "BELL_T",      "BELL TWP",
      "COOK_T",      "COOK TWP",
      "DERRY_T",     "DERRY TWP",
      "DONEGAL_T",   "DONEGAL TWP",
      "E_HUNT_T",    "EAST HUNTINGDON TWP",
      "FAIR_T",      "FAIRFIELD TWP",
      "HEMP_T",      "HEMPFIELD TWP",
      "LIGONIER_T",  "LIGONIER",
      "LOYALHNA_T",  "LOYALHANNA",
      "MT_PLEAS_T",  "MT PLEASANT TWP",
      "N_HUNT_T",    "N HUNTINGDON TWP",
      "PENN_T",      "PENN TWP",
      "ROST_T",      "ROSTRAVER TWP",
      "SALEM_T",     "SALEM TWP",
      "S_HUNT_T",    "S HUNTINGDON TWP",
      "ST_CLAIR_T",  "ST CLAIR TWP",
      "SEWICK_T",    "SEWICKELY TWP",
      "U_BURL_T",    "UPPER BURRELL TWP",
      "UNITY_T",     "UNITY TWP",
      "WASH_T",      "WASHINGTON TWP"
  });
}
