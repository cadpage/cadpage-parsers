package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;


/**
 * Calaveras County, CA
 */
public class CACalaverasCountyAParser extends FieldProgramParser {

  private static final Pattern MASTER =
      Pattern.compile("Inc# (\\d+):([^:]+):(?:([^@]+)@)?(.+?) *, *([A-Z_]+) *(?:\\(([^]]*?)\\) *)?:Map +([^:]*):(?: :)? LAT/LONG (X: [-+]?\\d+ \\d+\\.\\d+ +Y: [-+]?\\d+ \\d+\\.\\d+): ([^:]*)(?::([^:]*))?(?::.*)?");

  public CACalaverasCountyAParser() {
    this("CALAVERAS COUNTY");
  }

  protected CACalaverasCountyAParser(String defCity) {
    super(defCity, "CA",
          "ID! INCIDENT_NAME:PLACE! CODE_CALL! ADDRCITY! INFO/N+? GPS! http:SKIP! RESOURCES:UNIT! COMMAND:CH! TAC:CH/L! AIR:CH/L! GRD:CH/L! END");
  }

  @Override
  public String getFilter() {
    return "tcucad@FIRE.CA.GOV";
  }

  @Override
  public String getAliasCode() {
    return "CACalaverasCountyA";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID CALL PLACE ADDR APT CITY MAP GPS INFO UNIT");
      data.strCallId = match.group(1);
      data.strCall = match.group(2);
      data.strPlace = getOptGroup(match.group(3));
      parseAddress(match.group(4).trim(), data);
      data.strCity = match.group(5).trim();
      data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(6)));
      data.strMap = match.group(7).trim();
      setGPSLoc(match.group(8), data);
      data.strSupp = match.group(9).trim();
      data.strUnit = getOptGroup(match.group(10));
    } else {
      if (!parseFields(body.split("\n+"), data)) return false;
    }
    data.strCity = data.strCity.replace('_', ' ');
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INCIDENT #(\\d{6})", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("LAT: (.*?); LONG: *(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
