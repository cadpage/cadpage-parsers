package net.anei.cadpage.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Parser class that convert the generic Cadpage message format to and from
 * a parsed message information object
 */
public class CadpageParser  extends CadpageParserBase {
  
//  private static final Pattern KEYWORD = Pattern.compile("\\b([\\w]+):");
  private static final Pattern KEYWORD = Pattern.compile("(?:^|\n)([\\w]+):");
  
  @Override
  public String getLocName() {
    return "Standard Cadpage Format A";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int fieldCnt = 0;
    int lastPt = 0;
    Field lastField = null;
    
    // Look through message body looking for keyword constructs
    Matcher match = KEYWORD.matcher(body);
    while (match.find()) {
      
      // For each one, extract the keyword name and look it up in the field map
      // If we don't find it there, assume this was a extra colon in the data
      // field and go on to the next one
      String key = match.group(1);
      Field field = getMapField(key);
      if (field != null) {
        
        // Increment the field count,
        fieldCnt++;
        
        // This keyword marks the end of the previous data field.
        // Extract that field value and pass it to the previous
        // field processor
        //
        // If the last field value is not empty, but the last field processor
        // is still null, that means the message body did not start with a
        // recognizable keyword, which is grounds to reject the message
        String value = body.substring(lastPt, match.start()).trim();
        if (value.length() > 0) {
          if (lastField == null) return false;
          lastField.parse(value, data);
        }
        
        // Save last field position and last field processor
        lastField = field;
        lastPt = match.end();
      }
    }
    
    // End of loop.  If we didn't find any recognizable keywords, reject message
    if (lastField == null) return false;
    
    // pass the last field to the last field processor
    lastField.parse(body.substring(lastPt).trim(), data);
    
    // We have to have found at least two fields for this to be considered valid
    return fieldCnt >= 2;
  }

  /**
   * Convert parsed information object into a text string that the Cadpage parser
   * This is only invokved when the Cadpage parsing engine is running on a messaging server
   * can turn back into a parsed information object
   * @param info parsed information object
   * @param delim field separator to place between data fields.  Should be blanks or a newline 
   * @param inclMapAddr true if base map address should be included.  This should only be used
   * when the target is a IPhone or IPad.  Android phones can take care of this themselves
   * and dumb phones users would only be confused.
   * @compatMode compatibility mode option.  If set, generates a text string that is
   * compatible with older client versions
   * @return
   */
  public static String formatInfo(MsgInfo info, String delim, boolean inclMapAddr, boolean compatMode) {
    StringBuilder sb = new StringBuilder();
    MsgInfo.MsgType msgType = info.getMsgType();
    if (!compatMode && msgType != MsgInfo.MsgType.PAGE) append(sb, "TYPE", msgType, delim);
    append(sb, "PRI", info.getPriority(), delim);
    append(sb, "DATE", info.getDate(), delim);
    append(sb, "TIME", info.getTime(), delim);
    String call = (compatMode ? info.getExtendedCall() : info.getCall());
    append(sb, "CALL", call, delim);
    append(sb, "PLACE", info.getPlace(), delim);
    append(sb, "ADDR", info.getAddress(), delim);
    append(sb, "CITY", info.getCity(), delim);
    append(sb, "ST", info.getState(), delim);
    append(sb, "APT", info.getApt(), delim);
    append(sb, "X", info.getCross(), delim);
    append(sb, "BOX", info.getBox(), delim);
    append(sb, "MAP", info.getMap(), delim);
    append(sb, "CH", info.getChannel(), delim);
    append(sb, "UNIT", info.getUnit(), delim);
    append(sb, "INFO", info.getSupp(), delim);
    append(sb, "NAME", info.getName(), delim);
    append(sb, "PH", info.getPhone(), delim);
    append(sb, "CODE", info.getCode(), delim);
    append(sb, "ID", info.getCallId(), delim);
    append(sb, "SRC", info.getSource(), delim);
    append(sb, "URL", info.getInfoURL(), delim);
    if (info.getCity().length() == 0) append(sb, "DCITY", info.getDefCity(), delim);
    if (info.getState().length() == 0) append(sb, "DST", info.getDefState(), delim);
    CountryCode country = info.getCountryCode();
    if (country != CountryCode.US) {
      append(sb, "CO", country.toString(), delim);
    }
    append(sb, "GPS", info.getGPSLoc(), delim);
    if (info.isPreferGPSLoc()) append(sb, "REC_GPS", "Y", delim);
    MsgParser.MapPageStatus mapPageStatus = info.getMapPageStatus();
    if (mapPageStatus != null) append(sb, "MP_STATUS", mapPageStatus.toString(), delim);
    append(sb, "MP_URL", info.getMapPageURL(), delim);
    if (inclMapAddr) {
      append(sb, "MADDR", info.getBaseMapAddress(), delim);
      String mapCity = info.getMapCity();
      if (!mapCity.equals(info.getCity())) {
        append(sb, "MCITY", mapCity, delim);
      }
    }
    MsgParser parser = info.getParser();
    if (parser != null) append(sb, "PARSER", parser.getParserCode(), delim);
    
    return sb.toString();
  }
  
  private static void append(StringBuilder sb, String key, Object value, String delim) {
    String sVal = value == null ? null : value.toString();
    append(sb, key, sVal, delim);
  }
  
  private static void append(StringBuilder sb, String key, String value, String delim) {
    if (value != null && value.length() > 0) {
      if (sb.length() > 0) sb.append(delim);
      sb.append(key);
      sb.append(": ");
      sb.append(value);
    }
  }
}
