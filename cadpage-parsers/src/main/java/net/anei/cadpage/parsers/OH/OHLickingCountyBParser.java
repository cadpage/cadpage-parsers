package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLickingCountyBParser extends FieldProgramParser {

  public OHLickingCountyBParser() {
    super("LICKING COUNTY", "OH",
          "Nature:CALL! Radio:CH! Loc:ADDRCITY/S2P! Coord:GPS! CAD_Zone:MAP! Notes:INFO! INFO/N+");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "911dispatch@lcounty.com";
  }

  @Override
  public int getMapFlags( ) {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern ADDR_X_PTN = Pattern.compile("(.*?)\\((.*)\\)(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = "";
      Matcher match = ADDR_X_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCross = match.group(2).trim();
        zip = match.group(3);
      }
      super.parse(field, data);
      int pt = data.strCity.indexOf('#');
      if (pt >= 0) {
        data.strApt = append(data.strApt, "-", data.strCity.substring(pt+1).trim());
        data.strCity = data.strCity.substring(0,pt).trim();
      }
      if (data.strCity.isEmpty()) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "BLUFF RIDGE",
      "CARRY BACK",
      "CEDAR PARK",
      "CUMBERLAND CROSSING",
      "DEN TON",
      "DUSKY WILLOW",
      "ENOS LOOMIS",
      "EPHRAIM MUNSELL",
      "HARVEST MOON",
      "HEADLEYS MILL",
      "JEFFERSON RIDGE",
      "JOHN REESE",
      "KELLY MARIE",
      "LEXINGTON WOODS",
      "NORTH BEND",
      "NORTH END",
      "PAT HAVEN",
      "SHELTER COVE",
      "STONEY RIDGE",
      "SUGAR MILL",
      "TAYLOR MILLS",
      "TAYLOR WOODS",
      "THOMAS CHRISTOPHER",
      "TWENTY GRAND",
      "VICTORIA MEADOW",
      "WEEPING WILLOW",
      "WESLEYAN CHURCH"
  };
}
