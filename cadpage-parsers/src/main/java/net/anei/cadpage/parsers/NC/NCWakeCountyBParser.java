package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCWakeCountyBParser extends DispatchOSSIParser {

  public NCWakeCountyBParser() {
    super(CITY_CODES, "CARY", "NC",
          "( CANCEL ADDR CITY! PLACE " +
          "| FYI? CH? MAP SRC? ( CODE CALL? ADDR! | CALL ADDR! ) CITY? ( UNIT | X/Z UNIT | X/Z X/Z UNIT | X+? ) ) INFO/N+? GPS1 GPS2 ID END");
  }

  @Override
  public String getFilter() {
    return "CAD@townofcary.org,cad.dispatching@townofcary.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CAUTION:")) {
      int pt = body.indexOf("______ ");
      if (pt < 0) return false;
      body = body.substring(pt+7).trim();
    }
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strCode.length() > 0) {
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("CH")) return new ChannelField("OPS_\\d+", true);
    if (name.equals("MAP")) return new MapField("[A-Z]{2,5}", true);
    if (name.equals("SRC1")) return new SourceField("[A-Z]{1,4}");
    if (name.equals("SRC2")) return new SourceField("S\\d{2}|[A-Z]{4}");
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+,[A-Z0-9,]+|[A-Z]+\\d+|[A-Z]+FD|MUT[A-Z0-9]+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ID")) return new IdField("\\d+", true);
    return super.getField(name);
  }

  private class MyCancelField extends BaseCancelField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (super.checkParse(field, data)) return true;
      if (!field.equals("WORKING FIRE")) return false;
      data.strCall = field;
      return true;
    }
  }


  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) (\\d{1,2}[A-Z]\\d{1,2})");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{4,}");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
    }

    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      field = field.replace(" ", "");
      if (!GPS_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();

  // City codes are only used for CANCEL messages :(
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANG",  "ANGIER",
      "APEX", "APEX",
      "ASHE", "ASHEBORO",
      "AUTR", "AUTRYVILLE",
      "BOON", "BOONVILLE",
      "BROD", "BROADWAY",
      "BUNN", "BUNN",
      "BURL", "BURLINGTON",
      "CARY", "CARY",
      "CHAP", "CHAPEL HILL",
      "CHAR", "CHARLOTTE",
      "CLAY", "CLAYTON",
      "CLIN", "CLINTON",
      "COAT", "COATS",
      "CUYA", "CUYAHOGA",
      "DUNN", "DUNN",
      "DURH", "DURHAM",
      "EDEN", "EDEN",
      "ELM",  "ELM CITY",
      "ENFI", "ENFIELD",
      "ERWI", "ERWIN",
      "FAY",  "FAYETTEVILLE",
      "FOUR", "FOUR OAKS",
      "FUQU", "FUQUAY VARINA",
      "GARN", "GARNER",
      "GRAH", "GRAHAM",
      "GRVL", "GREENVILLE",
      "HOLL", "HOLLY SPRINGS",
      "KILL", "KILL DEVIL HILLS",
      "KNIG", "KNIGHTDALE",
      "LILL", "LILLINGTON",
      "MARY", "MARYSVILLE",
      "MEBA", "MEBANE",
      "MIDD", "MIDDLE CREEK",
      "MORR", "MORRISVILLE",
      "NASH", "NASHVILLE",
      "NB",   "NEW BERN",
      "NEW",  "NEW HILL",
      "NEWT", "NEWTON GROVE",
      "PITT", "PITTSBORO",
      "QUCR", "QUEEN CREEK",
      "RALE", "RALEIGH",
      "RESE", "RESEACH TRIANGLE PARK",
      "ROCK", "ROCKY MOUNT",
      "ROLE", "ROLESVILLE",
      "ROUG", "ROUGEMONT",
      "SANF", "SANFORD",
      "SILE", "SILER CITY",
      "SMIT", "SMITHFIELD",
      "SPHO", "SPRING HOPE",
      "WAKE", "WAKE FORREST",
      "WEND", "WENDELL",
      "WHPL", "WHITE PLAINS",
      "WILL", "WILLOW SPRINGS",
      "WILS", "WILSON",
      "YOUN", "YOUNGSVILLE",
      "ZEBU", "ZEBULON"

  });
}