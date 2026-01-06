package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Burke County, NC
 */
public class NCBurkeCountyParser extends DispatchOSSIParser {

  public NCBurkeCountyParser() {
    super(CITY_CODES, "BURKE COUNTY", "NC",
          "( CANCEL ADDR CITY? | FYI? SRC? ( CITY/Z GPS1! GPS2! | CITY | ) CALL EMPTY? ( ID CODE? | CODE ID? | ) ADDR! X? X? ) CODE? ID? INFO/N+");
    setupCityValues(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "CAD@bceoc.org,CAD@burke.local";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("|")) body = body.substring(1).trim();
    boolean good = body.startsWith("CAD:");
    if (!good) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.isEmpty()) return false;
    if (!data.strSource.isEmpty() ||
        !data.strCode.isEmpty() ||
        !data.strCity.isEmpty() ||
        !data.strCallId.isEmpty() ||
        !data.strCross.isEmpty()) return true;
    return isValidAddress(data.strAddress);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL|UNDER CONTROL", true);
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Z]\\d\\d[A-Za-z]?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern SOURCE_PTN = Pattern.compile("[A-Z0-9]{1,4}");
  private class MySourceField extends SourceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!SOURCE_PTN.matcher(field).matches()) return false;
      if (GPS_PTN.matcher(getRelativeField(+1)).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");

  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // NCCaldwellCounty alerts loook a lot like ours.  We identify them if the
      // field following the address is one of their city codes
      if (!field.isEmpty() && data.strSupp.isEmpty() &&
          NCCaldwellCountyParser.CITY_CODES.getProperty(field) != null) abort();
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "HICK", "HICKORY",
      "LONG", "LONG VIEW",
      "MORG", "MORGANTOWN",
      "NEBO", "NEBO",
      "NEWL", "JONAS RIDGE"  // ???
  });
}
