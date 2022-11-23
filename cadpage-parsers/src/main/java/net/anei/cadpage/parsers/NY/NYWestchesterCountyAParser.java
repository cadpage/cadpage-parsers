package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class NYWestchesterCountyAParser extends FieldProgramParser {

  public NYWestchesterCountyAParser() {
    super("WESTCHESTER COUNTY", "NY",
          "ADDR Cross:X! Type:CALL! CALL Time_out:TIME Area:MAP lev:PRI Comments:INFO% INFO+? Event_Number:ID END");
  }

  @Override
  public String getFilter() {
    return "IPAGE@westchestergov.com,messaging@iamresponding.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Check for IPage signature
    do {
      if (subject.equals("IPage")) break;
      if (subject.equals("Email Copy Message From Hiplink")) break;
      if (body.startsWith("IPage / ")) {
        body = body.substring(8).trim();
        break;
      }
      if (body.startsWith("- ")) {
        body = body.substring(2).trim();
        break;
      }
      if (isPositiveId()) break;
      return false;
    } while (false);

    body = body.replace(" Area:", ",Area:");
    if (!parseFields(body.split(","), data)) return false;
    int pt = data.strCity.indexOf(',');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1).trim();
      data.strCity = data.strCity.substring(0,pt).trim();
    }
    if (data.strCity.equals("BANKSVILLE")) {
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = "BEDFORD";
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_DELIM_PTN = Pattern.compile("(.*): *(?:@|(APT|CONDO|ROOM|RM|UNIT)|)(.*)");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?\\d?|[A-Z]\\d*");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      String apt = "";
      while (true) {
        Matcher match = ADDR_DELIM_PTN.matcher(field);
        if (!match.matches()) break;
        field = match.group(1).trim();
        String term = match.group(3).trim();
        if (match.group(2) != null || ADDR_APT_PTN.matcher(term).matches()) {
          apt = append(term, "-", apt);
        } else {
          data.strPlace = append(term, " - ", data.strPlace);
        }
      }
      Parser p = new Parser(field);
      data.strCity = p.getLast(' ');
      parseAddress(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      data.strCity = stripFieldEnd(data.strCity, "_T");
      data.strCity = data.strCity.replace('_', ' ');
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST PLACE APT";
    }
  }

  private static final Pattern GPS_PTN1 = Pattern.compile("WPH\\d +([-+]?\\d{3}\\.\\d{6,}[, ][-+]?\\d{3}\\.\\d{6,})");
  private static final Pattern GPS_PTN2 = Pattern.compile("http://maps.google.com/\\?q=([-+]?\\d{2,3}\\.\\d{6,})");
  private static final Pattern GPS_PTN3 = Pattern.compile("([-+]?\\d{2,3}\\.\\d{6,})");
  private class MyInfoField extends InfoField {

    private String gps1 = null;

    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN1.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        return;
      }

      match = GPS_PTN2.matcher(field);
      if (match.matches()) {
        gps1 = match.group(1);
        return;
      }
      if (gps1 != null) {
        match = GPS_PTN3.matcher(field);
        if (data.strGPSLoc.length() == 0 && match.matches()) {
          setGPSLoc(gps1+',' +  match.group(1), data);
        }
        gps1 = null;
        return;
      }

      data.strSupp = append(data.strSupp, ", ", field);
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
}
