package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyAParser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile("(?:pagegate:)?\\*\\* DISP \\*\\* *(.*)");

  public WASnohomishCountyAParser() {
    super("SNOHOMISH COUNTY", "WA",
           "( UNIT CALL ADDR PLACE MAP! | CALL ADDR MAP CH MAP UNIT! ) INFO+");
  }

  @Override
  public String getFilter() {
    return "6245,pagegate@snopac911.us,noreply@snocom.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = match.group(1);
    return super.parseFields(body.split("!"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?)\\b *)+");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith(",")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    // Usually PK means PIKE, but not here
    addr = PK_PTN.matcher(addr).replaceAll("PKWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARL", "ARLINGTON",
      "BOT", "BOTHELL",
      "BRI", "BRIER",
      "D01", "LYNWOOD",
      "DAR", "DARRINGTON",
      "EDM", "EDMONDS",
      "EVE", "EVERETT",
      "G",   "GRANITE FALLS",
      "GBR", "GOLD BAR",
      "GRF", "GRANITE FALLS",
      "IDX", "INDEX",
      "LKS", "LAKE STEVENS",
      "LYN", "LYNNWOOD",
      "MAR", "MARYSVILLE",
      "MCK", "MILL CREEK",
      "MLT", "MOUNTLAKE TERRACE",
      "MON", "MONROE",
      "MUK", "MUKILTEO",
      "SNO", "SNOHOMISH",
      "STW", "STANWOOD",
      "SUL", "SULTAN",
      "WDV", "WOODENVILLE"
  });
}
