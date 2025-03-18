package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXVictoriaCountyParser extends DispatchOSSIParser {

  public TXVictoriaCountyParser() {
    super(CITY_CODES, "VICTORIA COUNTY", "TX",
          "ADDR CITY DATETIME SRC CALL! INFO/N+? UNIT! GPS1 GPS2 END");
  }

  @Override
  public String getFilter() {
    return "CAD@victoriatx.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body,  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b[A-Z]{1,3}\\d{1,2}(?:MSZ)?\\b,?)+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d\\d\\.\\d+");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BL",   "BLOOMINGTON",
      "DA",   "VICTORIA",
      "INEZ", "INEZ",
      "MV",   "MEYERSVILLE",
      "NUR",  "NURSURY",
      "PL",   "PLACEDO",
      "QC",   "QUAIL CREEK",
      "VCFD", "VICTORIA",
      "VICT", "VICTORIA"
  });

}
