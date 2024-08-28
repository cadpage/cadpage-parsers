package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXWilliamsonCountyParser extends DispatchOSSIParser {

  public TXWilliamsonCountyParser() {
    super(CITY_CODES, "WILLIAMSON COUNTY", "TX",
          "( CANCEL! | FYI? CALL! ) PRI? ( PLACE ADDR/ZS9 CITY! | ADDR/ZS9! CITY? ) ( X X? | ) ( SPLACE PLACE_MAP | PLACE_MAP? ) UNIT/C+");
    setupCities(CITY_LIST);
    removeWords("RM");    // RM = RANCH-TO-MARKET
  }

  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(FYI|Update)( +;.*)", Pattern.DOTALL);
  @Override protected boolean parseMsg(String subject, String body, Data data) {

    if (!body.startsWith("CAD:")) {
      Matcher match = MISSING_COLON_PTN.matcher(body);
      if (match.matches()) body = match.group(1)+':'+match.group(2);
      body = "CAD:" + body;
    }

    if (body.startsWith("CAD:ATTN ")) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(4);
      return true;
    }

    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[P0-9]");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SPLACE")) return new MySPlaceField();
    if (name.equals("PLACE_MAP")) return new MyPlaceMapField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strAddress.endsWith(" LOOP") && !data.strApt.isEmpty()) {
        data.strAddress = append(data.strAddress, " ", data.strApt);
        data.strApt = "";
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains("(")) return false;
      if (field.contains("PVR")) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  private class MySPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(field, " - ", data.strPlace);
    }
  }

  private static Pattern PLACE_MAP_PAT = Pattern.compile("[.\\\\]?(?:(.*)\\(S\\)(.*?) )?\\(N\\)(.*?)");
  private class MyPlaceMapField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher mat = PLACE_MAP_PAT.matcher(field);
      if (!mat.matches()) return false;
      data.strPlace = append(data.strPlace, " - ", append(getOptGroup(mat.group(1)), " - ", getOptGroup(mat.group(2))));
      data.strMap = mat.group(3).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE MAP";
    }
  }

  private static final Pattern UNIT_CALL_PTN = Pattern.compile("FALL|.* .*|[A-Z]{6,}");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("REF#")) return;
      if (UNIT_CALL_PTN.matcher(field).matches()) {
        data.strCall = append(data.strCall, " - ", field);
      } else {
        super.parse(field, data);
      }
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return PVR_PTN.matcher(addr).replaceAll("PVT RD");
  }
  private static final Pattern PVR_PTN = Pattern.compile("\\bPVR\\b", Pattern.CASE_INSENSITIVE);

  private static final String[] CITY_LIST = new String[] {

      // Cities shared with other counties
      "AUSTIN",
      "BARTLETT",
      "CEDAR PARK",
      "LEANDER",
      "PFLUGERVILLE",
      "ROUND ROCK",
      "THORNDALE",

      // Cities
      "COUPLAND",
      "FLORENCE",
      "GEORGETOWN",
      "GRANGER",
      "HUTTO",
      "JARRELL",
      "LIBERTY HILL",
      "TAYLOR",
      "THRALL",
      "WEIR",

      // Census-designated places
      "BRUSHY CREEK",
      "JOLLYVILLE",
      "SANTA RITA RANCH",
      "SERENADA",
      "SONTERRA",

      // Unincorporated communities
      "JONAH",
      "MACEDONIA",
      "NORMANS CROSSING",
      "RICES CROSSING",
      "SCHWERTNER",
      "WALBURG",

      // Ghost towns
      "PALM VALLEY"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AU",      "AUSTIN",
      "BT",      "BARTLETT",
      "CL",      "COUPLAND",
      "CP",      "CEDAR PARK",
      "EL",      "ELGIN",
      "FL",      "FLORENCE",
      "GR",      "GRANGER",
      "GT",      "GEORGETOWN",
      "HU",      "HUTO",
      "JA",      "JARRELL",
      "LE",      "LEANDER",
      "LH",      "LIBERTY HILL",
      "RR",      "ROUND ROCK",
      "TA",      "TAYLOR",
      "TD",      "THORNDALE",
      "TH",      "THRALL",
      "WR",      "WEIR"
  });
}
