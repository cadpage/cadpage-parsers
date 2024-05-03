package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLPalmBeachCountyParser extends FieldProgramParser {

  public FLPalmBeachCountyParser() {
    super(CITY_CODES, "PALM BEACH COUNTY", "FL",
          "Type:CALL! Event_Location:ADDR/S4? APT Dev:PLACE? Map_page:MAP? Map_Coord:MAP? Talk_Group:CH? TIME:TIME! ( lat/lon:GPS1! GPS2! EMPTY? | ) Disp:UNIT! UNIT/S+");
  }

  @Override
  public String getFilter() {
    return "FIRE-ALERT-CAD@pbcgov.org,FIRE-ALERT-CAD@pbc.gov>";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static Pattern DOTDOTDOT = Pattern.compile("\\.{3,}");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("IPS I/Page Notification")) return false;

    //remove \\.{3,}
    body = DOTDOTDOT.matcher(body).replaceAll(" ");

    body = body.replace(" Disp:", ",Disp:").replace(" lat/lon:", ",lat/lon:");

    return parseFields(body.split(","), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    return super.getField(name);
  }

  private static final Pattern COLON_AT_PTN = Pattern.compile(":(?: @)?");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(COLON_AT_PTN);
      String place = p.get(":APT ").trim();
      String apt = p.get();

      String city = CITY_CODES.getProperty(addr);
      if (city != null) {
        data.strCity = city;
        addr = place;
        place = "";
      }
      super.parse(addr, data);
      data.strPlace = append(data.strPlace, " - ", place);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = COLON_AT_PTN.matcher(field);
      if (match.find()) {
        data.strPlace = append(data.strPlace, " - ", field.substring(match.end()).trim());
        field = field.substring(0, match.start()).trim();
      }
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strPlace.equals(field)) data.strPlace = append(data.strPlace, " - ", field);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, " / ", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BG",    "Belle Glade",
      "BR",    "Boca Raton",
      "CL",    "Cloud Lake",
      "DB",    "Delray Beach",
      "GA",    "Green Acres",
      "GR",    "Glren Ridge",
      "HB",    "Highland Beach",
      "JB",    "Juno Beach",
      "JIC",   "Jupiter Inlet Beach Colony",
      "JP",    "Jupiter",
      "LAN",   "Lantana",
      "LCS",   "Lake Clark Shores",
      "LG",    "Loxahatchee Groves",
      "LP",    "Lake Park",
      "LW",    "Lake Worth",
      "MC",    "Martin County",
      "MP",    "Mangonia Park",
      "NPB",   "North Palm Beach",
      "OR",    "Ocean Ridge",
      "PB",    "Palm Beach",
      "PBC",   "Palm Beach County",
      "PBG",   "Palm Beach Gardens",
      "PBS",   "Palm Beach Shores",
      "PS",    "Palm Springs",
      "RB",    "Riviera Beach",
      "RPB",   "Royal Palm Beach",
      "SB",    "South Bay",
      "SPB",   "South Palm Beach",
      "TQ",    "Tequesta",
      "WL",    "Wellington",
      "WPB",   "West Palm Beach"
  });
}

