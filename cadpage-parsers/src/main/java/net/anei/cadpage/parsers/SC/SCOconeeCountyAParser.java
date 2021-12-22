package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class SCOconeeCountyAParser extends FieldProgramParser {

  private static final Pattern ID_PTN = Pattern.compile(" *(\\d{4}-\\d{8})\\b");
  private static final Pattern DATE_TIME_MARK = Pattern.compile("  +(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b");
  private static final Pattern DATE_TIME_MARK2 = Pattern.compile("^(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b");
  private static final String DATE_TIME_MARK3 = "NN/NN/NN NN:NN";

  public SCOconeeCountyAParser() {
    super(CITY_LIST, "OCONEE COUNTY", "SC",
           "BASE! Narr:INFO E911_Info_-_Class_of_Service:SKIP Special_Response_Info:SKIP");
  }

  @Override
  public String getFilter() {
    return "911@oconeelaw.com,pagingsend@totalpaging.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("911 Message") && !subject.equals("911") &&
        !subject.equals("[Dispatch] 911 Message") &&
        !subject.endsWith("Alphanumeric Message Notification")) return false;

    // Strip off trailing disclaimer
    int pt = body.indexOf("\n___");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace("Narr:", " Narr:");
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BASE")) return new BaseField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class BaseField extends Field {

    @Override
    public void parse(String body, Data data) {

      // Look for an ID marker
      String sLeader = null;
      String sTrailer = null;
      do {
        Matcher match = ID_PTN.matcher(body);
        if (match.find()) {

          // Found it, get the ID and identify leading portion
          data.strCallId = match.group(1);
          sLeader = body.substring(0,match.start()).trim();
          sTrailer = body.substring(match.end()).trim();

          // This should be followed by a date and time
          match = DATE_TIME_MARK2.matcher(sTrailer);
          if (match.find()) {
            data.strDate = match.group(1);
            data.strTime = match.group(2);
            sTrailer = sTrailer.substring(match.end()).trim();
            break;
          }

          // If it isn't, see if it is a truncated Date/time
          String sCheck = sTrailer.replaceAll("\\d", "N");
          if (! DATE_TIME_MARK3.startsWith(sCheck)) abort();
          sTrailer = "";
          break;
        }

        match = DATE_TIME_MARK.matcher(body);
        if (match.find()) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
          sLeader = body.substring(0,match.start()).trim();
          sTrailer = body.substring(match.end()).trim();
          break;
        }

        sLeader = body;
        sTrailer = "";
      } while (false);

      sLeader = sLeader.replace(" > ", "  ");
      int pt = sLeader.indexOf("  ");
      if (pt < 0) abort();
      data.strCall = sLeader.substring(0,pt);
      String sAddr = sLeader.substring(pt+2).trim();

      sAddr = sAddr.replace(" XStreet:", " XS:");
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, sAddr, data);
      String left = getLeft();
      if (left.startsWith("XS:")) {
        String cross = left.substring(3).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, cross, data);
        cross = getLeft();
        while (isCommaLeft()) {
          String saveCross = data.strCross;
          data.strCross = "";
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, cross, data);
          data.strCross = append(saveCross, ", ", data.strCross);
          cross = getLeft();
        }
        if (cross.startsWith("/")) {
          data.strCross = append(data.strCross, " / ", cross.substring(1).trim());
        } else {
          data.strPlace = cross;
        }
      } else {
        Parser p  = new Parser(getLeft());
        data.strPlace = p.get(" XS:");
        data.strCross = append(data.strCross, " & ", p.get());
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR CITY X PLACE ID DATE TIME";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("  ");
      if (pt >= 0) field = field.substring(pt+2).trim();
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "SALEM",
    "SENECA",
    "WALHALLA",
    "WEST UNION",
    "WESTMINSTER",

    "FAIRPLAY",
    "FAIR PLAY",
    "LONG CREEK",
    "MOUNTAIN REST",
    "NEWRY",
    "OAKWAY",
    "RICHLAND",
    "TAMASSEE",
    "TOWNVILLE",
    "UTICA",

    // Laurens County??
    "CLINTON",

    // Greenville County
    "GREENVILLE"
  };
}
