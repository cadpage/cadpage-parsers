package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class TNMorganCountyAParser extends FieldProgramParser {

  public TNMorganCountyAParser() {
    super("MORGAN COUNTY", "TN",
          "CALL! ADDRCITY INFO");
  }

  @Override
  public String getFilter() {
    return "dispatch@911email.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911 Incident Auto-Page")) return false;
    return parseFields(body.split("\n"), data);
  };

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d{1,4}[A-Z]?|[A-Z]|LOT .*");
  private static final Pattern CITY_PTN = Pattern.compile("[ A-Z]+");

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int ptl = field.length();
      int pts;
      while (ptl > 0 && (pts = field.lastIndexOf(',', ptl-1)) >= 0) {
        String part = field.substring(pts+1,ptl).trim();
        ptl = pts;
        if (part.isEmpty()) continue;

        if (APT_PTN.matcher(part).matches()) {
          apt = append(part, "-", apt);
        } else if (!data.strCity.isEmpty() || !CITY_PTN.matcher(part).matches()) {
          data.strPlace = append(part, " - ", data.strPlace);
        } else {
          data.strCity = part;
        }
      }
      parseAddress(field.substring(0,ptl).trim(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    int pt = addr.indexOf(" - ");
    if (pt >= 0) addr = addr.substring(0,pt).trim();
    return super.adjustMapAddress(addr);
  }
}
