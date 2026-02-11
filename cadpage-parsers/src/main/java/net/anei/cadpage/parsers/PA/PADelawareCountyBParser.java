package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


public class PADelawareCountyBParser extends FieldProgramParser {

  private AddressField addressField;

  private boolean crossAddress;

  private static final Properties CITY_CODES = PADelawareCountyParser.CITY_CODES;

  public PADelawareCountyBParser() {
    super(CITY_CODES, "DELAWARE COUNTY", "PA",
          "( SELECT/1 ADDR CITY/Z? CALL DATE TIME ID INFO/N+? ALERT! END " +
          "| SELECT/3 ( FIRE_STATION_DISPATCH_REPORT%EMPTY! Unit:UNIT! Dispatched:DATETIME3/s! Event:ID! Agency:SKIP! Event_Type:CALL3! Sub_Type:CALL/SDS! Location:ADDR3! Cross_Streets:X! Call_Source:SKIP! Caller_Name:NAME! INFO/N+ " +
                     "| Unit_Clear_Report%EMPTY! Unit:UNIT! Time_Reported:DATETIME3/s! Event:ID! Location:ADDR3! Agency:SKIP! Event_Type:CALL3! Sub_Type:CALL/SDS! Cross_Street:X! Caller_Source:SKIP! Caller:NAME! INFO/N+ ) " +
          "| ADDR2/S X1:XADDR? X2:X? Nature:CALL2! EID:SKIP? CALLER_NAME:NAME? CALLER_ADDR:SKIP? Time:TIME2 Notes:INFO? EID:SKIP? Inc:ID2? Beat:BOX? LL:GPS? Disp:UNIT )");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  @Override
  public String getFilter() {
    return "@delcotextrelay.com,caddbserver@delco911alerts.com,@c-msg.net";
  }

  private static final Pattern DELIM2 = Pattern.compile("\n|\\s+(?=Dispatched:|Time Reported:)");
  private static final Pattern PREFIX_PTN = Pattern.compile("^\\d{7} ");
  private static final Pattern LEAD_TIME_DATE_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d +");
  private static final Pattern NATURE_PTN = Pattern.compile("[\\* \"<](?:N.tu.e:|..ture:|Natu....|Nat...:|Na..re:(?=[A-Z]))");
  private static final Pattern TIME_PTN = Pattern.compile(".Time:", Pattern.CASE_INSENSITIVE);
  private static final Pattern NOTES_PTN = Pattern.compile(" Not..[\\*:]");
  private static final Pattern INC_PTN = Pattern.compile(" I..:(?! @)");
  private static final Pattern XN_PTN = Pattern.compile("[ ><](?!X1 INSIDE)X([12])..");
  private static final Pattern X1_PTN = Pattern.compile("..1:");
  private static final Pattern X2_PTN = Pattern.compile("..2:");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Nature:|X1:|X2:|Time:|Notes:|EID:|Inc:|Beat:|Disp:)");
  private static final Pattern MASTER_STAR_PTN = Pattern.compile("(.*?) \\*{2,}(.*)\\*{2,}");
  private static final Pattern MASTER_DASH_PTN = Pattern.compile("(.*?)[\\\\:]*-+(.*)");
  private static final Pattern PLACE_CALL_PTN = Pattern.compile("@ *([^ ].*?) ([-_/A-Z]+\\b.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Block version G calls here
    if (body.startsWith("ADDR:") || body.startsWith("DATE:")) return false;

    if (subject.equals("New Alert")) {
      setSelectValue("1");
      return parseFields(body.split("\n"), data);
    }

    if (body.startsWith("FIRE STATION DISPATCH REPORT \n") || body.startsWith("Unit Clear Report\n")) {
      setSelectValue("3");
      return parseFields(DELIM2.split(body), data);
    }

    setSelectValue("2");
    if (subject.startsWith("ALERT! ")) {
      data.strSource = subject.substring(7).trim().replace(' ', '_');
    }

    boolean confirmed = false;
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.find()) {
      confirmed = true;
      String prefix = match.group();
      body = body.substring(match.end()).trim();

      // Long alerts get broken up into two messages, with the unit prefix
      // on both of them.  After the merge logic does it's thing, we still
      // have to clean up the second unit field
      String search = ' ' + prefix;
      match = LEAD_TIME_DATE_PTN.matcher(body);
      if (match.lookingAt()) body = body.substring(match.end());
      while (true) {
        int pt = body.indexOf(search);
        if (pt < 0) break;
        String part1 = body.substring(0,pt);
        String part2 = body.substring(pt+search.length());
        part1 = stripFieldEnd(part1, " -");
        match = LEAD_TIME_DATE_PTN.matcher(part2);
        if (match.lookingAt()) part2 = part2.substring(match.end());
        body = append(part1, " ", part2);
      }
    }
    body = stripFieldEnd(body, " -");

    // For some reason the Nature: keyword is prone to being garbled
    body = NATURE_PTN.matcher(body).replaceFirst(" Nature: ");

    // Other field gets hit slightly less often
    body = TIME_PTN.matcher(body).replaceFirst(" Time:");
    body = NOTES_PTN.matcher(body).replaceFirst(" Notes:");
    body = INC_PTN.matcher(body).replaceFirst(" Inc:");

    // Then again, garbledness seems to occur anywhere, we just find it
    // in the Nature field because that causes the parser to fail.  But lets
    // fix up the Xn: fields if we can
    body = XN_PTN.matcher(body).replaceAll(" X$1: ");

    body = body.replace('\n', ' ');
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");

    int px1 = body.indexOf(" X1:");
    int px2 = body.indexOf(" X2:");
    int pNature = body.indexOf(" Nature:");
    if (pNature >= 0) {
      if (px1 < 0 && px2 >= 0) {
        body = X1_PTN.matcher(body.substring(0,px2)).replaceFirst(" X1:") +
               body.substring(px2);
      } else if (px1 >= 0 && px2 < 0 && px1 < pNature) {
        body = body.substring(0,px1) +
               X2_PTN.matcher(body.substring(px1, pNature)).replaceFirst(" X2:") +
               body.substring(pNature);
      }

      body = stripFieldStart(body, "Location:");
      body = stripFieldStart(body, "LOC:");

      crossAddress = false;
      if (!super.parseMsg(body, data)) return false;

      // If we did not retrieve a unit value, expect more to follow
      if (data.strUnit.length() == 0 || data.strUnit.equals("?")) data.expectMore = true;
      return true;
    }

    // No go so far.  If we are positive this is a dispatch
    // message, try some simple basic combinations
    if (!confirmed) return false;

    setFieldList("ADDR APT CITY ST PLACE CALL");
    String left;
    match = MASTER_STAR_PTN.matcher(body);
    if (match.matches()) {
      parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, match.group(1).trim(), data);
      left = match.group(2).trim();
      if (!isValidAddress()) return false;
    }

    else if ((match = MASTER_DASH_PTN.matcher(body)).matches()) {
      parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, match.group(1).trim(), data);
      if (!isValidAddress()) return false;
      left = match.group(2).trim();
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, left, data);
        left = getLeft();
      }
    }

    else {
      parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS, body, data);
      left = getLeft();
      if (!isValidAddress()) return false;
    }

    if (data.strCity.length() == 0) {
      int pt = left.indexOf('@');
      if (pt >= 0) {
        String city = left.substring(0,pt).trim();
        city = CITY_CODES.getProperty(city);
        if (city != null) {
          data.strCity = city;
          left = left.substring(pt);
        }
      }
    }

    match = PLACE_CALL_PTN.matcher(left);
    if (match.matches()) {
      data.strPlace = match.group(1).trim();
      left = match.group(2).trim();
    }

    data.strCall = left;
    fixCity(data);
    return true;
  }

  private void fixCity(Data data) {
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      data.strCity = data.strCity.substring(0,pt);
    }
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("ID")) return new IdField("F\\d+");
    if (name.equals("ALERT")) return new SkipField(".*@alert.active911.com", true);
    if (addressField == null) addressField = new MyAddressField();
    if (name.equals("ADDR2")) return addressField;
    if (name.equals("XADDR")) return new CrossAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("TIME2")) return new MyTime2Field();
    if (name.equals("ID2")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CALL3")) return new MyCall3Field();
    if (name.equals("DATETIME3")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("ADDR3")) return new MyAddress3Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_DASH_CITY_PTN = Pattern.compile("(.*)[-<]([A-Z]{2})");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "<");
      Parser p = new Parser(field);
      while (true) {
        String place = p.getLastOptional(':');
        if (place.length() == 0) break;
        place = stripFieldStart(place, "@");
        if (!place.endsWith(" CO") && !place.endsWith(" CO.") && !place.endsWith(" MD")) {
          String city = CITY_CODES.getProperty(place);
          Result res = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, place);
          city = res.getCity();
          if (city.length() > 0) {
            res.getData(data);
            place = res.getStart();
          }
        }
        if (!data.strPlace.contains(place)) data.strPlace = append(place, " - ", data.strPlace);
      }
      String apt = p.getLastOptional(',');
      field = p.get();
      if (field.startsWith("@")) {
        String place = field.substring(1).trim();
        if (!data.strPlace.contains(place)) data.strPlace = append(place, " - ", data.strPlace);
      } else if (field.startsWith("/")) {
        data.strAddress = "";
        data.strCity = convertCodes(field.substring(1).trim(), CITY_CODES);
      } else {
        Matcher match = ADDR_DASH_CITY_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1) + " " + match.group(2);
        }
        super.parse(field, data);
      }
      data.strAddress = stripFieldEnd(data.strAddress, "--");
      data.strApt = append(data.strApt, "-", apt);
      fixCity(data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT PLACE";
    }
  }

  private class CrossAddressField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() == 0) {
        crossAddress = true;
        addressField.parse(field, data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return addressField.getFieldNames() + " X";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (crossAddress) {
        String saveAddr = data.strAddress;
        data.strAddress = "";
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
        data.strAddress = append(saveAddr, " & ", data.strAddress);
        fixCity(data);
      } else {
        super.parse(field, data);
      }
    }
  }

  private static final Pattern TRASH_TIME_PTN = Pattern.compile("(.*).......(\\d\\d:\\d\\d:\\d\\d)");
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      // Look for time field with a Garbled Time: keyword at end of data
      Matcher match = TRASH_TIME_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strTime = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL TIME";
    }
  }

  private class MyCall3Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }


  private class MyTime2Field extends TimeField {
    @Override
    public void parse(String field, Data data) {
      // This is all an attempt to unscramble following keywords
      // garbled by transmission errors
      super.parse(substring(field,0,8), data);

      if (field.length() <= 10) return;
      String left = field.substring(10);
      int nCnt = matchCount("Notes: ", left);
      int iCnt = matchCount("Inc: ", left);
      if (iCnt > nCnt) {
        left = left.substring(5).trim();
        new MyIdField().parse(left, data);
        return;
      }
      if (nCnt > 0) left = substring(left, 7);
      data.strSupp = left;
    }

    private int matchCount(String key, String field) {
      int count = 0;
      for (int ii = 0; ii<key.length(); ii++) {
        char fldChr = (ii < field.length() ? field.charAt(ii) : ' ');
        if (key.charAt(ii) == fldChr) count++;
      }
      return count;
    }

    @Override
    public String getFieldNames() {
      return "TIME INFO ID BOX";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Beat ");
      if (pt >= 0) {
        data.strBox = field.substring(pt+6).trim();
        field = field.substring(0,pt).trim();
      } else {
        field = stripFieldEnd(field, " Beat");
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID BOX";
    }
  }

  private static final Pattern UNIT_BLANK_PTN = Pattern.compile(" {2,}");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BLANK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private class MyAddress3Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" Apt# ", " Apt ");
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
    }
  }
}
