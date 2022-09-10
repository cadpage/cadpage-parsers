package net.anei.cadpage.parsers.CA;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


/**
 * San Joaquin County, CA
 */
public class CASanJoaquinCountyAParser extends FieldProgramParser {

  public CASanJoaquinCountyAParser() {
    this("SAN JOAQUIN COUNTY", "CA");
  }

  CASanJoaquinCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/OLD CALL! Location:PLACE! Address:ADDR! City:CITY1! GPS:GPS? Bldg:PLACE% Apt:APT% CrossStreets:X1% Plan:PLAN% Initial_Assignment:UNIT% " +
          "| UNIT:UNIT! ( SELECT/POST_ASSIGNMENT_PAGE ASS_ADDR! ( ADDR/Z CITY GPS:GPS! | CITY? GPS:GPS! ) INFO/L+ " +
                       "| CAD#:ID! RESP#:ID/L? " +
                           "( SELECT/RR INFO! INFO/N+ " +
                           "| CODE_CALL CALL/L+? PRI2! PRI/L+ Loc:PLACE! ADDR! ADDR City:CITY! X2! X2 Bldg:APT! Apt:APT! " +
                               "( Assigned_Units:UNIT! Resp_Plan:PLAN! GPS:GPS! CH CH MAP MAP/L! " +
                               "| GPS:GPS! ( Assigned_Units:UNIT! CH CH MAP MAP/L! Inc_Type:SKIP! Resp_Plan:PLAN! | ) " +
                               ") Incident_Comments:INFO2! " +
                           ") " +
                       ") " +
          ") END");
  }

  @Override
  public String getAliasCode() {
    return "CASanJoaquinCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public boolean noParseSubjectFollow() { return true; }
      @Override public int splitBreakLength() { return 120; }
      @Override public int splitBreakPad() { return 3; }
    };
  }

  @Override
  public String getFilter() {
    return "LifecomCellPaging@amr.net,VRECC.CAD@donotreply.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = MAP_ADDR_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1);
    return super.adjustMapAddress(addr);
  }
  private static final Pattern MAP_ADDR_PTN = Pattern.compile("[1-3] - +(.*)");

  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("(?:([-A-Z0-9]+) +)?(?:(\\d{8}|[A-Z]{2,5}-\\d{6}) +)?(Dispatched:.*?Enroute:.*?On Scene:.*?(?:AOR|Clear Call):.*)");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("RUN REPORT CAD ?#(\\d{8}|[A-Z]{2,5}-\\d{6}) +([-\\(\\)A-Z0-9]{1,10}) *((?:Dispatched:.*Enroute:.*On Scene:.*AOR:|Disp:.*Resp:.*On Scene:.*TX:.*Dest:.*AOR:).*)");
  private static final Pattern RUN_REPORT_PTN3 = Pattern.compile("([-A-Z0-9]+) +CAD ID #: +Run ?\\(PCR#\\):(\\d+)\\*RUN REPORT\\*(.*?)STmiles/[\\d\\.]* *Emiles/[\\d\\.]* *Grid/.*? *Map/(\\S*) *Area/.*");
  private static final Pattern RUN_REPORT_PTN4 = Pattern.compile("(Late Scene Arrival)(\\d{8}) *(\\S+) (\\d\\d:\\d\\d:\\d\\d)(\\d\\d:\\d\\d:\\d\\d)?");
  private static final Pattern RR_ID1_PTN = Pattern.compile("[A-Z]{2,3}-\\d{6}");
  private static final Pattern RR_UNIT_PTN = Pattern.compile("[A-Z0-9]+");
  private static final Pattern RR_TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d|");
  private static final Pattern RR_ID2_PTN = Pattern.compile("\\d+");

  private static final Pattern ASSIGNED_POST_AT_PTN = Pattern.compile(", ?You have been assigned to post at:");
  private static final Pattern SECURITY_CHECK_20_MINUTES_PTN = Pattern.compile("\\*+SECURITY CHECK - 20 MINUTES\\*+");
  private static final Pattern RUN_PCR_PTN = Pattern.compile(" *(?:RUN|Run) ?\\(PCR#\\):");
  private static final Pattern CAD_ID_PTN = Pattern.compile("CAD ID ?#:");

  private static final Pattern MASTER1 = Pattern.compile("(?:((?![A-Z]{3}-\\d+)[A-Z][-\\(\\)A-Z0-9]*+) ++)?+((?:[A-Z]{3}-\\d{6}|\\d{8})(?=  )|\\d{8}-\\d{6}) *(.*?)  +(.*?)(?:  +(.*?))?Bldg: *(.*?)Apt: *(.*?)(?:Assigned Units: *(.*))?");
  private static final Pattern MASTER2 = Pattern.compile("(?:([A-Z][-A-Z0-9]*+) ++)?+(\\d{8}) {5}([^ ].*)");
  private static final Pattern UNIT_GPS_CH_PTN = Pattern.compile("(.*?)GPS:(\\d{2})(\\d{6}) +&(\\d{3})(\\d{6})\\b *(.*)");
  private static final Pattern CHANNEL_PTN = Pattern.compile("(.*?) *((?:CMD |XSJ ).*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // First see if this looks like a run report
    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = getOptGroup(match.group(1));
      data.strCallId = getOptGroup(match.group(2));
      data.strSupp = cleanRunReportText(match.group(3));
      return true;
    }

    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = cleanRunReportText(match.group(3));
      return true;
    }

    match = RUN_REPORT_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO MAP");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strSupp = cleanRunReportText(match.group(3));
      data.strMap = match.group(4);
      return true;
    }

    match = RUN_REPORT_PTN4.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = match.group(1);
      data.strCallId = match.group(2);
      data.strUnit = match.group(3);
      String time1 = match.group(4);
      String time2 = match.group(5);
      if (time2 != null) {
        data.strSupp = "Dispatched: " + time1 + "\nArrived: " + time2;
      } else {
        data.strSupp = "Arrived: " + time1;
      }
      return true;
    }

    // Try the slash delimited format first
    if (body.length() >= 26 && body.substring(20,26).equals("/UNIT:")) {
      String type = body.substring(0,20).replace(' ', '_');
      body = body.substring(21);
      body = body.replace("Assigned_Units:", "Assigned Units:").replace("Inc_Type:", "Inc Type:").replace("Resp_Plan:", "Resp Plan:").replace("Incident_Comments:", "Incident Comments:");
      setSelectValue(type);
      return parseFields(splitSlashDelimFields(body), data);
    }

    do {
      FParser p = new  FParser(body);
      String id1 = p.get(19);
      if (!RR_ID1_PTN.matcher(id1).matches()) break;
      if (!p.check(" ") || p.check(" ")) break;
      String unit = p.get(8);
      if (!RR_UNIT_PTN.matcher(unit).matches()) break;
      String time = p.get(8);
      if (!RR_TIME_PTN.matcher(time).matches()) break;
      String id2 = p.get();
      if (!RR_ID2_PTN.matcher(id2).matches()) break;

      setFieldList("ID UNIT TIME");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = id1;
      data.strUnit = unit;
      data.strTime = time;
      return true;

    } while (false);

    do {
      FParser p = new FParser(body);
      p.check("UNIT:");
      String unit = p.get(9);
      if (!p.checkBlanks(1) || p.checkBlanks(1)) break;
      p.check("CAD#:");
      if (p.checkAhead(15,  "Dispatched:")) {
        String callId = p.get(15);
        setFieldList("UNIT ID INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strUnit = unit;
        data.strCallId = callId;
        data.strSupp = cleanRunReportText(p.get());
        return true;
      }
      String callId = p.getOptional("Loc:", 10, 15);
      boolean variant1 = callId != null;
      String call;
      if (variant1) {
        call = "ALERT";
      } else {
        callId = p.get(15);
        call = p.get(30);
        if (!p.check("Loc:")) break;
      }
      String place = p.get(50);
      String addr = p.get(50);

      if (variant1) {
        String cross = p.get(20);
        if (!p.check("Bldg:")) break;
        String apt1 = p.get(10);
        if (!p.check("Apt:")) break;
        String apt2 = p.get(10);
        if (!p.check("City:")) break;
        String city = p.get();

        setFieldList("UNIT ID CODE CALL PLACE ADDR X APT CITY");
        data.strUnit = unit;
        data.strCallId = callId;
        parseCodeCall(call, data);
        data.strPlace = place;
        parseAddress(addr, data);
        data.strCity = city;
        data.strCross = cleanCrossField(cross);
        data.strApt = append(apt1, "-", data.strApt);
        data.strApt = append(data.strApt, "-", apt2);
        return true;

      }

      if (!p.check("City:")) break;
      String city = p.get(15);
      String cross = p.get(20);
      if (!p.check("Bldg:")) break;
      String apt1 = p.get(10);
      if (!p.check("Apt:")) break;
      String apt2 = p.get(10);
      p.setOptional();
      if (!p.check("Assigned Units:")) break;
      if (p.check("Resp Plan:")) {
        String respPlan = p.get(50);
        if (respPlan.length() > 0) respPlan = "Resp Plan:" + respPlan;
        if (!p.check("All Units Assigned:")) break;
        String left = p.get();

        setFieldList("UNIT ID CODE CALL PLACE ADDR CITY X APT INFO GPS CH");
        data.strUnit = unit;
        data.strCallId = callId;
        parseCodeCall(call, data);
        data.strPlace = place;
        parseAddress(addr, data);
        data.strCity = city;
        data.strCross = cleanCrossField(cross);
        data.strApt = append(apt1, "-", data.strApt);
        data.strApt = append(data.strApt, "-", apt2);
        data.strSupp = respPlan;

        String channel;
        if ((match = UNIT_GPS_CH_PTN.matcher(left)).matches()) {
          addUnitField(match.group(1), data);
          setGPSLoc(match.group(2)+'.'+match.group(3)+','+match.group(4)+'.'+match.group(5), data);
          channel = match.group(6);
        }
        else if ((match = CHANNEL_PTN.matcher(left)).matches()) {
          addUnitField(match.group(1), data);
          channel = match.group(2);
        }
        else {
          addUnitField(left, data);
          channel = "";
        }
        data.strChannel = channel.replaceAll("  +", " ");
        return true;
      }

      unit = append(unit, ",", p.get(30));
      if (!p.check("Resp Plan:")) break;
      String respPlan = p.get(50);
      if (respPlan.length() > 0) respPlan = "Resp Plan:" + respPlan;
      if (!p.check("GPS:")) break;
      String gps = p.get(80);
      p.checkBlanks(1);
      p.checkBlanks(1);
      if (!p.check("Incident Comments:")) break;
      String info = p.get();

      setFieldList("UNIT ID CODE CALL PLACE ADDR CITY X APT INFO GPS");
      addUnitField(unit, data);
      data.strCallId = callId;
      parseCodeCall(call, data);
      data.strPlace = place;
      parseAddress(addr, data);
      data.strCity = city;
      data.strCross = cleanCrossField(cross);
      data.strApt = append(apt1, "-", data.strApt);
      data.strApt = append(data.strApt, "-", apt2);
      data.strSupp = respPlan;
      parseInfo(info, data);
      setGPSLoc(convertGPS(gps), data);
      return true;

    } while (false);

    do {
      FParser p = new FParser(body);
      String unit = p.get(8);
      p.check("  ");
      if (p.check(ASSIGNED_POST_AT_PTN)) {
        setFieldList("UNIT CALL ADDR INFO");
        data.strUnit = unit;
        data.strCall = "Post assignment";
        data.strAddress = p.get(80);
        data.strSupp = p.get();
        return true;
      }
      p.check(CAD_ID_PTN);
      String callId = "";
      if (!p.check(RUN_PCR_PTN)) {
        callId = p.get(10);
        if (p.check(SECURITY_CHECK_20_MINUTES_PTN)) {
          setFieldList("UNIT ID CALL INFO");
          data.strUnit = unit;
          data.strCallId = callId;
          data.strCall = "SECURITY CHECK - 20 MINUTES";
          data.strSupp = p.get();
          return true;

        }
        if (!p.check(RUN_PCR_PTN)) break;
      }

      boolean locFmt = p.check("Loc:");
      if (!locFmt) {
        callId = append(callId, "/", p.get(8));
        locFmt = p.check("  Loc:");
      }
      if (locFmt) {
        String place = p.get(50);
        p.check(" ");
        String addr = p.get(50);
        String cross = p.get(20);
        if (!p.check("Bldg:")) break;
        String apt = p.get(10);
        if (!p.check("Apt.")) break;
        String apt2 = p.getOptional("City:", 10, 12);
        if (apt2 == null) break;
        apt = append(apt, "-", apt2);
        String city = p.get();

        setFieldList("UNIT ID PLACE ADDR X APT CITY");
        data.strUnit = unit;
        data.strCallId = callId;
        data.strPlace = place;
        parseAddress(addr, data);
        data.strCross = cross;
        data.strApt = apt;
        data.strCity = city;
        return true;
      }

      if (p.check("*RUN REPORT*") || p.check("                      *RUN REPORT*")) {
        setFieldList("UNIT ID INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strUnit = unit;
        data.strCallId = callId;
        data.strSupp = cleanRunReportText(p.get());
        return true;
      }
      String place = p.get(50);
      String addr = p.get(30);

      String call, cross, apt1, apt2, city, gps, map, comment;
      if (p.check("**CANCEL RESPONSE**")) {
        call = "CANCEL - " + p.get(15);
        if (!p.check("Tcr/") && !p.check("TCR=")) break;

        setFieldList("UNIT ID PLACE ADDR APT CALL");
        data.strUnit = unit;
        data.strCallId = callId;
        data.strPlace = place;
        parseAddress(addr, data);
        data.strCall = call;
        return true;
      }
      boolean addrChange = p.check("**RESPONSE ADDRESS CHANGE**");
      if (!addrChange && !p.check("**NEW CALL ASSIGNMENT**")) break;
      if (!p.check("X:")) break;
      cross = p.get(40);
      call = p.get(30);
      if (!p.check(" ")) break;
      call = append(call, "/", p.get(30));
      city = p.get(15);
      if (addrChange) {
        if (p.get(20).length() > 0) break;
        if (!p.check("Apt#:")) break;
      } else {
        if (!p.check("Apt #") && !p.check("Apt#") && !p.check("Apt:")) break;
      }
      apt2 = p.get(10);
      if (!p.check("BLDG#") && !p.check("BLDG:")) break;
      if (addrChange && !p.check(":")) break;
      apt1 = p.get(10);
      if (!p.check("GPS:")) break;
      gps = p.get(2)+'.'+p.get(6);
      if (!p.check("  &")) break;
      gps = gps+','+p.get(3)+'.'+p.get(6);
      if (addrChange) {
        map = "";
        comment = "";
      } else {
        if (!p.check("      ProQA Code:")) break;
        p.check(" ");
        if (!p.check("Map:")) break;
        map = p.get(8);
        p.setOptional();
        if (!p.check(" Comments:") && !p.check(" Notes:")) break;
        comment = p.get();
      }

      setFieldList("UNIT ID PLACE ADDR X CALL CITY APT GPS MAP INFO");
      data.strUnit = unit;
      data.strCallId = callId;
      data.strPlace = place;
      parseAddress(addr, data);
      data.strCross = cleanCrossField(cross);
      data.strCall = call;
      data.strCity = city;
      data.strApt = append(apt1, "-", data.strApt);
      data.strApt = append(data.strApt, "-", apt2);
      setGPSLoc(gps, data);
      data.strMap = map;
      parseInfo(comment, data);
      return true;
    } while (false);

    do {
      FParser p = new FParser(body);
      String call = p.get(30);
      String place = p.get(30);
      String addr = p.get(50);
      String city = p.get(12);
      if (!p.check("CAD #")) break;
      String callId = p.get(16);
      String unit = p.get(10);
      if (!p.check("Bldg:")) break;
      String apt1 = p.get(10);
      if (!p.check("Apt:")) break;
      String apt2 = p.get(10);
      String cross = p.get(30);
      String source = p.get(30);

      setFieldList("CALL PLACE ADDR CITY ID UNIT PLACE APT X SRC");
      data.strCall = call;
      data.strPlace = place;
      parseAddress(addr, data);
      data.strCity = city;
      data.strCallId = callId;
      data.strUnit = unit;
      data.strApt = append(apt1, "-", data.strApt);
      data.strApt = append(data.strApt, "-", apt2);
      data.strCross = cleanCrossField(cross);
      data.strSource = source;
      return true;
    } while (false);

    // There are two types of CAD page, check for the first one
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID CALL ADDR X MAP PLACE APT UNIT");
      addUnitField(getOptGroup(match.group(1)), data);
      data.strCallId = match.group(2);
      data.strCall = match.group(3).trim();
      parseAddress(match.group(4).trim(), data);
      data.strCross = cleanCrossField(getOptGroup(match.group(5)));
      data.strPlace = match.group(6).trim();
      data.strApt = match.group(7).trim();
      addUnitField(getOptGroup(match.group(8)), data);
      return true;
    }

    //  See if this matches the second pattern
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID CALL ADDR APT X MAP UNIT");
      addUnitField(getOptGroup(match.group(1)), data);
      data.strCallId = match.group(2);
      String sAddr = match.group(3).trim();
      if (sAddr.length() <= 20) return false;
      data.strCall = sAddr.substring(0,16).trim();
      Parser p = new Parser(sAddr.substring(16).trim());
      addUnitField(p.getLastOptional("Assigned Units:"), data);
      data.strMap = p.getLast(' ');
      data.strCross = cleanCrossField(p.getLastOptional("  "));
      sAddr = p.get();
      if (data.strCross.length() > 0) {
        parseAddress(sAddr, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, sAddr, data);
        data.strCross = cleanCrossField(getLeft());
      }
      return true;
    }

    // Next try normal field based formats
    setSelectValue("OLD");
    body = body.replace("GPS:", " GPS:");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }

  private String[] splitSlashDelimFields(String body) {
    String comments = null;
    int pt = body.indexOf("/Incident Comments:");
    if (pt >= 0) {
      comments = body.substring(pt+1);
      body = body.substring(0,pt);
    }
    String[] flds = body.split("/");
    if (comments != null) {
      String[] flds2 = new String[flds.length+1];
      System.arraycopy(flds, 0, flds2, 0, flds.length);
      flds2[flds.length] = comments;
      flds = flds2;
    }
    return flds;
  }

  private static final Pattern RUN_REPORT_DELIM_PTN = Pattern.compile("\\s*+((?:Enroute|Resp|On Scene|Scn|TX|Tx|Dest|Dsp|AOR|Clear Call)[:/]|STmiles)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {3,}");

  private String cleanRunReportText(String text) {
    text = RUN_REPORT_DELIM_PTN.matcher(text).replaceAll("\n$1");
    text = MBLANK_PTN.matcher(text).replaceAll("\n");
    return text;
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Za-z0-9]+)- +(.*)");
  private void parseCodeCall(String codeCall, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(codeCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      codeCall = match.group(2);
    }
    data.strCall = codeCall;
  }

  private String cleanCrossField(String field) {
    field = stripFieldStart(field, "No Cross Street/");
    int pt = field.lastIndexOf('/');
    if (pt >= 0) {
      if ("No Cross Street".startsWith(field.substring(pt+1).trim())) {
        field = field.substring(0,pt).trim();
      }
    } else {
      if ("No Cross Street".startsWith(field)) field = "";
    }
    return field.replace('/', '&');
  }

  private void addUnitField(String field, Data data) {

    if (field.length() == 0) return;

    Set<String> unitSet = new HashSet<String>();
    if (data.strUnit.length() > 0) {
      for (String unit : UNIT_DELIM_PTN.split(data.strUnit)) {
        unitSet.add(unit);
      }
    }

    for (String unit : UNIT_DELIM_PTN.split(field)) {
      if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
    }
  }
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[ ,]+");

  private static final Pattern GPS_PTN = Pattern.compile("(\\d+)(\\d{6}) *& *(\\d+)(\\d{6})");
  private String convertGPS(String gps) {
    Matcher match = GPS_PTN.matcher(gps);
    if (!match.matches()) return "";
    return match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4);
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[, ]*\\[[^\\[\\]]*\\][, ]*| {3,}");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("PSAP Caller Source:.*");

  private void parseInfo(String info, Data data) {
    for (String part : INFO_DELIM_PTN.split(info)) {
      if (part.length() == 0) continue;
      if (INFO_JUNK_PTN.matcher(part).matches()) continue;
      if (part.startsWith("Caller Lat/Lon:")) {
        if (data.strGPSLoc.length() == 0) {
          part = part.substring(15).trim().replace('/', ',');
          setGPSLoc(part, data);
        }
        continue;
      }
      data.strSupp = append(data.strSupp, "\n", part);
    }
  }


  @Override
  public Field getField(String name) {
    if (name.equals("SELECT")) return new MySelectField();
    if (name.equals("PRI2")) return new PriorityField("P\\d .*", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY1")) return new MyCity1Field();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("X1")) return new MyCross1Field();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("PLAN")) return new MyPlanField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ASS_ADDR")) return new MyAssignAddressField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private class MySelectField extends SelectField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (getQual().equals("RR")) {
        String select = getSelectValue();
        if (select.equals("RESPONSE_INFORMATION") | select.equals("RESPONSE_INFO_UPDATE")) return false;
        data.msgType = MsgType.RUN_REPORT;
        data.strCall = select;
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, " - ", field);
    }
  }

  private static final Pattern CITY_PTN = Pattern.compile("(.*)[ \\(]CAD INC#: ?([^ ]+) *\\)? ?(?:\\[GROUP PAGE\\]|\\(GROUP PAGE\\))");
  private class MyCity1Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CITY ID";
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      super.parse(convertGPS(field), data);
    }
  }

  private class MyCross1Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strChannel = match.group(2);
      }
      field = cleanCrossField(field);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X CH";
    }
  }

  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Street")) return;
      super.parse(field, data);
    }
  }

  private class MyPlanField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strSupp = "Resp Plan: " + stripFieldEnd(field,  ",");
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0,pt);
      addUnitField(field, data);
    }
  }

  private static final Pattern ASSIGN_ADDR_PTN = Pattern.compile("You have been assigned to post at: *(\\S+) (.*)");
  private class MyAssignAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ASSIGN_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = "POST ASSIGNMENT: " + match.group(1);
      parseAddress(match.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT";
    }
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      parseCodeCall(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strChannel)) return;
      data.strChannel = append(data.strChannel, "/", field);
    }
  }

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseInfo(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS?";
    }
  }
}
