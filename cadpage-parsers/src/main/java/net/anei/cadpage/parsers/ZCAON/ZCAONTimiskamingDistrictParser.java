package net.anei.cadpage.parsers.ZCAON;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAONTimiskamingDistrictParser extends FieldProgramParser {

  public ZCAONTimiskamingDistrictParser() {
    super(CITY_LIST, "TIMISKAMING DISTRICT", "ON",
          "Location:ADDRCITYST! Type:CALL! Other_Info:INFO! INFO/N+ Call_Start_Time:DATETIME! END");
  }

  private static final Pattern EXTRA_PIPE_PTN = Pattern.compile("(?<=:)\\||~?\\|(?=\n|$)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = EXTRA_PIPE_PTN.matcher(body).replaceAll("");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern TRAIL_PR_PTN = Pattern.compile("(.*), *([A-Z]{2})");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TRAIL_PR_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      ZCAONTimiskamingDistrictParser.this.parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
    }
  }
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "TEMISKAMING SHORES",

      // Towns
      "COBALT",
      "ENGLEHART",
      "KIRKLAND LAKE",
      "LATCHFORD",
      "TOWNSHIPS",
      "ARMSTRONG",
      "BRETHOUR",
      "CASEY",
      "CHAMBERLAIN",
      "CHARLTON AND DACK",
      "COLEMAN",
      "EVANTUREL",
      "GAUTHIER",
      "HARLEY",
      "HARRIS",
      "HILLIARD",
      "HUDSON",
      "JAMES",
      "KERNS",
      "LARDER LAKE",
      "MATACHEWAN",
      "MCGARRY",

      // Village
      "THORNLOE"
  };

}
