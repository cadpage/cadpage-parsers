package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchGlobalDispatchParser extends FieldProgramParser {

  // Station and unit codes lead the call/address field
  public static final int LEAD_SRC_UNIT_ADDR = 1;

  // Station and unit codes follow the call/address field
  public static final int TRAIL_SRC_UNIT_ADDR = 2;

  // Call description follows the address
  public static final int CALL_FOLLOWS_ADDR = 4;

  // Place name occurs between address and city name
  public static final int PLACE_FOLLOWS_ADDR = 8;

  // Place name follows call description
  public static final int PLACE_FOLLOWS_CALL = 0x10;

  public DispatchGlobalDispatchParser(String defCity, String defState) {
    this(null, defCity, defState, 0, null, null);
  }

  public DispatchGlobalDispatchParser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, 0, null, null);
  }

  public DispatchGlobalDispatchParser(String defCity, String defState, int flags) {
    this(null, defCity, defState, flags, null, null);
  }

  public DispatchGlobalDispatchParser(String[] cityList, String defCity, String defState, int flags) {
    this(cityList, defCity, defState, flags, null, null);
  }

  private static final Pattern CALL_NUMBER_PTN = Pattern.compile("^Call Number: *(\\d+) +");
  private static final Pattern KEYWORD_PTN = Pattern.compile("(?:MapRegions|Description|CrossStreets|Description|Dispatch|Primary_Incident):");
  private static final Pattern DELIM_PTN = Pattern.compile("\n| (?=ReferenceCode:|MapRegions:|CrossStreets:|Dispatch:|Description:)");

  private Pattern stationPtn;
  private Pattern unitPtn;
  private boolean leadStuff;
  private boolean trailStuff;
  private boolean placeFollowsAddress;

  public DispatchGlobalDispatchParser(String[] cityList, String defCity, String defState,
                                       int flags, Pattern stationPtn, Pattern unitPtn) {
    super(cityList, defCity, defState,
         "( ReferenceCode:CODE! CALL ADDR/S! EMPTY Description:INFO! INFO/N+? TRAIL_UNIT " +
         "| " + calcAddressTerm(flags, cityList != null) +
          " Call_Received_Time:DATE_TIME_CITY MapRegions:MAP Description:INFO2 INFO2+ CrossStreets:X UNIT Description:INFO2 INFO2+ Dispatch:DATETIME " +
          "Primary_Incident:ID Call_Number:ID Description:INFO2 INFO2+ ReferenceText:INFO2 Dispatch:SKIP Caller:NAME Call_Number:ID Description:INFO2 INFO2+ ReferenceText:SKIP Call_Number:ID Map_Link:GPS )",
          FLDPROG_NL_BRK);
    this.stationPtn = stationPtn;
    this.unitPtn = unitPtn;
    leadStuff = (flags & LEAD_SRC_UNIT_ADDR) != 0;
    trailStuff = (flags & TRAIL_SRC_UNIT_ADDR) != 0;
    placeFollowsAddress = (flags & PLACE_FOLLOWS_ADDR) != 0;
  }

  private static final String calcAddressTerm(int flags, boolean inclCity) {
    StringBuilder sb = new StringBuilder("( SELECT/NEW ");
    if ((flags & CALL_FOLLOWS_ADDR) == 0) sb.append("CALL1! ");
    sb.append("ReferenceCode:SKIP? ");
    sb.append("ADDR/S6");
    sb.append(testFlag(flags, CALL_FOLLOWS_ADDR) ? "XC" :
              testFlag(flags, PLACE_FOLLOWS_ADDR) ? "XP" : "");
    sb.append("! ");
    if (inclCity) sb.append("CITY? ");
    if ((flags & CALL_FOLLOWS_ADDR) != 0) sb.append("( CALL! Primary_Incident:ID! | PLACE CALL! ) ");
    if ((flags & TRAIL_SRC_UNIT_ADDR) != 0) sb.append("UNIT ");
    sb.append(" | ADDR2/S6");
    sb.append(testFlag(flags, CALL_FOLLOWS_ADDR) ? "XC" :
              testFlag(flags, PLACE_FOLLOWS_CALL | PLACE_FOLLOWS_ADDR) ? "LP" :
              testFlag(flags, PLACE_FOLLOWS_CALL) ? "LX" :
              testFlag(flags, PLACE_FOLLOWS_ADDR) ? "CP" : "CX");
    sb.append("! )");
    return sb.toString();
  }

  private static boolean testFlag(int flags, int mask) {
    return ((flags & mask) == mask);
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    String prefix = "";
    if (body.startsWith("SECOND PAGE")) {
      prefix = "SECOND PAGE";
      body = body.substring(11).trim();
    }

    Matcher match = CALL_NUMBER_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    }

    // See if this is the new fangled line break separated format
    // Beware of false positives, occasionally an old format will
    // have enough newlines to trigger the old logic
    boolean newFmt = body.startsWith("ReferenceCode:");
    if (!newFmt) {
      match = KEYWORD_PTN.matcher(body);
      int pt = (match.find() ? match.start() : body.length());
      newFmt = (body.substring(0,pt).contains("\n"));
    }
    if (newFmt) {
      setSelectValue("NEW");
      if (!parseFields(DELIM_PTN.split(body), data)) return false;
      if (data.strCallId.length() > 0 || data.strMap.length() > 0 || data.strSupp.length() > 0 ||
          data.strCross.length() > 0 || data.strTime.length() > 0) return true;

      // Still questionable.  If there is a unit field and pattern, see if
      // they match
      if (data.strUnit.length() > 0 && unitPtn != null) {
        boolean good = true;
        for (String unit : data.strUnit.split(" +")) {
          if (!unitPtn.matcher(unit).matches()) {
            good = false;
            break;
          }
        }
        if (good) return true;
      }
      return false;
    }

    // Otherwise use the standard line break format
    setSelectValue("OLD");
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) return false;
    if (data.strCity.length() == 0 && data.strUnit.length() == 0 && data.strCross.length() == 0 && !body.contains("Description:")) return false;
    data.strCall = append(prefix, " - ", data.strCall);
    return true;
  }

  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new BaseCall1Field();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("ADDR2")) return new BaseAddress2Field();
    if (name.equals("DATE_TIME_CITY")) return new BaseDateTimeCityField();
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("INFO2")) return new BaseInfo2Field();
    if (name.equals("ID")) return new BaseIdField();
    if (name.equals("TRAIL_UNIT")) return new BaseTrailUnitField();
    if (name.equals("GPS")) return new GPSField("https?://google.com/maps\\?q=(.*)", false);
    return super.getField(name);
  }

  protected class BaseCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripUnitSrcData(leadStuff, false, field, data);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      if (leadStuff) return "UNIT SRC CALL";
      else return "CALL";
    }
  }

  protected class BaseAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      field = field.replace(",", "");
      super.parse(field, data);
    }
  }

  protected class BaseAddress2Field extends AddressField {

    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll(",", "");

      field = stripUnitSrcData(leadStuff, trailStuff, field, data);
      super.parse(field, data);

      // If we place follows the address, see if it looks more like an
      // extension of the address
      if (placeFollowsAddress) {
        if (data.strPlace.startsWith("&") || data.strPlace.startsWith("/")) {
          data.strAddress = append(data.strAddress, " & ", data.strPlace.substring(1).trim());
          data.strPlace = "";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC UNIT " + super.getFieldNames();
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.contains("&")) return true;
    return super.isNotExtraApt(apt);
  }

  /**
   * Strip leading and/or trailing source/unit information from field
   * @param leadStuff if leading stuff should be checked
   * @param trailStuff true if trailing stuff should be checked
   * @param field data field
   * @param data data object
   * @return
   */
  private String stripUnitSrcData(boolean leadStuff, boolean trailStuff, String field, Data data) {

    // If we have station or unit patterns, these need to be stripped off
    // the front and back of the address field
    if (leadStuff || trailStuff) {

      // Start by splitting field into list of words, and identifying
      // each word as station, unit or neither
      String[] words = field.split(" +");
      int[] types = new int[words.length];
      int stReg = 0;
      if (leadStuff) {
        for (stReg = 0; stReg<words.length; stReg++) {
          String word = words[stReg];
          types[stReg] = (stationPtn != null && stationPtn.matcher(word).matches() ? 1 :
                          unitPtn != null && unitPtn.matcher(word).matches() ? 2 : 0);
          if (types[stReg] == 0) break;
        }
      }
      int endReg = words.length-1;
      if (stReg < words.length) {
        if (trailStuff) {
          for ( ; endReg > stReg; endReg--) {
            String word = words[endReg];
            types[endReg] = (stationPtn != null && stationPtn.matcher(word).matches() ? 1 :
                            unitPtn != null && unitPtn.matcher(word).matches() ? 2 : 0);
            if (types[endReg] == 0) break;
          }
        }
        for (int ii = stReg+1; ii < endReg; ii++) types[ii] = 0;
      }

      // Construct three Stringbuilders with all of the regular, station, and unit words
      StringBuilder[] sba = new StringBuilder[3];
      for (int ii = 0; ii < 3; ii++) sba[ii] = new StringBuilder();
      for (int ii = 0; ii<words.length; ii++) {
        StringBuilder sb = sba[types[ii]];
        if (sb.length() > 0) sb.append(' ');
        sb.append(words[ii]);
      }

      // And finally convert the StringBuilders back to the appropriate fields
      field = sba[0].toString();
      data.strSource = sba[1].toString();
      data.strUnit = sba[2].toString();
    }
    return field;
  }

  private class BaseDateTimeCityField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strDate = p.get(' ');
      data.strTime = p.get(' ');
      data.strCity = p.get();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CITY";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  protected class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("\\[(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) \\d+\\]");
  protected class BaseInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (match.find()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        field = match.replaceAll("\n").trim();
      }
      field = field.replaceAll("\\s{2,}", " ");
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(data.strCallId, "/", field);
    }
  }

  private Pattern TRAIL_UNIT_PTN = Pattern.compile("[- A-Z0-9]+");
  private class BaseTrailUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!TRAIL_UNIT_PTN.matcher(field).matches()) return false;
      data.strUnit = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    if (cross) {
      Matcher match = X_MI_MARK_PTN.matcher(sAddress);
      if (match.find()) sAddress = sAddress.substring(0,match.start());
    }
    return super.adjustMapAddress(sAddress, cross);
  }
  private static final Pattern X_MI_MARK_PTN = Pattern.compile(" +\\d+\\.\\d+ mi\\b");


}
