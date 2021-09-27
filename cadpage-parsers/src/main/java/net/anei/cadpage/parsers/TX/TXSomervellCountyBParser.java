package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXSomervellCountyBParser extends FieldProgramParser {

  public TXSomervellCountyBParser() {
    super("SOMERVELL COUNTY", "TX",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYST! ID:ID! PRI:PRI! DATE:DATETIME! TIME:SKIP! MAP:MAP! UNIT:SRC! INFO:INFO!");
  }

  @Override
  public String getFilter() {
    return "zt@co.somervell.tx.us";
  }

  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?)(?:(?:\\b(?:APT|ROOM|LOT|CABIN|UNIT)|#) *(.*))|(\\d{1,4}|[A-Z])", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.isEmpty() && !data.strPlace.isEmpty()) {
      Matcher match = PLACE_APT_PTN.matcher(data.strPlace);
      if (match.matches()) {
        String place = match.group(1);
        if (place != null) {
          data.strPlace = place.trim();
          data.strApt = match.group(2);
        } else {
          data.strApt = data.strPlace;
          data.strPlace = "";
        }
      }
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern CALL_BACK_PTN = Pattern.compile("Call back: *(.*?), *");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_BACK_PTN.matcher(field);
      if (match.lookingAt()) {
        String phone = match.group(1).trim();
        if (!phone.equals("None")) data.strPhone = phone;
        field = field.substring(match.end());
      }

      Parser p = new Parser(field);
      String unit = p.getLastOptional(',');
      data.strUnit = UNIT_DELIM_PTN.matcher(unit).replaceAll(",");

      String trailInfo = p.getLastOptional(',');
      data.strSupp = INFO_DELIM_PTN.matcher(p.get()).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", trailInfo);
    }

    @Override
    public String getFieldNames() {
      return "PHONE INFO UNIT";
    }
  }
}
