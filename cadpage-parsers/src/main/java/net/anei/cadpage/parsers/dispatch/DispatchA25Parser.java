package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchA25Parser extends FieldProgramParser {

  private boolean checkCity;

  public DispatchA25Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA25Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CALL! CALL2+? ( SELECT/1 Address:ADDR! Reporting_Person:NAME Phone:PHONE% Detail:INFO% " +
                        "| ADDR! MEMO:INFO ) INFO/N+");
    checkCity = (cityList != null);
  }

  private static final Pattern RUN_REPORT_ID_PTN = Pattern.compile(" INC #(\\d+-\\d+) ");
  private static final Pattern RUN_REPORT_ID_PTN2 = Pattern.compile("^Inc # (\\d+-\\d+)\\b");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("^OCC #\\d\\d-\\d+, INC #(\\d\\d-\\d+)");
  private static final Pattern MARKER1 = Pattern.compile("NEWOCC #OUTS  +|ALERT - OCC #OUTS +|(?:\\*+ (UPDATE) \\*+\n|ALERT - )?(?:NEW)?(?:INC|OCC) #([-0-9\\?]+) +");
  private static final Pattern MARKER2 = Pattern.compile("MEMO OCC #OUTS *- *");
  private static final Pattern MARKER3 = Pattern.compile("MEMO INC #([-0-9]+) - *");
  private static final Pattern MISSING_DELIM = Pattern.compile(",? (?=Phone:)");
  private static final Pattern ALTERNATE_PTN = Pattern.compile("NEW (?:(\\d\\d?-\\d\\d?-[A-Z]{1,2}) )?(.*?)(?:[-,] ([ 'A-Za-z]+))?(?:, *([A-Z]{2}))?");
  private static final Pattern PLACE_ADDR_PREFIX_PTN = Pattern.compile("([NSEW]B)|(.*)(?:&| and)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("HISTORY FOR ")) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      Matcher match = RUN_REPORT_ID_PTN.matcher(subject);
      if (match.find()) data.strCallId = match.group(1);
      data.strSupp = body;
      return true;
    }

    if (subject.startsWith("NEW 911 CALL HISTORY FOR UNIT #")) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = subject.substring(32).trim();
      Matcher match = RUN_REPORT_ID_PTN2.matcher(body);
      if (match.find()) data.strCallId = match.group(1);
      data.strSupp = body;
      return true;

    }

    Matcher match = RUN_REPORT_PTN2.matcher(body);
    if (match.find()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = body;
      return true;
    }

    match = MARKER1.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      String update = getOptGroup(match.group(1));
      data.strCallId = getOptGroup(match.group(2));
      body = body.substring(match.end());
      body = MISSING_DELIM.matcher(body).replaceFirst("\n");
      if (!super.parseFields(body.split("\n"), data)) return false;
      data.strCall = append(update, " - ", data.strCall);
      return true;
    }

    match = MARKER2.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("2");
      body = body.substring(match.end());
      return super.parseFields(body.split("\n"), data);
    }

    match = MARKER3.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("2");
      data.strCallId = match.group(1);
      body = body.substring(match.end());
      return super.parseFields(body.split("\n"), data);
    }

    match = ALTERNATE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL PLACE ADDR APT CITY ST");
      data.strCode = getOptGroup(match.group(1));
      String addr = match.group(2).trim();
      data.strCity = getOptGroup(match.group(3));
      data.strState = getOptGroup(match.group(4));

      String place = "";
      int pt = addr.lastIndexOf('@');
      if (pt >= 0) {
        place = addr.substring(pt+1).trim();
        addr = addr.substring(0,pt);
      }

      String apt = "";
      if (addr.endsWith(")")) {
        pt = addr.lastIndexOf('(');
        if (pt >= 0) {
          apt = addr.substring(pt+1, addr.length()-1).trim();
          addr = addr.substring(0,pt).trim();
          match = APT_PREFIX_PTN.matcher(apt);
          if (match.matches()) apt = match.group(1);
        }
      }

      do {
        CodeSet callList = getCallList();
        if (callList != null) {
          String call = callList.getCode(addr.toUpperCase(), true);
          if (call != null) {
            data.strCall = addr.substring(0, call.length());
            addr = addr.substring(call.length());
            pt = addr.indexOf(" - ");
            if (pt >= 0) {
              data.strPlace = addr.substring(0,pt).trim();
              parseAddress(addr.substring(pt+3).trim(), data);
            } else {
              parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, addr, data);
            }
            break;
          }
        }

        pt = addr.indexOf(" - ");
        if (pt >= 0) {
          data.strCall = addr.substring(0,pt).trim();
          parseAddress(addr.substring(pt+3).trim(), data);
          break;
        }

        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
      } while (false);

      if (data.strAddress.length() == 0) {
        if (data.strPlace.length() == 0) return false;
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }

      data.strApt = append(data.strApt, "-", apt);

      // See if place looks like it should really be prefixed to the address
      match = PLACE_ADDR_PREFIX_PTN.matcher(data.strPlace);
      if (match.matches()) {
        String tmp = match.group(1);
        String sep = " ";
        if (tmp == null) {
          tmp = match.group(2).trim();
          sep = " & ";
        }
        data.strAddress = append(tmp, sep, data.strAddress);
        data.strPlace = "";
      }

      // Append previously identified place name
      data.strPlace = append(data.strPlace, " - ", place);
      return true;
    }
    return false;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_PTN = Pattern.compile("([-/& A-Z0-9.]+?) - (.*)", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Address:")) return false;
      if (!data.strCall.endsWith("-")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " ", field);
    }
  }

  private static final Pattern CITY_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: +(\\d{5}))?");
  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:APT(?: ROOM)?|LOT|RM|ROOM|STE)[ :]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = CITY_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field =  match.group(1);
        data.strState =  match.group(2);
        zip = match.group(3);
      } else {
        field = stripFieldEnd(field, ",");
      }

      String addr;
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        String part1 = field.substring(0,pt).trim();
        String part2 = field.substring(pt+3).trim();
        pt = part2.lastIndexOf(',');
        if (pt >= 0) {
          data.strPlace = part1;
          data.strCity = part2.substring(pt+1).trim();
          addr = part2.substring(0,pt).trim();
        }
        else if (checkCity && !isCity(part2)) {
          data.strPlace = part1;
          addr = part2;
        } else {
          addr = part1;
          data.strCity = part2;
        }
      } else {
        pt = field.lastIndexOf(',');
        if (pt >= 0) {
          addr = field.substring(0,pt).trim();
          data.strCity = field.substring(pt+1).trim();
        } else {
          addr = field;
        }
      }

      String apt = "";
      if (addr.endsWith(")")) {
        pt = addr.lastIndexOf('(');
        if (pt >= 0) {
          apt = addr.substring(pt+1, addr.length()-1).trim();
          addr = addr.substring(0,pt).trim();
          match = APT_PREFIX_PTN.matcher(apt);
          if (match.matches()) apt = match.group(1);
        }
      }
      if (data.strCity.isEmpty() && checkCity) {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      } else {
        parseAddress(addr, data);
      }
      if (data.strCity.isEmpty() && zip != null) data.strCity = zip;
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY ST";
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0 && ", Phone:".startsWith(field.substring(pt))) {
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("(?:RP )?Lat/Long: *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        if (data.strGPSLoc.length() == 0) {
          setGPSLoc(match.group(1), data);
        }
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
}
