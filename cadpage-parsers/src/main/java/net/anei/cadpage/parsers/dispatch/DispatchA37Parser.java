package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.Properties;
import java.util.regex.*;

abstract public class DispatchA37Parser extends SmartAddressParser {

  private String prefix;
  
  private boolean checkCity;
  
  public DispatchA37Parser(String prefix, String city, String state) {
    super(city, state);
    this.prefix = prefix;
    this.checkCity = false;
    setFieldList();
  }
  
  public DispatchA37Parser(String prefix, Properties cityCodes, String city, String state) {
    super(cityCodes, city, state);
    this.prefix = prefix;
    this.checkCity = (cityCodes !=  null);
    setFieldList();
  }
  
  public DispatchA37Parser(String prefix, String[] cityList, String city, String state) {
    super(cityList, city, state);
    this.prefix = prefix;
    this.checkCity = (cityList != null);
    setFieldList();
  }
  
  private void setFieldList() {
    if (prefix != null) prefix += ':';
    String fieldList = "ID CALL CODE DATE TIME ADDR APT";
    if (checkCity) fieldList += " CITY";
    setFieldList(fieldList);
  }

  // Separate and parse DispatchID, CALLID, CALL, DATE, TIME and Location/Message fields
  private static final Pattern MASTER_PATTERN
    = Pattern.compile("(?:Call\\s*#\\s*((?:[A-Z]?\\d{2}-|[A-Z]{2})?\\d+?)?)?\\s*(?:\\-\\s*(.+?)\\-\\s+)?\\s*(\\d\\d?/\\d\\d?/\\d{4})\\s+(\\d{2}:\\d{2}:\\d{2})\\s*(?:(Location|Message):\\s*(.*?))?");
  public boolean parseMsg(String body, Data data) {
    if (prefix != null) {
      if (!body.startsWith(prefix)) return false;
      body = body.substring(prefix.length()).trim();
    }
    Matcher m = MASTER_PATTERN.matcher(body);
    if (!m.matches()) return false;
    data.strCallId = getOptGroup(m.group(1));
    parseCallField(getOptGroup(m.group(2)), data);
    data.strDate = m.group(3);
    data.strTime = m.group(4);
    return parseDataField(m.group(5), getOptGroup(m.group(6)), data);
  }

  // Call field is a call type followed by optional call code
  private static final Pattern CALL_PATTERN = Pattern.compile("(.*?)\\s+([A-Z]\\-\\d{1,3}[A-Z]?)?");
  private void parseCallField(String field, Data data) {
    Matcher m = CALL_PATTERN.matcher(field);
    if (m.matches()) {
      data.strCall = m.group(1);
      data.strCode = m.group(2);
    } else
      data.strCall = field;
  }

  // Parses Location: and Message: fields
  private boolean parseDataField(String fieldType, String field, Data data) {
    if (fieldType == null) return false;
    if (fieldType.equals("Location")) return parseLocationField(field, data);
    if (fieldType.equals("Message")) return parseMessageField(field, data);
    return false;
  }

  /**
   * Process Location: field.  May be overridden by subclasses that need to
   * do something more sophisticated then just parse an address
   * @param field data field
   * @param data Data object where parsed fields are returned
   * @return true if successful
   */
  protected boolean parseLocationField(String field, Data data) {
    if (checkCity) {
      parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT | FLAG_ANCHOR_END, field, data);
    } else {
      parseAddress(field, data);
    }
    return true;
 }
  
  abstract protected boolean parseMessageField(String field, Data data);
}
