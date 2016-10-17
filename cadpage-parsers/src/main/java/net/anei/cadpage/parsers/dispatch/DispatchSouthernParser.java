package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchSouthernParser extends FieldProgramParser {
  
  // Flag indicating  a leading dispatch name is required
  public static final int DSFLAG_DISPATCH_ID = 0x01;
  
  // Flag indicating a leading dispatch name is optional
  public static final int DSFLAG_OPT_DISPATCH_ID = 0x02;
  
  // Flag indicate a unit designation follows the time stamp
  public static final int DSFLAG_UNIT = 0x04;
  
  // Flag indicating that the call ID is optional
  public static final int DSFLAG_ID_OPTIONAL = 0x08;
  
  // Flag indicating a place name may precede the address
  // And Name/Phone number follows address
  public static final int DSFLAG_LEAD_PLACE = 0x010;
  
  // Flag indicating cross street information follows the address instead of
  // the usual name & phone
  public static final int DSFLAG_FOLLOW_CROSS = 0x20;
  
  // Flag indicating address will be followed by cross street, and then the usual
  // name & phone
  public static final int DSFLAG_CROSS_NAME_PHONE = 0x40;
  
  // Flag indicating there is no name and phone following the address
  public static final int DSFLAG_NO_NAME_PHONE = 0x80;
  
  // Flag indicating we should not check for implied non-numeric apartments
  public static final int DSFLAG_NO_IMPLIED_APT = 0x100;
  
  // Flag indicating call description follows address
  public static final int DSFLAG_FOLLOW_CALL = 0x200;
  
  // Flag indicating optional unit designation preceedes call ID
  public static final int DSFLAG_LEAD_UNIT = 0x400;
  
  // Flag indicating place name can be in front of and behind the address
  public static final int  DSFLAG_BOTH_PLACE = 0x800;
  
  // Flag indicating there is no place name
  public static final int DSFLAG_NO_PLACE = 0x1000;
  
  // Flag indicating a place follows the address, even in field delimited mode
  // This is the default behavior in space delimited mode
  public static final int DSFLAG_TRAIL_PLACE = 0x2000;
  
  // Flag indicating a state may follow the address
  public static final int DSFLAG_STATE = 0x4000;
  
  // Flag indicating time is optional :(
  public static final int DSFLAG_TIME_OPTIONAL = 0x8000;
  
  // Flag indicating place field following address is a place field
  public static final int DSFLAG_PLACE_FOLLOWS = 0x10000;

  private boolean parseFieldOnly;

  private boolean leadDispatch;
  private boolean optDispatch;
  private boolean unitId;
  private boolean idOptional;
  private boolean leadPlace;
  private boolean trailPlace;
  private boolean trailPlace2;
  private boolean inclCross;
  private boolean inclCrossNamePhone;
  private boolean noNamePhone;
  private boolean impliedApt;
  private boolean inclCall;
  private boolean leadUnitId;
  private boolean state;
  private boolean timeOptional;
  private boolean inclPlace;
  private CodeSet callSet;
  private Pattern unitPtn;
  
  private String defaultFieldList;
  
  public DispatchSouthernParser(String[] cityList, String defCity, String defState) {
    this(null, cityList, defCity, defState, DSFLAG_DISPATCH_ID, null);
  }
  
  public DispatchSouthernParser(String[] cityList, String defCity, String defState, int flags) {
    this(null, cityList, defCity, defState, flags, null);
  }
  
  public DispatchSouthernParser(String[] cityList, String defCity, String defState, int flags, String unitPtnStr) {
    this(null, cityList, defCity, defState, flags, unitPtnStr);
  }
  
  public DispatchSouthernParser(CodeSet callSet, String[] cityList, String defCity, String defState, int flags) {
    this(callSet, cityList, defCity, defState, flags, null);
  }
  
  public DispatchSouthernParser(CodeSet callSet, String[] cityList, String defCity, String defState, int flags, String unitPtnStr) {
    super(cityList, defCity, defState, "");
    this.parseFieldOnly = false;
    this.callSet = callSet;
    this.leadDispatch = (flags & DSFLAG_DISPATCH_ID) != 0;
    this.optDispatch = (flags & DSFLAG_OPT_DISPATCH_ID) != 0;
    this.unitId = (flags & DSFLAG_UNIT) != 0;
    this.idOptional = (flags & DSFLAG_ID_OPTIONAL) != 0;
    this.leadPlace = (flags &  (DSFLAG_BOTH_PLACE | DSFLAG_LEAD_PLACE)) != 0;
    this.trailPlace = (flags & (DSFLAG_LEAD_PLACE | DSFLAG_NO_PLACE)) == 0;
    this.trailPlace2 = (flags & (DSFLAG_BOTH_PLACE | DSFLAG_TRAIL_PLACE)) != 0;
    this.inclCross = (flags & DSFLAG_FOLLOW_CROSS) != 0;
    this.inclCrossNamePhone = (flags & DSFLAG_CROSS_NAME_PHONE) != 0;
    this.impliedApt = (flags & DSFLAG_NO_IMPLIED_APT) == 0;
    this.noNamePhone = (flags & DSFLAG_NO_NAME_PHONE) != 0;
    this.inclCall = (flags & DSFLAG_FOLLOW_CALL) != 0;
    this.leadUnitId = (flags & DSFLAG_LEAD_UNIT) != 0;
    this.state = (flags & DSFLAG_STATE) != 0;
    this.timeOptional = (flags & DSFLAG_TIME_OPTIONAL) != 0;
    this.inclPlace = (flags & DSFLAG_PLACE_FOLLOWS) != 0;
    this.unitPtn = (unitPtnStr == null ? null : Pattern.compile(unitPtnStr));

    
    // Program string needs to be built at run time
    StringBuilder sb = new StringBuilder();
    sb.append("ADDR/S2");
    if (impliedApt) sb.append("6");
    sb.append(leadPlace ? 'P' : 'X');
    if (trailPlace2) sb.append("P");
    if (state) sb.append(" ST?");
    if (inclPlace) {
      sb.append(" PLACE?");
    } else {
      if (inclCall) sb.append(" CALL");
      if (inclCross || inclCrossNamePhone) sb.append(" X?");
      if (!inclCross && !noNamePhone) sb.append(" NAME+? PHONE?");
      sb.append(" CODE+? PARTCODE?");
      if (leadUnitId) sb.append(" UNIT?");
    }
    sb.append(" ID");
    if (idOptional) sb.append('?');
    sb.append(" TIME");
    if (timeOptional) sb.append('?');
    sb.append(" INFO+ OCA:ID2");
    String program = sb.toString();
    setProgram(program, 0);
    
    sb = new StringBuilder();
    if (leadPlace) sb.append("PLACE ");
    sb.append("ADDR PLACE APT");
    sb.append(inclCross || inclCrossNamePhone ? " CITY X" : " X CITY");
    if (inclCall) sb.append(" CALL");
    if (!inclCross && !noNamePhone) sb.append(" NAME");
    sb.append(" PHONE");
    if (leadUnitId) sb.append(" UNIT");
    sb.append(" CODE ID TIME");
    if (unitId) sb.append(" UNIT");
    if (!inclCall) sb.append(" CALL");
    sb.append(" INFO");
    defaultFieldList = sb.toString();
  }
  
  public DispatchSouthernParser(String[] cityList, String defState, String defCity, String program) {
    super(cityList, defState, defCity, program);
    this.parseFieldOnly = true;
  }

  @Override
  protected void setupCallList(CodeSet callSet) {
    this.callSet = callSet;
    super.setupCallList(callSet);
  }

  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("(?:[A-Z]+:)?(\\d{8,10}|[A-Z]\\d{2}-\\d+)[ ;] *([- A-Z0-9]+)\\(.*\\)\\d\\d:\\d\\d:\\d\\d\\|");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("CFS: *(\\S+), *Unit: *(\\S+), *(Status:.*)");
  private static final Pattern LEAD_PTN = Pattern.compile("^[\\w\\.@]+:");
  private static final Pattern NAKED_TIME_PTN = Pattern.compile("([ ,;]) *(\\d\\d:\\d\\d:\\d\\d)(?:\\1|$)");
  private static final Pattern OCA_TRAIL_PTN = Pattern.compile("\\bOCA: *([-A-Z0-9]+)$");
  private static final Pattern ID_PTN = Pattern.compile("\\b\\d{2,4}-?(?:\\d\\d-)?\\d{4,8}$");
  private static final Pattern CALL_PTN = Pattern.compile("^([A-Z0-9\\- /()]+)\\b[ \\.,-]*");
  private static final Pattern PHONE_PTN = Pattern.compile("\\b\\d{10}\\b");
  private static final Pattern EXTRA_CROSS_PTN = Pattern.compile("(?:AND +|[/&] *)(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_BRK_PTN = Pattern.compile(" +/+ *");
  private static final Pattern VERIFY_X_PTN = Pattern.compile(" *\\(Verify\\) *");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = body.substring(match.start(2)).replace('|', '\n').trim();
      return true;
    }
    
    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType= MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = match.group(3).replaceAll(", +", "\n");
      return true;
    }
    
    if (parseFieldOnly) {
      if (!parseDelimitedFields(body, data)) return false;
    }
    
    else {
      // Message must always start with dispatcher ID, which we promptly discard
      if (leadDispatch || optDispatch) {
        match = LEAD_PTN.matcher(body);
        if (match.find()) {
          body = body.substring(match.end()).trim();
        } else if (!optDispatch) return false;
      }
      
      // See if this looks like one of the new comma delimited page formats
      // If it is, let FieldProgramParser handle it.
      match = NAKED_TIME_PTN.matcher(body);
      if (!match.find()) {
        if (!timeOptional) return false;
        if (!parseDelimitedFields(body, data)) return false;
        if (data.strCallId.length() == 0 && data.strTime.length() == 0 && data.strCode.length() == 0 && data.strGPSLoc.length() == 0) return false;
      } else {
        String delim = match.group(1);
        if (delim.charAt(0) != ' ') {
          body = body.replace(" OCA:", delim + "OCA:");
          if (!parseFields(body.split(delim + '+'), data)) return false;
        }
        
        // Blank delimited fields get complicated
        // We already found a time field.  Use that to split the message 
        // into and address and extra versions 
        else {
          setFieldList(defaultFieldList);
          data.strTime = match.group(2);
          String sAddr = body.substring(0,match.start()).trim();
          String sExtra = body.substring(match.end()).trim();
          
          // See if there is an ID field immediate in front of the time field
          match = ID_PTN.matcher(sAddr);
          if (match.find()) {
            data.strCallId = match.group();
            sAddr = sAddr.substring(0,match.start()).trim();
          } else if (!idOptional) return false;
          
          // See if there is a labeled OCA field at the end of the extra block
          match = OCA_TRAIL_PTN.matcher(sExtra);
          if (match.find()) {
            data.strCallId = match.group(1).trim();
            sExtra = sExtra.substring(0,match.start()).trim();
          }
          
          if (sAddr.length() > 0) {
            parseMain(sAddr, data);
            parseExtra(sExtra, data);
          } else {
            parseExtra2(sExtra, data);
          }
        }
      }
    }
    
    // set an call description if we do not have one
    if (data.strCall.length() == 0 && data.strSupp.length() == 0) data.strCall= "ALERT";
    
    // Remove any asterisks or verify markers from cross street info
    data.strCross = data.strCross.replace("*", "");
    data.strCross = VERIFY_X_PTN.matcher(data.strCross).replaceAll(" ").trim();
    
    // Apparently there is no way to not enter a street number, entering 0 or 1 is the accepted
    // workaround.
    if (data.strAddress.startsWith("1 ") || data.strAddress.startsWith("0 ")) {
      data.strAddress = data.strAddress.substring(2).trim();
      String cross = data.strCross;
      if (!cross.contains("/") && !cross.contains("&")) {
        data.strAddress = append(data.strAddress, " & ", cross);
        data.strCross = "";
      }
    }
    
    //  Occasional implied intersections end up in the apt field
    if (data.strApt.length() > 0) {
      int status = checkAddress(data.strApt);
      if (status == STATUS_STREET_NAME) {
        data.strAddress = append(data.strAddress, " & ", data.strApt);
        data.strApt = "";
      } else if (status == STATUS_INTERSECTION) {
        data.strCross = append(data.strApt, " / ", data.strCross);
        data.strApt = "";
      }
    }
    
    return true;
  }

  private boolean parseDelimitedFields(String body, Data data) {
    String ocaField = null;
    int pt = body.lastIndexOf(" OCA:");
    if (pt >= 0) {
      ocaField = body.substring(pt+1);
      body = body.substring(0,pt);
    }
    String[] flds = body.split(";");
    String[] flds2 = body.split(",");
    if (flds2.length > flds.length) flds = flds2;
    if (ocaField != null) {
      flds2 = flds;
      flds = new String[flds2.length+1];
      System.arraycopy(flds2, 0, flds, 0, flds2.length);
      flds[flds2.length] = ocaField;
    }
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  protected void parseMain(String sAddr, Data data) {
    // First half contains address, optional place/name, and possibly an MDL call code
    Parser p = new Parser(sAddr);
    data.strCode = p.getLastOptional(" MDL ");
    if (data.strCode.length() == 0) data.strCode = p.getLastOptional(" FDL ");
    if (data.strCode.length() == 0) data.strCode = p.getLastOptional(" LDL ");
    sAddr = p.get();
    StartType st = (leadPlace ? StartType.START_PLACE : StartType.START_ADDR);
    int flags = FLAG_AT_SIGN_ONLY;
    if (impliedApt) flags |= FLAG_RECHECK_APT;
    if (inclCross || inclCrossNamePhone) flags |= FLAG_CROSS_FOLLOWS;
    if (noNamePhone && !trailPlace) flags |= FLAG_ANCHOR_END;
    boolean leadOne = sAddr.startsWith("1 ");
    if (leadOne) {
      sAddr = sAddr.substring(2).trim();
      st = StartType.START_ADDR;
    }
    parseAddress(st, flags, sAddr, data);
    if (leadOne) data.strAddress = append("1", " ", data.strAddress);
    String sLeft = getLeft();
    
    // If everything went to place, move it back to address
    if (st == StartType.START_PLACE && data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    
    // Processing what is left gets complicated
    // First strip anything that looks like a trailing phone number
    Matcher match = PHONE_PTN.matcher(sLeft);
    if (match.find()) {
      data.strPhone = match.group();
      sLeft = sLeft.substring(0,match.start()).trim();
    }
    
    // If this is a (misused) call description, make it so
    if (inclCall) {
      data.strCall = sLeft;
    }
    
    // if cross street information follows the address, process that
    else if (inclCross) {
      match = EXTRA_CROSS_PTN.matcher(sLeft);
      if (match.matches()) {
        parseAddress(match.group(1), data);
      } else {
        sLeft = stripFieldStart(sLeft, "AT ");
        sLeft = sLeft.replace(" X ", " / ");
        sLeft = stripFieldEnd(sLeft, " X");
        data.strCross = append(data.strCross, " & ", sLeft);
      }
    } 
    
    else {
      
      if (inclCrossNamePhone) {
        int pt = sLeft.indexOf(" X ");
        if (pt >= 0) {
          String save = sLeft.substring(0,pt).trim();
          sLeft = sLeft.substring(pt+3).trim();
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, sLeft, data);
          data.strCross = save + " / " + data.strCross;
          sLeft = getLeft();
        }
      }
      
      // Otherwise, if the place name isn't located in front of the address
      // assume whatever follows it is a place name
      if (trailPlace) {
        if (sLeft.startsWith("/")) {
          data.strAddress = data.strAddress + " & " + sLeft.substring(1).trim();
        } else {
          data.strPlace = append(data.strPlace, " - ", sLeft);
        }
      }
  
      // Otherwise assume it is a name followed by an optional phone number
      else {
        data.strName = cleanWirelessCarrier(sLeft);
      }
    }
  }

  protected void parseExtra(String sExtra, Data data) {

    // Second half May contain unit ID,
    // then call description and long call description
    // Call description comes first and contains only upper case letters and numbers
    Matcher match;
    if (unitId) {
      Parser p = new Parser(sExtra);
      String unit = p.get(' ');
      if (unitPtn == null || unitPtn.matcher(unit).matches()) {
        data.strUnit = unit;
        sExtra = p.get();
      }
    }
    
    if (inclCall) {
      data.strSupp = sExtra;
      return;
    }
    
    if (callSet != null) {
      String call = callSet.getCode(sExtra, true);
      if (call != null) {
        data.strCall = call;
        data.strSupp = stripFieldStart(sExtra.substring(call.length()).trim(), "/");
        return;
      }
    }
    
    match = CALL_PTN.matcher(sExtra);
    if (match.find() && match.end() > 0 && match.end() < sExtra.length()) {
      String sCall = match.group(1).trim();
      if (sCall.length() <= 30) {
        sExtra = sExtra.substring(match.end()).trim();
        if (sExtra.startsWith("y")) {
          int pt = sCall.length();
          while (pt > 0 && Character.isDigit(sCall.charAt(pt-1))) pt--;
          if (pt > 0 && pt < sCall.length()) {
            sExtra = sCall.substring(pt) + ' ' + sExtra;
            sCall = sCall.substring(0,pt).trim();
          }
        }
        data.strCall = sCall;
        data.strSupp = sExtra;
        return;
      }
    }
    
    if (sExtra.length() <= 30) {
      data.strCall = sExtra;
      return;
    }
    
    match = CALL_BRK_PTN.matcher(sExtra);
    if (match.find() && match.start() <= 30) {
      data.strCall = sExtra.substring(0,match.start()).trim();
      data.strSupp = sExtra.substring(match.end()).trim();
      return;
    }
    
    data.strSupp = sExtra;
  }

  protected void parseExtra2(String sExtra, Data data) {
    // First half contains address, optional place/name, and possibly an MDL call code
    Parser p = new Parser(sExtra);
    data.strCode = p.getLastOptional(" MDL ");
    if (data.strCode.length() == 0) data.strCode =p.getLastOptional(" FDL ");
    sExtra = p.get();
    parseAddress(StartType.START_CALL_PLACE, FLAG_AT_SIGN_ONLY | FLAG_RECHECK_APT, sExtra, data);
    data.strSupp = getLeft();
  }
  
  //  Classes for handling the new comma delimited format
  
  private class BaseCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(" X ") && !field.startsWith("X ") && !field.endsWith(" X")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" X ", " / ");
      field = stripFieldStart(field, "X ");
      field = stripFieldEnd(field, " X");
      field = stripFieldStart(field, "*");
      field = field.replace("/ *", "/ ");
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CODE"))  return new BaseCodeField();
    if (name.equals("PARTCODE")) return new SkipField("[MFL]D?");
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("ID")) return new IdField("[A-Z]?\\d\\d(?:\\d\\d)?-?\\d{4,8}", true);
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("ID2")) return new IdField("\\d{6}-\\d{2,4}");
    return super.getField(name);
  }
  
  protected class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      if (field.startsWith("1 ")) {
        field = field.substring(2).trim();
        int flags = FLAG_AT_SIGN_ONLY;
        if (!trailPlace2) flags |= FLAG_ANCHOR_END;
        if (impliedApt) flags |= FLAG_RECHECK_APT;
        parseAddress(StartType.START_ADDR, flags, field, data);
        if (trailPlace2) data.strPlace = append(data.strPlace, " - ", getLeft());
        data.strAddress = append("1", " ", data.strAddress);
      } else {
        super.parse(field, data);
        
        // If we pulled a place name from the front fo the string, see if
        // it looks like a legitimate address that got mangled by something
        // further back
        if (data.strPlace.length() > 0 && field.startsWith(data.strPlace)) {
          if (data.strPlace.endsWith("/")) {
            String place = data.strPlace.substring(0,data.strPlace.length()-1).trim().replace('/', '&');
            data.strAddress = append(place, " & ", data.strAddress);
            data.strPlace = "";
          } else {
            int flags = FLAG_CHECK_STATUS | FLAG_AT_SIGN_ONLY | FLAG_ANCHOR_END;
            if (impliedApt) flags |= FLAG_RECHECK_APT;
            Result res = parseAddress(StartType.START_ADDR, flags, field);
            if (res.isValid() && res.getStatus() >= getStatus()) {
              data.strPlace = data.strAddress = data.strApt = data.strCity = "";
              res.getData(data);
            }
          }
        }
      }
    }
  }
  
  // Name field continues until it finds a phone number, call number, or time
  private static final Pattern NOT_NAME_PTN = Pattern.compile("\\d{10}|\\d\\d(?:\\d\\d)?-?\\d{5,8}|\\d\\d:\\d\\d:\\d\\d");
  class BaseNameField extends NameField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_NAME_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strName = append(data.strName, ", ", field);
    }
  }
  
  private class BaseCodeField extends CodeField {
    public BaseCodeField() {
      super("[FML]DL *(.*)", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCode.length() == 0) super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_PRI_PTN = Pattern.compile("\\d");
  protected class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      if (unitId && data.strUnit.length() == 0) {
        Parser p = new Parser(field);
        String unit = p.get(' ');
        if (unitPtn == null || unitPtn.matcher(unit).matches()) {
          data.strUnit = unit;
          field = p.get();
        }
      }
      
      if (field.startsWith("geo:")) {
        field = setGPSLoc(field.substring(4).trim(), data);
        return;
      }
      
      if (data.strCall.length() == 0) {
        data.strCall = field;
        return;
      }
      
      if (data.strSupp.length() == 0 && data.strPriority.length() == 0 && 
          INFO_PRI_PTN.matcher(field).matches()) {
        data.strPriority = field;
        return;
      }
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CALL PRI GPS INFO";
    }
  }
}
