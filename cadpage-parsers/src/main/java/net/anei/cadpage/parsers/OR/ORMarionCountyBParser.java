
package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ORMarionCountyBParser extends DispatchH05Parser {

  public ORMarionCountyBParser() {
    this("MARION COUNTY", "OR");
  }

  protected ORMarionCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "SRC_CODE DATETIME ADDRCITY UNIT ID INFO_BLK/Z+? GPS1 GPS2 END");
  }

  @Override
  public String getAliasCode() {
    return "ORMarionCountyB";
  }

  @Override
  public String getFilter() {
    return "WVCChelpdesk@cityofsalem.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_CR;
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int jj = 0; jj < flds.length; jj++) {
      flds[jj] = stripFieldStart(stripFieldEnd(flds[jj], ";"), ";");
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_CODE")) return new MySourceCodeField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MySourceCodeField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strSource = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      data.strCode = field;
    }

    @Override
    public String getFieldNames() {
      return "SRC CODE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }
}
