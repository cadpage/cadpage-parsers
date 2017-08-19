package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/*
 * 
Improved version of DispatchBParser that does a better job of handling call codes and
message prefixes

This has been identified as Interact GIS system
And as CAD Interactive
Go Figure

*/

public class DispatchB2Parser extends DispatchBParser {
  
  // Flag requesting that first word of call description be considered a call
  // code if there is no marker
  public static final int B2_FORCE_CALL_CODE = 1;
  
  // Flag requesting that we tell the underlying smart address logic that cross street
  // information will be following the main address
  public static final int B2_CROSS_FOLLOWS = 2;
  
  // Flag indicating that the call code is optional
  public static final int B2_OPT_CALL_CODE = 4;
  
  // Flag indicating that will be a separate apt field following the city
  public static final int B2_SEPARATE_APT_FLD = 8;
  
  // Flag indicating that we will only recognized delimited field formats
  public static final int B2_DELIM_ONLY = 0x10;
  
  private static final Pattern CODE_PATTERN = Pattern.compile("^([- /#&_A-Z0-9]{0,6}?|\\?) *> *"); 
  private static final Pattern PHONE_PTN = Pattern.compile("[ /]*((?<!\\d)(?:(?:\\d{3}[- ]?)?\\d{3}[- ]?\\d{4}\\b *)+)[ /]*");
  private static final Pattern NAME_PTN = Pattern.compile(" +([A-Z]+, ?[A-Z]+(?: [A-Z]\\.?)?)$");
  private static final Pattern INTERSECT_PTN = Pattern.compile("(?:[&/]|AND |([NSEW]O) |[NSEW][EW]? OF ).*", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile(" +([NSEW]B?)$");
  private static final Pattern CROSS_LABEL_PTN = Pattern.compile("\\b(?:XS|CS|C/S)[: ] *");
  private static final Pattern APT1_PTN = Pattern.compile("Apt: *(\\S+) *");
  private static final Pattern BLDG_PTN = Pattern.compile("Bldg(?: (\\d+))?\\b *"); 
  private static final Pattern APT2_PTN = Pattern.compile("[A-Z]?\\d+[A-Z]?");
  
  private String[] prefixList;
  private boolean forceCallCode;
  private boolean crossFollows;
  private boolean optCallCode;

  public DispatchB2Parser(String[] cityList, String defCity, String defState) {
    this(null, cityList, defCity, defState, 0);
  }

  public DispatchB2Parser(String[] cityList, String defCity, String defState, int flags) {
    this(null, cityList, defCity, defState, flags);
  }
  
  public DispatchB2Parser(int version, String[] cityList, String defCity, String defState) {
    super(version, cityList, defCity, defState);
  }

  public DispatchB2Parser(Properties cityCodes, String defCity, String defState) {
    this(null, cityCodes, defCity, defState);
  }
  
  public DispatchB2Parser(String defCity, String defState) {
    this((String)null, defCity, defState);
  }

  public DispatchB2Parser(String prefix, String[] cityList, String defCity, String defState) {
    super(-4, cityList, defCity, defState);
    this.prefixList = prefix.split("\\|\\|");
  }

  public DispatchB2Parser(String prefix, String[] cityList, String defCity, String defState, int flags) {
    super(calcVersionCode(flags), cityList, defCity, defState);
    setup(prefix, flags);
  }

  public DispatchB2Parser(String prefix, int version, String[] cityList, String defCity, String defState) {
    super(version, cityList, defCity, defState);
    setup(prefix, 0);
  }

  public DispatchB2Parser(String prefix, Properties cityCodes, String defCity, String defState) {
    super(-4, cityCodes, defCity, defState);
    setup(prefix,0);
  }
  
  public DispatchB2Parser(String prefix, String defCity, String defState) {
    this(prefix, defCity, defState, 0);
  }
  
  public DispatchB2Parser(String prefix, String defCity, String defState, int flags) {
    super(-4, defCity, defState);
    setup(prefix, flags);
  }
  
  private static int calcVersionCode(int flags) {
    
    int code = ((flags & B2_SEPARATE_APT_FLD) != 0 ? 6 : 4);
    if ((flags & B2_DELIM_ONLY) == 0) code = -code;
    return code;
  }
  
  private void setup(String prefix, int flags) {
    prefixList = (prefix == null ? null : prefix.split("\\|\\|"));
    forceCallCode = (flags & B2_FORCE_CALL_CODE) != 0;
    crossFollows = (flags & B2_CROSS_FOLLOWS) != 0;
    optCallCode = (flags & B2_OPT_CALL_CODE) != 0;
  }
  

  @Override
 protected boolean parseMsg(String body, Data data) {
    
    if (prefixList != null) {
      boolean found = false;
      for (String prefix : prefixList) {
        if (body.startsWith(prefix)) {
          found = true;
          body = body.substring(prefix.length()).trim();
          break;
        }
      }
      if (!found) return false;
    }
    if (super.parseMsg(body, data)) return true;
    if (prefixList == null) return false;
    data.parseGeneralAlert(this, body);
    return true;
  }
  
  
  @Override
  protected boolean isPageMsg(String body) {
    if (prefixList != null) return true;
    return super.isPageMsg(body);
  }
  

  
  /**
   * Processes the complicated first address field
   * Will usually be overridden by subclasses
   * @param field first address field
   * @param data message information data object
   * @return true if parse was successful
   */
  protected boolean parseAddrField(String field, Data data) {
    
    field = field.replaceAll("  +", " ");
    String address = null;
    if (field.length() == 0) return false;
    
    if (field.charAt(0) == '(') {
      int pt = 0;
      int pCnt = 0;
      do {
        if (pt >= field.length()) return false;
        char chr = field.charAt(pt);
        if (chr == '(') pCnt++;
        else if (chr == ')') pCnt--;
        pt++;
      } while (pCnt > 0);
      address = field.substring(pt).trim();
      field = field.substring(1, pt-1);
    }

    Matcher match;
    field = parseCallCode(field, data);
    if (data.strCode.length() == 0 && !optCallCode) return false;
    
    if (address != null) {
      data.strCall = field;
      field = address;
    } else { 
      Pattern ptn = getCallPattern();
      if (ptn != null) {
        match =  ptn.matcher(field);
        if (match.matches()) {
          data.strCall = match.group(1).trim();
          field = match.group(2).trim();
        }
      }
    }

    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    if (data.strCall.length() > 0) {
      st = StartType.START_ADDR;
      flags = 0;
    }

    // look for a phone number.  If found it will either be at the end of
    // the message, or it will mark the end of the address
    String left = null;
    boolean foundPhone = false;
    match = PHONE_PTN.matcher(field);
    if (match.find()) {
      int pt;
      do {
        foundPhone = true;
        data.strPhone = match.group(1).trim();
        left = field.substring(match.end());
        pt = match.start();
      } while (match.find());
      field = field.substring(0, pt);
      if (left.length() == 0) left = null;
    }
    
    if (left != null) {
      left = stripTrailName(left, data);
    } else {
      field = stripTrailName(field, data);
    }
    
    if (crossFollows) flags |= FLAG_CROSS_FOLLOWS;
    if (left != null) flags |=  FLAG_ANCHOR_END;
    flags |= getExtraParseAddressFlags();
    parseAddress(st, flags | FLAG_NEAR_TO_END, field, data);
    if (left == null) left = getLeft();
    data.strApt = stripFieldEnd(data.strApt, " Bldg");
    
    boolean noCross = (data.strCross.length() == 0);
    if (left.length() > 0) {

      if ((flags & FLAG_ANCHOR_END) == 0 && data.strCity.length() == 0) {
        match = INTERSECT_PTN.matcher(left);
        if (match.matches()) {
          if (match.group(1) == null || isValidAddress(left.substring(match.end()).trim())) {
            String connect = " ";
            if (left.startsWith("/") || left.startsWith("&")) {
              left = left.substring(1).trim();
              connect = " & ";
            }
            data.strAddress = append(data.strAddress, connect, left);
            left = "";
          }
        }
      } 
      
      match = APT1_PTN.matcher(left);
      if (match.lookingAt()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        left = left.substring(match.end());
      }
      match = BLDG_PTN.matcher(left);
      if (match.lookingAt()) {
        data.strApt = append(data.strApt, " Bldg ", getOptGroup(match.group(1)));
        left = left.substring(match.end());
      }

      match = CROSS_LABEL_PTN.matcher(left);
      if (match.find()) {
        data.strApt = append(data.strApt, "-", left.substring(0,match.start()).trim());
        left = left.substring(match.start()).trim();
      }
      
      if ((match = APT2_PTN.matcher(left)).matches()) {
        data.strApt = append(data.strApt, "-", left);
      }
      
      else {
        parseCrossName(left, data);
      }
      
      // We can't turn on the FLAG_CROSS_FOLLOWS option or it will mess up following names
      // but if we have identified a cross street, see if the address has a trailing direction
      // symbol that should be attached to the cross street.
      if (noCross && data.strApt.length() == 0 && data.strCross.length() > 0 && 
          data.strCity.length() == 0 && !foundPhone) {
        match = TRAIL_DIR_PTN.matcher(data.strAddress);
        if (match.find() && !TRAIL_DIR_PTN.matcher(data.strCross).find()) {
          data.strCross = match.group(1) + ' ' + data.strCross;
          data.strAddress = data.strAddress.substring(0,match.start());
        }
      }
    }
    
    return data.strAddress.length() > 0;
  }

  private String stripTrailName(String field, Data data) {
    
    field = cleanWirelessCarrier(field, true);
    
    Matcher match;
    match = NAME_PTN.matcher(field);
    if (match.find()) {
      String name = match.group(1);
      if (!notName(name)) {
        data.strName = match.group(1);
        field = field.substring(0,match.start());
      }
    }
    return field;
  }

  private void parseCrossName(String field, Data data) {
    if (data.strCross.length() == 0) {
      Matcher match = CROSS_LABEL_PTN.matcher(field);
      boolean forceCross = match.lookingAt();
      if (forceCross) field = field.substring(match.end());
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field);
      if (forceCross || res.isValid()) {
        res.getData(data);
        field = res.getLeft();
      }
    }
    data.strName = append(field, " ", data.strName);
  }
  
  /***
   * @return Pattern that can be used to separate call description from rest of address line.
   */
  protected Pattern getCallPattern() {
    return null;
  }

  private String parseCallCode(String field, Data data) {
    Matcher match = CODE_PATTERN.matcher(field);
    if (match.find()) {
      data.strCode = match.group(1);
      field = field.substring(match.end());
    } else if (forceCallCode) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strCode = field.substring(0,pt);
        field = field.substring(pt+1).trim();
      }
    }
    return field;
  }

  /**
   * Method that can be called to indicate that something that looks like a name really is not a name value
   * @param name field we have tentatively identified as a name value
   * @return true if this should not be considered a name, false otherwise
   */
  protected boolean notName(String name) {
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("CALL_ADDR")) return new BaseCallAddressField();
    return super.getField(name);
  }

  protected class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = parseCallCode(field, data);
      if (data.strCode.length() == 0 && !optCallCode) abort();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  protected class BaseCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = parseCallCode(field, data);
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }
}
