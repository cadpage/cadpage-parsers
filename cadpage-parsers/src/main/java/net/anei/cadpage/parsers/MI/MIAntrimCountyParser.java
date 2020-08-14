package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIAntrimCountyParser extends DispatchOSSIParser {

  public MIAntrimCountyParser() {
    this("ANTRIM COUNTY", "MI");
  }

  MIAntrimCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "FYI? CALL PRI? ADDR! ( CITY | X/Z CITY | X/Z X/Z CITY | X+? ) INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@antrimcounty.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[1-9P]");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private static final String DATE_TIME_STR = "NN/NN/NNNN NN:NN:NN";
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // See if this is a date time field
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        if (data.strTime.length() == 0) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
        }
        return;
      }

      // Or if it is a truncated field at end of message
      else if (getRelativeField(+1).length() == 0) {
        if (DATE_TIME_STR.startsWith(field.replaceAll("\\d", "N"))) return;
      }
      super.parse(field, data);
    }
  }


  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BANK",  "BANKS TWP",
      "CL",    "CENTRAL LAKE",
      "CLT",   "CENTRAL LAKE TWP",
      "CHT",   "CHESTONIA TWP",
      "CUST",  "CUSTER TWP",
      "ECHO",  "ECHO TWP",
      "ELLV",  "ELLSORTH",
      "ERT",   "ELK RAPIDS TWP",
      "FHT",   "FOREST HOME TWP",
      "JORD",  "JORDAN TWP",
      "KERN",  "KEARNEY TWP",
      "MAN",   "MANCELONA TWP",
      "MANV",  "MANCEOLNA",
      "MILT",  "MILTON TWP",
      "STAR",  "STAR TWP",
      "TLT",   "TORCH LAKE TWP",
      "VBL",   "BELLAIRE",
      "VER",   "ELK RAPIDS",
      "WARN",  "WARNER TWP"
  });
}
