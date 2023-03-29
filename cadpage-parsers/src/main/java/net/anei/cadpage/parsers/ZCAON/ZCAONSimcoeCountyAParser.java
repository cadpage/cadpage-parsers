package net.anei.cadpage.parsers.ZCAON;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAONSimcoeCountyAParser extends FieldProgramParser {

  public ZCAONSimcoeCountyAParser() {
    super(CITY_LIST, "SIMCOE COUNTY", "ON",
          "URL ADDR/S6 PLACE? CITY/Y CALL! Caller_Name:NAME? INFO/N+");
    setupProtectedNames("15-16 SIDEROAD");
  }

  @Override
  public String getFilter() {
    return "cad@barrie.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Trip Ticket")) return false;
    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("URL")) return new InfoUrlField("http://911txt.co/.*", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private static  Pattern INTERSECT_PTN = Pattern.compile("(.*?), (.*) INTERSECT");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INTERSECT_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = "";
        parseAddress(match.group(1).trim()+" & "+match.group(2), data);
      } else {
        if (field.endsWith(")")) {
          int pt = field.lastIndexOf('(');
          if (pt >= 0) field = field.substring(0, pt).trim();
        }
        super.parse(field, data);
      }
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return ZCAONSimcoeCountyParser.doAdjustMapAddress(sAddress);
  }

  private static final String[] CITY_LIST = new String[]{
      "ADJALA-TOSORONTIO",
      "BARRIE",
      "BRADFORD",
      "BRADFORD WEST GWILLIMBURY",
      "CLEARVIEW",
      "COLLINGWOOD",
      "ESSA",
      "INNISFIL",
      "MIDLAND",
      "NEW TECUMSETH",
      "ORILLA",
      "ORO-MEDONTE",
      "PENETANGUISHENE",
      "RAMARA",
      "SEVERN",
      "SPRINGWATER",
      "TAY",
      "TINY",
      "WASAGA BEACH",

      // Grey County
      "BLUE MOUNTAINS",
      "GREY HIGHLANDS",

      // Muskoka Regioin
      "BEAUSOLEIL",
      "DUFFERIN",
      "GEORGIAN BAY",
      "HUNTSVILLE",
      "LAKE OF BAYS"
  };
}
