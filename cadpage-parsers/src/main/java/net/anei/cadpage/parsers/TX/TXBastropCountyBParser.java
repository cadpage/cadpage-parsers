package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBastropCountyBParser extends FieldProgramParser {

  public TXBastropCountyBParser() {
    super("BASTROP COUNTY", "TX",
          "Location:ADDRCITYST! Coordinates:GPS? Call_Type:CALL! EMD_Code:CODE! Comments:INFO! INFO/N+ Responding_Units:UNIT! Incident_Number:ID! Created_Date/Time:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:Update to Incident|New Incident) - \\d+");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        data.strSource = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " SRC";
    }
  }
}
