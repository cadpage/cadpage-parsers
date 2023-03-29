package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAMontereyCountyBParser extends FieldProgramParser {

  public CAMontereyCountyBParser() {
    super("MONTEREY COUNTY", "CA",
          "( RES:UNIT! CLOSE:ID! CALL! ADDRCITY! INFO/R! INFO/N+ " +
          "| CALL ADDRCITY UNIT PLACE X MAP GPS! ID! Tac:CH! AIR/N GRD/N UNIT! INFO/N )");
  }

  @Override
  public String getFilter() {
    return "8319983288,beuecc@fire.ca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_SUPPR_SR;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(" <a ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("; "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Inc# +(\\d{6})", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new GPSField("X:.* Y:.*", true);
    if (name.equals("AIR")) return new InfoField("Air:.*", true);
    if (name.equals("GRD")) return new InfoField("Grd:.*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{1,2})-(\\d{1,2})-(\\d{1,2}:\\d{2})");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1)+'/'+match.group(2);
      data.strTime = match.group(3);
    }

  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0, pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
      data.strCity = data.strCity.replace('_', ' ');
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO TEXT")) return;
      super.parse(field, data);
    }
  }
}
