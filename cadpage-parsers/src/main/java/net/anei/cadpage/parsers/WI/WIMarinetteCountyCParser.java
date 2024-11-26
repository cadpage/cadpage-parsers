package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIMarinetteCountyCParser extends FieldProgramParser {

  public WIMarinetteCountyCParser() {
    super("MARINETTE COUNTY", "WI", "CALL ADDRCITY! Cross_Street:X? GPS! END");
  }

  @Override
  public String getFilter() {
    return "rapidnotification@marinettecounty.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile(" +\\| +");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("RapidNotification Message - DO NOT REPLY")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{2}\\.\\d{5,},[-+]?\\d{2}\\.\\d{5,}", true);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|#)[# ]*(.*)");

  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      parseAddress(p.get(';'), data);
      while (true) {
        String fld = p.get(';');
        if (fld.isEmpty()) break;
        Matcher match = APT_PTN.matcher(fld);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
        } else {
          data.strPlace = append(data.strPlace, " - ", fld);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Intersection of:");
      super.parse(field, data);
    }
  }
}
