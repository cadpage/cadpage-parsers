package net.anei.cadpage.parsers.VT;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VTLamoilleCountyParser extends FieldProgramParser {
  private String timeString;
  private boolean unitInfo;
  private Map<String, String> unitName;

  public VTLamoilleCountyParser() {
    this("LAMOILLE COUNTY", "VT");
  }

  VTLamoilleCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "SKIP+ Address:ADDRCITY Incident_Number:ID! Call_Type:CALL Narratives:INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getAliasCode() {
    return "VTLamoilleCounty";
  }

  @Override
  public String getFilter() {
    return "valcournotification@gmail.com,valcournotification@valcourcloud-vt.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    timeString = "";
    unitInfo = false;
    unitName = new HashMap<String, String>();
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(timeString, "\n", data.strSupp);
    }

    data.strAddress = data.strAddress.replace("Glen Dr", "Glenn Dr");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{2,6}\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ZIP_APT_PTN = Pattern.compile("(\\d{5}|J0[A-Z0-9 ]{3,5})(?: *# *(.*))?");
  private static final Pattern STATE_ZIP_APT_PTN = Pattern.compile("(VT|VRT|VVT|NH|NY|PQ|QC|Vermont)(?: *\\d{5}|J0[A-Z0-9 ]{3,5}())?(?: *# *(.*))?", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(.*?)(?: (VT|NH|NY|PQ|QC))?(?: +(\\d{5}|J0[A-Z0-9 ]{3,5}))?", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String addr = "";
      String apt = "";
      while (true) {
        Parser p = new Parser(field);
        String part = p.getLastOptional(',');
        if (part.equals("`")) part = p.getLastOptional(',');

        Matcher match = ZIP_APT_PTN.matcher(part);
        if (match.matches()) {
          data.strCity = match.group(1);
          apt = getOptGroup(match.group(2));
          part = p.getLastOptional(',');
        }

        match = STATE_ZIP_APT_PTN.matcher(part);
        if (match.matches()) {
          part = match.group(1).toUpperCase();
          if (part.startsWith("V")) part = "VT";
          data.strState = part;
          data.strCity = getOptGroup(match.group(2));
          apt = append(getOptGroup(match.group(3)), "-", apt);;
          part = p.getLastOptional(',');
        }

        if (!part.isEmpty()) {
          match = CITY_ST_ZIP_PTN.matcher(part);
          if (match.matches()) {
            part = match.group(1).trim();
            String state = match.group(2);
            if (state != null) data.strState = state.toUpperCase();
          }
        }
        part = part.replace(".", "");
        if (part.equalsIgnoreCase("StAlbans")) part = "St Albans";
        data.strCity = part;

        String tmp = p.getLast(',');
        if (tmp.startsWith("#")) {
          apt = append(tmp.substring(1).trim(), "-", apt);
          addr = p.getLast(',');
        } else {
          addr = tmp;
        }

        // If we end up with a state code for the address, something got sorted out of order
        // Drop the last token, reset everything and try again :(
        match = STATE_ZIP_APT_PTN.matcher(addr);
        if (match.matches()) {
          addr = apt = data.strState = data.strCity = "";
          int pt = field.lastIndexOf(',');
          if (pt < 0) abort();
          field = field.substring(0,pt).trim();
          continue;
        }

        // Otherwise the hard part is done
        data.strPlace = p.get();
        break;
      }

      if (!data.strCity.isEmpty()) {
        parseAddress(addr, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      }

      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY ST APT";
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("(.*) ([-+]?\\d{3}\\.\\d{5,}, *[-+]?\\d{3}\\.\\d{5,})");
  private static final Pattern UNIT_INFO_PATTERN_1 = Pattern.compile("Unit Id +(\\d):(.*)"),
                               UNIT_INFO_PATTERN_2 = Pattern.compile("(Dispatched|Enroute|On Scene|Cleared) +(\\d): +(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d)"),
                               UNIT_INFO_PATTERN_3 = Pattern.compile("(?:Medical|Fire) \\d:.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String uName = "";
      if (field.equals("")) return;
      if (field.startsWith("-----") && field.endsWith("-----")) return;

      if (data.strGPSLoc.isEmpty()) {
        Matcher match = INFO_GPS_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          setGPSLoc(match.group(2), data);
        }
      }

      if (field.equals("Units")) {
        unitInfo = true;
        return;
      }
      if (unitInfo) {
        Matcher m = UNIT_INFO_PATTERN_3.matcher(field);
        if (m.matches()) return;
        m = UNIT_INFO_PATTERN_1.matcher(field);
        if (m.matches()) {
          uName = m.group(2).trim();
          unitName.put(m.group(1), uName);
          data.strUnit = append(data.strUnit, ",", uName);
          return;
        }
        m = UNIT_INFO_PATTERN_2.matcher(field);
        if (m.matches()) {
          String entry = m.group(1);
          if (data.strTime.length() == 0 && entry.equals("Dispatched")) {
            data.strDate = m.group(3);
            data.strTime = m.group(4);
          }
          if (entry.equals("On Scene")) data.msgType = MsgType.RUN_REPORT;
          uName = getOptGroup(unitName.get(m.group(2)));
          if (uName == null) uName = "Unit "+m.group(2);
          field = uName+" "+entry+" "+m.group(3)+" "+m.group(4);
          timeString = append(timeString, "\n", field);
        }
        return;
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" GPS UNIT DATE TIME";
    }
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "54 W MAIN CIR",                        "+44.508832,-72.969448"
  });

  private static final String[] CITY_LIST = new String[]{

      // Addison County ************************************
      // City
      "VERGENNES",

      // Towns
      "ADDISON",
      "BRIDPORT",
      "BRISTOL",
      "CORNWALL",
      "FERRISBURGH",
      "GOSHEN",
      "GRANVILLE",
      "HANCOCK",
      "LEICESTER",
      "LINCOLN",
      "MIDDLEBURY",
      "MONKTON",
      "NEW HAVEN",
      "ORWELL",
      "PANTON",
      "RIPTON",
      "SALISBURY",
      "SHOREHAM",
      "STARKSBORO",
      "VERGENNES",
      "WALTHAM",
      "WEYBRIDGE",
      "WHITING",

      // Census-designated places
      "BRISTOL",
      "EAST MIDDLEBURY",
      "LINCOLN",
      "MIDDLEBURY",
      "NEW HAVEN",
      "SOUTH LINCOLN",

      // Other unincorporated communities
      "BREAD LOAF",
      "CHIMNEY POINT",
      "SATANS KINGDOM",

      // Bennington County *********************************
      // Towns
      "ARLINGTON",
      "BENNINGTON",
      "DORSET",
      "GLASTENBURY",
      "LANDGROVE",
      "MANCHESTER",
      "PERU",
      "POWNAL",
      "READSBORO",
      "RUPERT",
      "SANDGATE",
      "SEARSBURG",
      "SHAFTSBURY",
      "STAMFORD",
      "SUNDERLAND",
      "WINHALL",
      "WOODFORD",

      // Villages
      "MANCHESTER VILLAGE",
      "NORTH BENNINGTON",
      "OLD BENNINGTON",

      // Census-designated places
      "ARLINGTON",
      "BENNINGTON",
      "DORSET",
      "MANCHESTER CENTER",
      "MANCHESTER CTR",
      "READSBORO",
      "SOUTH SHAFTSBURY",


      // Chittenden County *************************************
      // Cities
      "BURLINGTON",
      "SOUTH BURLINGTON",
      "WINOOSKI",

      // Towns
      "BOLTON",
      "CHARLOTTE",
      "COLCHESTER",
      "ESSEX",
      "HINESBURG",
      "HUNTINGTON",
      "JERICHO",
      "MILTON",
      "RICHMOND",
      "SHELBURNE",
      "ST GEORGE",
      "UNDERHILL",
      "WESTFORD",
      "WILLISTON",
      "VILLAGES",
      "ESSEX JUNCTION",
      "JERICHO",

      //Census-designated places
      "BOLTON",
      "BOLTON VALLEY",
      "EAST CHARLOTTE",
      "HANKSVILLE",
      "HINESBURG",
      "HUNTINGTON",
      "HUNTINGTON CENTER",
      "MILTON",
      "RICHMOND",
      "SHELBURNE",
      "UNDERHILL CENTER",
      "UNDERHILL FLATS",
      "WEST CHARLOTTE",
      "WESTFORD",

      // Unincorporated communities
      "BUELS GORE",
      "JONESVILLE",

      // Franklin County ***************************************
      // City
      "ST ALBANS",

      // Towns
      "BAKERSFIELD",
      "BERKSHIRE",
      "ENOSBURGH",
      "FAIRFAX",
      "FAIRFIELD",
      "FLETCHER",
      "FRANKLIN",
      "GEORGIA",
      "HIGHGATE",
      "MONTGOMERY",
      "RICHFORD",
      "SHELDON",
      "SWANTON",

      // Villages
      "ENOSBURG FALLS",
      "SWANTON VILLAGE",

      // Census-designated places
      "FAIRFAX",
      "RICHFORD",

      // Lamoille County

      // Towns
      "BELVIDERE",
      "CAMBRIDGE",
      "EDEN",
      "ELMORE",
      "HYDE PARK",
      "JOHNSON",
      "MORRISTOWN",
      "STOWE",
      "WATERVILLE",
      "WOLCOTT",

      // Villages
      "CAMBRIDGE",
      "HYDE PARK",
      "JEFFERSONVILLE",
      "JOHNSON",
      "MORRISVILLE",

      // Census-designated place
      "STOWE",

      // Unincorporated community
      "MOSCOW",

      // Grand Isle County ***************************************
      // Towns
      "ALBURGH",
      "GRAND ISLE",
      "ISLE LA MOTTE",
      "NORTH HERO",
      "SOUTH HERO",

      // Orange County *******************************************
      // Towns
      "BRADFORD",
      "BRAINTREE",
      "BROOKFIELD",
      "CHELSEA",
      "CORINTH",
      "FAIRLEE",
      "NEWBURY",
      "ORANGE",
      "RANDOLPH",
      "STRAFFORD",
      "THETFORD",
      "TOPSHAM",
      "TUNBRIDGE",
      "VERSHIRE",
      "WASHINGTON",
      "WEST FAIRLEE",
      "WILLIAMSTOWN",

      // Villages
      "NEWBURY",
      "WELLS RIVER",

      // Census-designated places
      "BRADFORD",
      "CHELSEA",
      "FAIRLEE",
      "RANDOLPH",
      "WILLIAMSTOWN",

      // Unincorporated community
      "POST MILLS",


      // Orleans County ********************************************
      // City
      "NEWPORT",

      // Towns
      "ALBANY",
      "BARTON",
      "BROWNINGTON",
      "CHARLESTON",
      "COVENTRY",
      "CRAFTSBURY",
      "DERBY",
      "GLOVER",
      "GREENSBORO",
      "HOLLAND",
      "IRASBURG",
      "JAY",
      "LOWELL",
      "MORGAN",
      "TROY",
      "WESTFIELD",
      "WESTMORE",

      // Villages
      "BEEBE PLAIN",
      "DERBY CENTER",
      "DERBY LINE",
      "NORTH TROY",
      "ORLEANS",

      // Census-designated places
      "COVENTRY",
      "GLOVER",
      "GREENSBORO",
      "GREENSBORO BEND",
      "IRASBURG",
      "LOWELL",
      "NEWPORT CENTER",
      "TROY"
  };
}